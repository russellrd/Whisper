package com.example.whisper.ui

import androidx.compose.runtime.Composable
import com.example.whisper.ui.view.Login
import com.example.whisper.view_model.AuthState

@Composable
fun Switcher(
    appState: WhisperAppState
) {
    val authState = AuthState.current
    if (authState.isLoggedIn)
        WhisperApp(appState)
    else
        Login()
}