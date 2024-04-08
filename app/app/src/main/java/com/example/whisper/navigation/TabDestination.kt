package com.example.whisper.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.whisper.R

// Outline all bottom navigation tab destinations
enum class TabDestination(
    val destinationNameId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    MESSAGES(
        destinationNameId = R.string.nav_messages,
        selectedIcon = Icons.Filled.Email,
        unselectedIcon = Icons.Outlined.Email
    ),
    ANNOUNCEMENTS(
        destinationNameId = R.string.nav_announcements,
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info
    ),
    SETTINGS(
        destinationNameId = R.string.nav_settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}
