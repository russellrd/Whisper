package com.example.whisper.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.whisper.ui.routes.AnnouncementsPageRoute
import com.example.whisper.ui.routes.ManageSubscriptionsRoute

const val ANNOUNCEMENT_PAGE_ROUTE = "announcement_page_route"

fun NavController.navigateToAnnouncementPage(announcementId: String, navOptions: NavOptions ? = null) = navigate(ANNOUNCEMENT_PAGE_ROUTE, navOptions)

fun NavGraphBuilder.announcementPageScreen() {
    composable(route = ANNOUNCEMENT_PAGE_ROUTE) {
        AnnouncementsPageRoute()
    }
}
