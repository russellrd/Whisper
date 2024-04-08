package com.example.whisper.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.whisper.ui.view.Login
import com.example.whisper.view_model.AuthStateViewModel

@Composable
fun Switcher(
    appState: WhisperAppState,
    viewModel: AuthStateViewModel = viewModel()
) {
    // Check if user is logged in
    val isLoggedIn: Boolean by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    // Go to main app if logged in
    // Otherwise, go to login page
    if (isLoggedIn)
        WhisperApp(appState)
    else
        Login(login = viewModel::login)
}