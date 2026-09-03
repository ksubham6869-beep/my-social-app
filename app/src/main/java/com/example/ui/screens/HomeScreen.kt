package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.PostEntity
import com.example.data.StoryEntity
import com.example.app.SocialViewModel

@Composable
fun HomeScreen(
    viewModel: SocialViewModel,
    onNavigateToInbox: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val posts by viewModel.posts.collectAsState()
    val stories by viewModel.stories.collectAsState()
    var showCreateStoryDialog by remember { mutableStateOf(false) }
    var selectedStoryToView by remember { mutableStateOf<StoryEntity?>(null) }

    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MySocial",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(onClick = onNavigateToNotifications) {
                        Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Notifications", tint = Color.White)
                    }
                    IconButton(onClick = onNavigateToInbox) {
                        Icon(imageVector = Icons.Outlined.NearMe, contentDescription = "Direct Messages", tint = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Stories Row
            item {
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(stories) { story ->
                        StoryItem(
                            story = story,
                            onClick = {
                                if (story.isUserStory) {
                                    showCreateStoryDialog = true
                                } else {
                                    selectedStoryToView = story
                                }
                            }
                        )
                    }
                }
                HorizontalDivider(color = Color(0xFF2A2A2A), thickness = 0.5.dp)
            }

            // Posts Feed
            items(posts) { post ->
                PostCard(post = post, onLikeToggle = { viewModel.toggleLike(post) })
            }
        }
    }

    // Create Story Dialog
    if (showCreateStoryDialog) {
        var mediaUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1507525428034-b723cf961d3e") }
        var noteText by remember { mutableStateOf("") }
        val presetImages = listOf(
            "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
            "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05",
            "https://images.unsplash.com/photo-1519681393784-d120267933ba",
            "https://images.unsplash.com/photo-1426604966848-d7adac902bff"
        )

        AlertDialog(
            onDismissRequest = { showCreateStoryDialog = false },
            containerColor = Color(0xFF1E1E1E),
            title = { Text("Create Temporary Story", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Expires in 24 hours automatically", color = Color(0xFFF9CE34), fontSize = 12.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black)
                    ) {
                        AsyncImage(
                            model = mediaUrl,
                            contentDescription = "Story preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        if (noteText.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(12.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = noteText, color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }

                    Text("Select preset backdrop:", color = Color(0xFFA1A1AA), fontSize = 12.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(presetImages) { url ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = if (mediaUrl == url) 2.dp else 0.dp,
                                        color = Color(0xFFF9CE34),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { mediaUrl = url }
                            ) {
                                AsyncImage(model = url, contentDescription = "preset", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = { Text("Add caption or note...", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFF9CE34),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color(0xFFF9CE34)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addStory(mediaUrl = mediaUrl, noteText = noteText)
                        showCreateStoryDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF9CE34))
                ) {
                    Text("Share to Story", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateStoryDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    // View Story Fullscreen Dialog
    selectedStoryToView?.let { story ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable { selectedStoryToView = null },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = story.mediaUrl,
                contentDescription = story.username,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Top Progress & Header
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LinearProgressIndicator(
                        progress = { 0.75f },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AsyncImage(
                                model = story.userAvatar,
                                contentDescription = story.username,
                                modifier = Modifier.size(36.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text(text = story.username, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(text = "• 24h left", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                        }
                        IconButton(onClick = { selectedStoryToView = null }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }

                if (story.noteText.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = story.noteText, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                } else {
                    Spacer(modifier = Modifier.height(1.dp))
                }
            }
        }
    }
}

@Composable
fun StoryItem(story: StoryEntity, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(74.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .then(
                    if (!story.isUserStory) {
                        Modifier.border(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFF9CE34), Color(0xFFEE2A7B), Color(0xFF6228D7))
                            ),
                            shape = CircleShape,
                            width = 2.5.dp
                        )
                    } else {
                        Modifier.border(
                            color = Color(0xFF2A2A2A),
                            shape = CircleShape,
                            width = 2.dp
                        )
                    }
                )
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E1E))
            ) {
                AsyncImage(
                    model = story.userAvatar,
                    contentDescription = story.username,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            if (story.isUserStory) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomEnd)
                        .background(Color(0xFFF9CE34), CircleShape)
                        .border(2.dp, Color(0xFF121212), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add story", tint = Color(0xFF121212), modifier = Modifier.size(14.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = story.username,
            color = Color(0xFFA1A1AA),
            fontSize = 11.sp,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PostCard(post: PostEntity, onLikeToggle: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(Color(0xFF121212))
    ) {
        // Post Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = post.userAvatar,
                    contentDescription = post.username,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = post.username, color = Color(0xFFE3E3E3), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    if (post.location.isNotBlank()) {
                        Text(text = post.location, color = Color(0xFFA1A1AA), fontSize = 11.sp)
                    }
                }
            }
            Text(text = "...", color = Color(0xFFE3E3E3), fontWeight = FontWeight.Bold)
        }

        // Post Image/Video with rounded corners & shadow look
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF1E1E1E))
        ) {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = post.caption,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(onClick = onLikeToggle) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) Color(0xFFEE2A7B) else Color(0xFFE3E3E3)
                    )
                }
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Outlined.ModeComment, contentDescription = "Comment", tint = Color(0xFFE3E3E3))
                }
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Outlined.NearMe, contentDescription = "Share", tint = Color(0xFFE3E3E3))
                }
            }
            Text(text = "🔖", fontSize = 20.sp)
        }

        // Likes count & Caption
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {
            Text(text = "${post.likesCount} likes", color = Color(0xFFE3E3E3), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Row {
                Text(text = post.username, color = Color(0xFFE3E3E3), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = post.caption, color = Color(0xFFE3E3E3), fontSize = 13.sp, maxLines = 3)
            }
            if (post.commentsCount > 0) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "View all ${post.commentsCount} comments", color = Color(0xFFA1A1AA), fontSize = 12.sp)
            }
        }
    }
}

