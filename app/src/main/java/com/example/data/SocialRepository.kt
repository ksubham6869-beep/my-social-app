package com.example.data

import kotlinx.coroutines.flow.Flow

class SocialRepository(private val dao: SocialDao) {
    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()
    val allStories: Flow<List<StoryEntity>> = dao.getAllStories()
    val allChats: Flow<List<ChatEntity>> = dao.getAllChats()
    val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()
    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()

    suspend fun insertPost(post: PostEntity) = dao.insertPost(post)
    suspend fun updatePost(post: PostEntity) = dao.updatePost(post)
    suspend fun insertStory(story: StoryEntity) = dao.insertStory(story)
    suspend fun insertChat(chat: ChatEntity) = dao.insertChat(chat)
    suspend fun insertNotification(notification: NotificationEntity) = dao.insertNotification(notification)
    suspend fun updateNotification(notification: NotificationEntity) = dao.updateNotification(notification)
    suspend fun insertUserProfile(profile: UserProfileEntity) = dao.insertUserProfile(profile)
}
