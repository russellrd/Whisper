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
    val isLoggedIn: Boolean by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    if (isLoggedIn)
        WhisperApp(appState)
    else
        Login(login = viewModel::login)
}