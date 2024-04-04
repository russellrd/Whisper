package com.example.whisper.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.whisper.ui.routes.MessagesRoute

const val MESSAGES_ROUTE = "messages_route"


fun NavController.navigateToMessages(navOptions: NavOptions) = navigate(MESSAGES_ROUTE, navOptions)
fun NavGraphBuilder.messagesScreen() {
    composable(route = MESSAGES_ROUTE) {
        MessagesRoute()
    }
}



