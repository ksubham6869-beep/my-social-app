package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.outlined.Menu
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
import com.example.data.UserProfileEntity
import com.example.app.SocialViewModel

@Composable
fun ProfileScreen(viewModel: SocialViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val posts by viewModel.posts.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    val userProfile = profile ?: UserProfileEntity()

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userProfile.username,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "➕", fontSize = 22.sp)
                    Icon(imageVector = Icons.Outlined.Menu, contentDescription = "Menu", tint = Color.White)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Profile Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatColumn(count = "${userProfile.postsCount}", label = "posts")
                    StatColumn(count = "${userProfile.followersCount}", label = "followers")
                    StatColumn(count = "${userProfile.followingCount}", label = "following")
                }
            }

            // Bio & Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = userProfile.fullName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = userProfile.category, color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = userProfile.bio, color = Color.White, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: Edit profile, Share profile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF262626)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                ) {
                    Text(text = "Edit profile", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF262626)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                ) {
                    Text(text = "Share profile", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Professional Dashboard Banner
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Professional dashboard", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Tools and resources just for creators.", color = Color.Gray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = { selectedTab = 0 }) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Posts",
                        tint = if (selectedTab == 0) Color.White else Color.Gray
                    )
                }
                IconButton(onClick = { selectedTab = 1 }) {
                    Icon(
                        imageVector = Icons.Default.OndemandVideo,
                        contentDescription = "Reels",
                        tint = if (selectedTab == 1) Color.White else Color.Gray
                    )
                }
                IconButton(onClick = { selectedTab = 2 }) {
                    Icon(
                        imageVector = Icons.Default.PersonPin,
                        contentDescription = "Tagged",
                        tint = if (selectedTab == 2) Color.White else Color.Gray
                    )
                }
            }
            HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)

            // Posts Grid
            val images = posts.map { it.imageUrl }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(images) { imgUrl ->
                    Box(
                        modifier = Modifier
                            .height(130.dp)
                            .background(Color.DarkGray)
                    ) {
                        AsyncImage(
                            model = imgUrl,
                            contentDescription = "Post Grid",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatColumn(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = label, color = Color.Gray, fontSize = 13.sp)
    }
}
