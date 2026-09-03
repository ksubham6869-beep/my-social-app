package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.PostEntity
import com.example.app.SocialViewModel

@Composable
fun ExploreScreen(viewModel: SocialViewModel) {
    val posts by viewModel.posts.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search or ask Meta AI", color = Color.Gray) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.DarkGray,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF121212),
                unfocusedContainerColor = Color(0xFF121212)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of images/reels
        val gridItems = posts.flatMap { listOf(it.imageUrl, it.userAvatar, it.imageUrl) }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(gridItems) { url ->
                Box(
                    modifier = Modifier
                        .height(135.dp)
                        .background(Color.DarkGray)
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = "Explore Item",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
