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
const val RECEIVER_ID_ARG = "receiverId"
const val MESSAGE_CHANNEL = "messageChannel"


const val DIRECT_MESSAGES_ROUTE = "$DIRECT_MESSAGES_ROUTE_BASE?$USER_ID_ARG={$USER_ID_ARG}?$RECEIVER_ID_ARG={$RECEIVER_ID_ARG}?$MESSAGE_CHANNEL={$MESSAGE_CHANNEL}"

fun NavController.navigateToDirectMessages(userId: String, receiverId: String, message_channel: String, navOptions: NavOptions? = null) {
    navigate("${DIRECT_MESSAGES_ROUTE_BASE}?${USER_ID_ARG}=$userId?${RECEIVER_ID_ARG}=$receiverId?${MESSAGE_CHANNEL}=$message_channel", navOptions)
}

fun NavGraphBuilder.directMessagesScreen() {
    composable(
        route = DIRECT_MESSAGES_ROUTE,
        arguments = listOf(
            navArgument(USER_ID_ARG) {
                type = NavType.StringType
                defaultValue = "-1"
            },
            navArgument(RECEIVER_ID_ARG) {
                type = NavType.StringType
                defaultValue = "-1"
            },
            navArgument(MESSAGE_CHANNEL) {
                type = NavType.StringType
                defaultValue = "-1"
            }
        )
    ) {
        val userId = requireNotNull(it.arguments).getString(USER_ID_ARG) ?: error("Missing friendName argument")
        val receiverId = requireNotNull(it.arguments).getString(RECEIVER_ID_ARG) ?: error("Missing friendName argument")
        val message_channel = requireNotNull(it.arguments).getString(MESSAGE_CHANNEL) ?: error("Missing friendName argument")
        DirectMessagesRoute(userId, receiverId, message_channel)
    }
}
