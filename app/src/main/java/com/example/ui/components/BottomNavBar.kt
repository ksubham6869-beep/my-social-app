package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class AppTab(val title: String) {
    HOME("Home"),
    REELS("Reels"),
    CREATE("Create"),
    EXPLORE("Explore"),
    PROFILE("Profile")
}

@Composable
fun BottomNavBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF121212),
        contentColor = Color(0xFFE3E3E3),
        modifier = Modifier.height(64.dp)
    ) {
        NavigationBarItem(
            selected = currentTab == AppTab.HOME,
            onClick = { onTabSelected(AppTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF121212),
                unselectedIconColor = Color(0xFFA1A1AA),
                indicatorColor = Color(0xFFF9CE34)
            )
        )
        NavigationBarItem(
            selected = currentTab == AppTab.REELS,
            onClick = { onTabSelected(AppTab.REELS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.REELS) Icons.Filled.OndemandVideo else Icons.Outlined.OndemandVideo,
                    contentDescription = "Reels"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFE3E3E3),
                unselectedIconColor = Color(0xFFA1A1AA),
                indicatorColor = Color(0xFF2A2A2A)
            )
        )
        NavigationBarItem(
            selected = currentTab == AppTab.CREATE,
            onClick = { onTabSelected(AppTab.CREATE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.CREATE) Icons.Filled.AddBox else Icons.Outlined.AddBox,
                    contentDescription = "Create"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF121212),
                unselectedIconColor = Color(0xFFA1A1AA),
                indicatorColor = Color(0xFFF9CE34)
            )
        )
        NavigationBarItem(
            selected = currentTab == AppTab.EXPLORE,
            onClick = { onTabSelected(AppTab.EXPLORE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.EXPLORE) Icons.Filled.Explore else Icons.Outlined.Explore,
                    contentDescription = "Explore"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFE3E3E3),
                unselectedIconColor = Color(0xFFA1A1AA),
                indicatorColor = Color(0xFF2A2A2A)
            )
        )
        NavigationBarItem(
            selected = currentTab == AppTab.PROFILE,
            onClick = { onTabSelected(AppTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFE3E3E3),
                unselectedIconColor = Color(0xFFA1A1AA),
                indicatorColor = Color(0xFF2A2A2A)
            )
        )
    }
}
