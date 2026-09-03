package com.example.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ChatEntity
import com.example.data.NotificationEntity
import com.example.data.PostEntity
import com.example.data.SocialRepository
import com.example.data.StoryEntity
import com.example.data.UserProfileEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SocialViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SocialRepository

    init {
        val dao = AppDatabase.getDatabase(application).socialDao()
        repository = SocialRepository(dao)
    }

    val posts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stories: StateFlow<List<StoryEntity>> = repository.allStories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chats: StateFlow<List<ChatEntity>> = repository.allChats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _commentsMap = androidx.compose.runtime.mutableStateOf<Map<Long, List<String>>>(emptyMap())
    val commentsMap: androidx.compose.runtime.State<Map<Long, List<String>>> = _commentsMap

    private val _isRefreshing = androidx.compose.runtime.mutableStateOf(false)
    val isRefreshing: androidx.compose.runtime.State<Boolean> = _isRefreshing

    fun refreshPosts() {
        viewModelScope.launch {
            _isRefreshing.value = true
            kotlinx.coroutines.delay(1200)
            _isRefreshing.value = false
        }
    }

    fun addComment(postId: Long, commentText: String) {
        viewModelScope.launch {
            val existing = _commentsMap.value[postId] ?: listOf("Awesome video! 🔥", "So cinematic 🌅", "Love this!")
            _commentsMap.value = _commentsMap.value + (postId to (existing + commentText))

            posts.value.find { it.id == postId }?.let { post ->
                val updated = post.copy(commentsCount = post.commentsCount + 1)
                repository.updatePost(updated)
            }
        }
    }

    fun toggleLike(post: PostEntity) {
        viewModelScope.launch {
            val updated = post.copy(
                isLiked = !post.isLiked,
                likesCount = if (post.isLiked) post.likesCount - 1 else post.likesCount + 1
            )
            repository.updatePost(updated)
        }
    }

    fun addPost(caption: String, imageUrl: String, isReel: Boolean, videoUrl: String = "", location: String = "India") {
        viewModelScope.launch {
            val newPost = PostEntity(
                username = "insta_lover_for_2025",
                userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
                imageUrl = if (imageUrl.isBlank()) "https://images.unsplash.com/photo-1517841905240-472988babdf9" else imageUrl,
                caption = caption,
                likesCount = 1,
                commentsCount = 0,
                location = location,
                isReel = isReel,
                videoUrl = videoUrl
            )
            repository.insertPost(newPost)
        }
    }

    fun addStory(mediaUrl: String, noteText: String) {
        viewModelScope.launch {
            val newStory = StoryEntity(
                username = "Your story",
                userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
                mediaUrl = mediaUrl,
                isUserStory = true,
                hasUnseenStory = false,
                noteText = noteText
            )
            repository.insertStory(newStory)
        }
    }

    fun followBack(notification: NotificationEntity) {
        viewModelScope.launch {
            val updated = notification.copy(isFollowing = true, isFollowBack = false)
            repository.updateNotification(updated)
        }
    }

    fun updateProfile(bio: String, fullName: String) {
        viewModelScope.launch {
            userProfile.value?.let { profile ->
                repository.insertUserProfile(profile.copy(bio = bio, fullName = fullName))
            }
        }
    }
}
