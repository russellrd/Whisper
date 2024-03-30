package com.example.whisper.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.whisper.ui.routes.AnnouncementsRoute

const val ANNOUNCEMENTS_ROUTE = "announcements_route"

fun NavController.navigateToAnnouncements(navOptions: NavOptions) = navigate(ANNOUNCEMENTS_ROUTE, navOptions)

fun NavGraphBuilder.announcementsScreen() {
    composable(route = ANNOUNCEMENTS_ROUTE) {
        AnnouncementsRoute()
    }
}
