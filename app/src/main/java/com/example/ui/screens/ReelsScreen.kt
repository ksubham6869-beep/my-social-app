package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.PostEntity
import com.example.app.SocialViewModel
import com.example.util.AnalyticsHelper

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReelsScreen(viewModel: SocialViewModel) {
    val context = LocalContext.current
    val analyticsHelper = remember { AnalyticsHelper(context) }
    val posts by viewModel.posts.collectAsState()
    val reels = posts.filter { it.isReel || it.imageUrl.contains("unsplash") } // fallback to show rich reels/videos
    val isRefreshing by viewModel.isRefreshing
    var selectedReelForComments by remember { mutableStateOf<PostEntity?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            analyticsHelper.logSearchQuery(searchQuery)
        }
    }

    val filteredReels = remember(reels, searchQuery) {
        if (searchQuery.isBlank()) {
            reels
        } else {
            reels.filter {
                it.username.contains(searchQuery, ignoreCase = true) ||
                it.caption.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshPosts() },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            if (filteredReels.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = if (searchQuery.isNotBlank()) "No matching reels found" else "No Reels available", color = Color.White)
                }
            } else {
                val listState = rememberLazyListState()
                val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
                val viewportCenter = (listState.layoutInfo.viewportEndOffset - listState.layoutInfo.viewportStartOffset) / 2
                val activeIndex = visibleItemsInfo.minByOrNull { item ->
                    val itemCenter = item.offset + item.size / 2
                    kotlin.math.abs(itemCenter - viewportCenter)
                }?.index ?: 0

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(filteredReels) { index, reel ->
                        ReelItem(
                            reel = reel,
                            isPlaying = index == activeIndex,
                            onLikeToggle = { viewModel.toggleLike(reel) },
                            onCommentClick = { selectedReelForComments = reel }
                        )
                    }
                }
            }

            // Top Header & Search Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Reels", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Camera", tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by creator or tag (#)...", color = Color.Gray) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.White) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF9CE34),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFFF9CE34),
                        focusedContainerColor = Color.DarkGray.copy(alpha = 0.7f),
                        unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                )
            }

            selectedReelForComments?.let { reel ->
                CommentsBottomSheet(
                    reel = reel,
                    viewModel = viewModel,
                    onDismiss = { selectedReelForComments = null }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    reel: PostEntity,
    viewModel: SocialViewModel,
    onDismiss: () -> Unit
) {
    val commentsMap by viewModel.commentsMap
    val comments = commentsMap[reel.id] ?: listOf("Awesome video! 🔥", "So cinematic 🌅", "Love this!")
    var commentText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        contentColor = Color(0xFFE3E3E3)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .padding(16.dp)
        ) {
            Text(
                text = "Comments (${reel.commentsCount})",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(comments) { comment ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = Color(0xFF2A2A2A)
                        ) {
                            AsyncImage(
                                model = reel.userAvatar,
                                contentDescription = "Avatar",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = reel.username, color = Color(0xFFA1A1AA), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = comment, color = Color(0xFFE3E3E3), fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Add a comment...", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2A2A2A),
                        unfocusedBorderColor = Color(0xFF2A2A2A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF121212),
                        unfocusedContainerColor = Color(0xFF121212)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            viewModel.addComment(reel.id, commentText)
                            commentText = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF9CE34)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Post", color = Color(0xFF121212), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReelItem(reel: PostEntity, isPlaying: Boolean, onLikeToggle: () -> Unit, onCommentClick: () -> Unit) {
    val context = LocalContext.current
    val analyticsHelper = remember { AnalyticsHelper(context) }
    var isFollowing by remember { mutableStateOf(false) }
    var showHeart by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var isFullScreen by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var isPlayingOverride by remember { mutableStateOf<Boolean?>(null) }
    val effectivePlaying = isPlayingOverride ?: isPlaying

    LaunchedEffect(isPlaying) {
        if (!isPlaying) {
            isPlayingOverride = null
        }
    }

    LaunchedEffect(effectivePlaying, reel.id) {
        if (effectivePlaying) {
            val startTime = System.currentTimeMillis()
            try {
                kotlinx.coroutines.delay(Long.MAX_VALUE)
            } finally {
                val duration = System.currentTimeMillis() - startTime
                if (duration > 500) {
                    analyticsHelper.logVideoView(reel.id.toString(), reel.username, duration)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .let { if (isFullScreen) it.fillMaxSize() else it.height(680.dp) }
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (!reel.isLiked) {
                            onLikeToggle()
                        }
                        showHeart = true
                    },
                    onTap = {
                        if (isFullScreen) {
                            isFullScreen = false
                        } else {
                            isPlayingOverride = !effectivePlaying
                        }
                    }
                )
            }
    ) {
        AsyncImage(
            model = reel.imageUrl,
            contentDescription = reel.caption,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for readability (hidden in full-screen)
        if (!isFullScreen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 400f
                        )
                    )
            )
        }

        // Mute / Unmute audio button overlay
        IconButton(
            onClick = {
                isMuted = !isMuted
                analyticsHelper.logClickThrough(reel.id.toString(), if (isMuted) "mute" else "unmute")
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .size(36.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                imageVector = if (isMuted) Icons.Filled.VolumeOff else Icons.Filled.VolumeUp,
                contentDescription = if (isMuted) "Unmute" else "Mute",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Paused indicator overlay when not playing
        if (!effectivePlaying && !showHeart) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Paused",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // Double-tap heart animation overlay
        if (showHeart) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(800)
                showHeart = false
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val scale by animateFloatAsState(
                    targetValue = if (showHeart) 1.3f else 0.2f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "heartScale"
                )
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Liked",
                    tint = Color(0xFFEE2A7B),
                    modifier = Modifier
                        .size(110.dp)
                        .scale(scale)
                )
            }
        }

        // Right side action buttons (hidden in full-screen)
        if (!isFullScreen) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {
                        onLikeToggle()
                        analyticsHelper.logClickThrough(reel.id.toString(), if (reel.isLiked) "unlike" else "like")
                    }) {
                        Icon(
                            imageVector = if (reel.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (reel.isLiked) Color(0xFFEE2A7B) else Color(0xFFE3E3E3),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(text = "${reel.likesCount}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {
                        onCommentClick()
                        analyticsHelper.logClickThrough(reel.id.toString(), "comment_click")
                    }) {
                        Icon(imageVector = Icons.Filled.ModeComment, contentDescription = "Comments", tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                    Text(text = "${reel.commentsCount}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                val context = LocalContext.current
                IconButton(onClick = {
                    analyticsHelper.logClickThrough(reel.id.toString(), "share")
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check out this reel by @${reel.username}: ${reel.caption}")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }) {
                    Icon(imageVector = Icons.Outlined.NearMe, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(30.dp))
                }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More", tint = Color.White, modifier = Modifier.size(26.dp))
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (isFullScreen) "Exit Full Screen" else "Full Screen View") },
                            onClick = {
                                showMenu = false
                                isFullScreen = !isFullScreen
                                analyticsHelper.logClickThrough(reel.id.toString(), if (isFullScreen) "enter_fullscreen" else "exit_fullscreen")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Report") },
                            onClick = {
                                showMenu = false
                                Toast.makeText(context, "Content reported. Thank you for your feedback.", Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Not Interested") },
                            onClick = {
                                showMenu = false
                                Toast.makeText(context, "Post hidden.", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.DarkGray
                ) {
                    AsyncImage(
                        model = reel.userAvatar,
                        contentDescription = "Audio",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        } else {
            // Floating menu in full screen to allow exit or report
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                IconButton(onClick = { showMenu = true }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More", tint = Color.White, modifier = Modifier.size(30.dp))
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Exit Full Screen") },
                        onClick = {
                            showMenu = false
                            isFullScreen = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Report") },
                        onClick = {
                            showMenu = false
                            Toast.makeText(context, "Content reported. Thank you for your feedback.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        // Bottom left user & caption info (hidden in full-screen)
        if (!isFullScreen) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .width(280.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = reel.userAvatar,
                        contentDescription = reel.username,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = reel.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            isFollowing = !isFollowing
                            analyticsHelper.logClickThrough(reel.id.toString(), if (isFollowing) "follow" else "unfollow")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFollowing) Color.Transparent else Color(0xFFF9CE34),
                            contentColor = if (isFollowing) Color.White else Color(0xFF121212)
                        ),
                        border = if (isFollowing) ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color.White)) else null,
                        modifier = Modifier.height(30.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text(text = if (isFollowing) "Following" else "Follow", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(text = reel.caption, color = Color.White, fontSize = 13.sp, maxLines = 2)

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Visibility, contentDescription = "Views", tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "${(reel.likesCount * 14) + 420} views", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.MusicNote, contentDescription = "Audio", tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "${reel.username} • Original audio", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        // Custom playback progress bar at the bottom (hidden in full-screen)
        if (!isFullScreen) {
            var progress by remember { mutableFloatStateOf(0f) }
            LaunchedEffect(effectivePlaying) {
                if (effectivePlaying) {
                    while (true) {
                        val anim = Animatable(progress)
                        try {
                            anim.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = ((1f - progress) * 6000).toInt(),
                                    easing = LinearEasing
                                )
                            ) {
                                progress = value
                            }
                            progress = 0f
                        } catch (e: Exception) {
                            break
                        }
                    }
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.BottomCenter),
                color = Color(0xFFF9CE34),
                trackColor = Color.White.copy(alpha = 0.2f)
            )
        }
    }
}
