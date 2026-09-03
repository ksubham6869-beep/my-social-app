package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
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
import com.example.data.ChatEntity
import com.example.app.SocialViewModel

@Composable
fun InboxScreen(viewModel: SocialViewModel, onBack: () -> Unit) {
    val chats by viewModel.chats.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    var selectedChat by remember { mutableStateOf<ChatEntity?>(null) }

    if (selectedChat != null) {
        ChatDetailScreen(chat = selectedChat!!, onBack = { selectedChat = null })
        return
    }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "insta_lover_for_2025", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = " ∨", color = Color.White, fontSize = 14.sp)
                }
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "New Message", tint = Color.White)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Meta AI
            OutlinedTextField(
                value = "",
                onValueChange = {},
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
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Notes Row
            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(70.dp)) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.DarkGray)
                        ) {
                            AsyncImage(
                                model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
                                contentDescription = "Your note",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Your note", color = Color.Gray, fontSize = 12.sp)
                    }
                }
                items(chats) { chat ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(70.dp)) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.DarkGray)
                        ) {
                            AsyncImage(
                                model = chat.recipientAvatar,
                                contentDescription = chat.recipientName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = chat.recipientName, color = Color.Gray, fontSize = 12.sp, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                    }
                }
            }

            // Tabs: Primary 12, Requests, General
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TabButton(text = "Primary 12", isSelected = selectedTab == 0) { selectedTab = 0 }
                TabButton(text = "Requests", isSelected = selectedTab == 1) { selectedTab = 1 }
                TabButton(text = "General", isSelected = selectedTab == 2) { selectedTab = 2 }
            }

            // Chat List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(chats) { chat ->
                    ChatRowItem(chat = chat, onClick = { selectedChat = chat })
                }
            }
        }
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) Color(0xFF262626) else Color.Transparent,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ChatRowItem(chat: ChatEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            AsyncImage(
                model = chat.recipientAvatar,
                contentDescription = chat.recipientName,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = chat.recipientName, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "${chat.lastMessage} • ${chat.timestamp}", color = Color.Gray, fontSize = 13.sp, maxLines = 1)
            }
        }
        if (chat.unreadCount > 0) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF0095F6), CircleShape)
            )
        }
    }
}

@Composable
fun ChatDetailScreen(chat: ChatEntity, onBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(chat.lastMessage, "Hello! How are you?", "Check out this reel!")) }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    AsyncImage(
                        model = chat.recipientAvatar,
                        contentDescription = chat.recipientName,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = chat.recipientName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Message...", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.DarkGray,
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF121212),
                        unfocusedContainerColor = Color(0xFF121212)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            messages = messages + messageText
                            messageText = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0095F6))
                ) {
                    Text(text = "Send")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                Surface(
                    color = Color(0xFF262626),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = msg,
                        color = Color.White,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
