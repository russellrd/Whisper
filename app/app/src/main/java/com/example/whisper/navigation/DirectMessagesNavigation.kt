package com.example.whisper.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.whisper.ui.routes.DirectMessagesRoute

const val DIRECT_MESSAGES_ROUTE_BASE = "direct_messages_route"
const val USER_ID_ARG = "userId"
const val DIRECT_MESSAGES_ROUTE = "$DIRECT_MESSAGES_ROUTE_BASE?$USER_ID_ARG={$USER_ID_ARG}"

fun NavController.navigateToDirectMessages(userId: String, navOptions: NavOptions? = null) {
    navigate("${DIRECT_MESSAGES_ROUTE_BASE}?${USER_ID_ARG}=$userId", navOptions)
}

fun NavGraphBuilder.directMessagesScreen() {
    composable(
        route = DIRECT_MESSAGES_ROUTE,
        arguments = listOf(
            navArgument(USER_ID_ARG) {
                type = NavType.StringType
                defaultValue = "-1"
            }
        )
    ) {
        val userId = requireNotNull(it.arguments).getString(USER_ID_ARG) ?: error("Missing user id argument")
        DirectMessagesRoute(userId)
    }
}
