package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.SocialViewModel

@Composable
fun CreatePostScreen(viewModel: SocialViewModel, onPostCreated: () -> Unit) {
    var caption by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isReel by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Create New Post or Reel",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = caption,
                onValueChange = { caption = it },
                placeholder = { Text("Write a caption...", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF121212),
                    unfocusedContainerColor = Color(0xFF121212)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                placeholder = { Text("Image URL (optional Unsplash link)", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF121212),
                    unfocusedContainerColor = Color(0xFF121212)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Publish as Reel (Short-form video)", color = Color.White, fontSize = 14.sp)
                Switch(checked = isReel, onCheckedChange = { isReel = it })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (caption.isNotBlank()) {
                        viewModel.addPost(caption = caption, imageUrl = imageUrl, isReel = isReel)
                        onPostCreated()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0095F6)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Share", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
