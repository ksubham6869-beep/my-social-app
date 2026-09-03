package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PostEntity::class,
        StoryEntity::class,
        ChatEntity::class,
        NotificationEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun socialDao(): SocialDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "social_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.socialDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: SocialDao) {
                // Profile
                dao.insertUserProfile(UserProfileEntity())

                // Stories
                dao.insertStory(StoryEntity(username = "Your story", userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb", mediaUrl = "", isUserStory = true, hasUnseenStory = false, noteText = "Location off"))
                dao.insertStory(StoryEntity(username = "yadav_vivek8", userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d", mediaUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9", noteText = "Chilling 🚀"))
                dao.insertStory(StoryEntity(username = "sonu_kumar", userAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e", mediaUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6", noteText = "Music time 🎵"))
                dao.insertStory(StoryEntity(username = "raus.hani6", userAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330", mediaUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1", noteText = "Exploring ✨"))

                // Posts & Reels
                dao.insertPost(PostEntity(
                    username = "babu_m_k_yadav_302",
                    userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d",
                    imageUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9",
                    caption = "meri kahani 2019 se 2026 tak ✨ In 7 saalalon ne mujhe umr se jyada samajh di. Read carefully 💯",
                    likesCount = 1432,
                    commentsCount = 89,
                    location = "Bihar, India",
                    isReel = false
                ))

                dao.insertPost(PostEntity(
                    username = "insta_explorer",
                    userAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
                    imageUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d",
                    caption = "Turn your life into a movie and discover short, entertaining videos! 🎬🔥 #reels #viral #trending",
                    likesCount = 8420,
                    commentsCount = 312,
                    location = "Mumbai, India",
                    isReel = true,
                    videoUrl = "https://assets.mixkit.co/videos/preview/mixkit-tree-branches-in-the-breeze-1195-large.mp4"
                ))

                dao.insertPost(PostEntity(
                    username = "rohit_vlogz",
                    userAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e",
                    imageUrl = "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800",
                    caption = "Little moments lead to big friendships. Share yours on My Social App! 🌟",
                    likesCount = 954,
                    commentsCount = 42,
                    location = "Goa Trip",
                    isReel = false
                ))

                dao.insertPost(PostEntity(
                    username = "nature_lover_99",
                    userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
                    imageUrl = "https://images.unsplash.com/photo-1501785888041-af3ef285b470",
                    caption = "The mountains are calling and I must go 🏔️✨ #nature #wanderlust",
                    likesCount = 3421,
                    commentsCount = 156,
                    location = "Himalayas",
                    isReel = true,
                    videoUrl = "https://assets.mixkit.co/videos/preview/mixkit-waves-in-the-water-1164-large.mp4"
                ))

                // Chats
                dao.insertChat(ChatEntity(recipientName = "Mafiya Yadav", recipientAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d", lastMessage = "Sent a reel by sulendra9189", timestamp = "7w", unreadCount = 2))
                dao.insertChat(ChatEntity(recipientName = "_yadav___babu___ahir___", recipientAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e", lastMessage = "4+ new messages", timestamp = "14w", unreadCount = 4))
                dao.insertChat(ChatEntity(recipientName = "Yadavinsh Rishi Sarkar", recipientAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330", lastMessage = "2 new messages", timestamp = "17w", unreadCount = 2))
                dao.insertChat(ChatEntity(recipientName = "amolkumar5697", recipientAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb", lastMessage = "Mentioned you in a story", timestamp = "20w", unreadCount = 0))

                // Notifications
                dao.insertNotification(NotificationEntity(username = "sonu__babu__br_09", userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d", actionText = "and 3 others liked your story.", timeAgo = "4w", targetThumbnail = "https://images.unsplash.com/photo-1517841905240-472988babdf9"))
                dao.insertNotification(NotificationEntity(username = "yadavfreefire8youtube", userAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e", actionText = "channel is on Instagram. iamaarav731 and 8 others also follow them.", timeAgo = "3w", isFollowing = false, isFollowBack = true))
                dao.insertNotification(NotificationEntity(username = "ray_muthun", userAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330", actionText = "started following you.", timeAgo = "5w", isFollowing = false, isFollowBack = true))
                dao.insertNotification(NotificationEntity(username = "karankum353", userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb", actionText = "started following you.", timeAgo = "3d", isFollowing = true, isFollowBack = false))
            }
        }
    }
}
