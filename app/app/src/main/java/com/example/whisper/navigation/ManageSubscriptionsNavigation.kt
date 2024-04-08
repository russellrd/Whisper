package com.example.whisper.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.whisper.ui.routes.ManageSubscriptionsRoute
import com.example.whisper.ui.routes.SettingsRoute

const val MANAGE_SUBSCRIPTIONS_ROUTE = "manage_subscriptions_route"

fun NavController.navigateToManageSubscriptions(navOptions: NavOptions ? = null) = navigate(MANAGE_SUBSCRIPTIONS_ROUTE, navOptions)

fun NavGraphBuilder.manageSubscriptionsScreen(navigateToAnnouncementPage: (String) -> Unit) {
    composable(route = MANAGE_SUBSCRIPTIONS_ROUTE) {
        ManageSubscriptionsRoute(navigateToAnnouncementPage = navigateToAnnouncementPage)
    }
}
