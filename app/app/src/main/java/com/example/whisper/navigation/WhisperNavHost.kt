package com.example.whisper.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.whisper.ui.WhisperAppState

@Composable
fun WhisperNavHost(
    appState: WhisperAppState,
    modifier: Modifier = Modifier,
    startDestination: String = MESSAGES_ROUTE, // TODO: Change to Login
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        messagesScreen()
        announcementsScreen()
        settingsScreen()
    }
}
