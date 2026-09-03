package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.NotificationEntity
import com.example.app.SocialViewModel

@Composable
fun NotificationsScreen(viewModel: SocialViewModel, onBack: () -> Unit) {
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Notifications", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(text = "Featured", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(notifications) { notif ->
                NotificationItem(notification = notif, onFollowBack = { viewModel.followBack(notif) })
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationEntity, onFollowBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            AsyncImage(
                model = notification.userAvatar,
                contentDescription = notification.username,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row {
                    Text(
                        text = notification.username,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${notification.actionText} ${notification.timeAgo}",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        maxLines = 2
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        if (notification.isFollowBack) {
            Button(
                onClick = onFollowBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0095F6)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(text = "Follow back", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        } else if (notification.targetThumbnail.isNotBlank()) {
            AsyncImage(
                model = notification.targetThumbnail,
                contentDescription = "Thumbnail",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
