package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.app.SocialViewModel
import com.example.ui.components.AppTab
import com.example.ui.components.BottomNavBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: SocialViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var currentTab by remember { mutableStateOf(AppTab.HOME) }
                var currentSubScreen by remember { mutableStateOf<String?>(null) } // "inbox", "notifications"

                if (!isLoggedIn) {
                    LoginScreen(onLoginSuccess = { isLoggedIn = true })
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (currentSubScreen == null) {
                                BottomNavBar(
                                    currentTab = currentTab,
                                    onTabSelected = { tab ->
                                        currentTab = tab
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            if (currentSubScreen == "inbox") {
                                InboxScreen(viewModel = viewModel, onBack = { currentSubScreen = null })
                            } else if (currentSubScreen == "notifications") {
                                NotificationsScreen(viewModel = viewModel, onBack = { currentSubScreen = null })
                            } else {
                                when (currentTab) {
                                    AppTab.HOME -> HomeScreen(
                                        viewModel = viewModel,
                                        onNavigateToInbox = { currentSubScreen = "inbox" },
                                        onNavigateToNotifications = { currentSubScreen = "notifications" }
                                    )
                                    AppTab.REELS -> ReelsScreen(viewModel = viewModel)
                                    AppTab.CREATE -> CreatePostScreen(
                                        viewModel = viewModel,
                                        onPostCreated = { currentTab = AppTab.HOME }
                                    )
                                    AppTab.EXPLORE -> ExploreScreen(viewModel = viewModel)
                                    AppTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
