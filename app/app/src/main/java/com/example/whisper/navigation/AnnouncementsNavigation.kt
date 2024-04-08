package com.example.whisper.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.whisper.ui.routes.AnnouncementsRoute

const val ANNOUNCEMENTS_ROUTE = "announcements_route"

fun NavController.navigateToAnnouncements(navOptions: NavOptions ? = null) = navigate(ANNOUNCEMENTS_ROUTE, navOptions)
fun NavGraphBuilder.announcementsScreen(
    navigateToManageSubscriptions: () -> Unit,
    navigateToAnnouncementPage: (String) -> Unit

) {
    composable(route = ANNOUNCEMENTS_ROUTE) {
        AnnouncementsRoute(
            navigateToAnnouncementPage = navigateToAnnouncementPage,
            navigateToManageSubscriptions = navigateToManageSubscriptions
        )
    }
}
