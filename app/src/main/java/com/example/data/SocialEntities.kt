package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val userAvatar: String,
    val imageUrl: String,
    val caption: String,
    var likesCount: Int,
    var commentsCount: Int,
    var isLiked: Boolean = false,
    val location: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isReel: Boolean = false,
    val videoUrl: String = ""
)

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val userAvatar: String,
    val mediaUrl: String,
    val isUserStory: Boolean = false,
    val hasUnseenStory: Boolean = true,
    val noteText: String = ""
)

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipientName: String,
    val recipientAvatar: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val userAvatar: String,
    val actionText: String,
    val timeAgo: String,
    val isFollowing: Boolean = false,
    val isFollowBack: Boolean = false,
    val targetThumbnail: String = ""
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "insta_lover_for_2025",
    val fullName: String = "Insta Lover",
    val bio: String = "Am insta lover please ❤️ follow my profile\nmai alag alag 🥰 content lata rahta hu 👉",
    val avatarUrl: String = "",
    val postsCount: Int = 16,
    val followersCount: Int = 109,
    val followingCount: Int = 192,
    val category: String = "Musician/band"
)
