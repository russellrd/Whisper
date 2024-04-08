package com.example.whisper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.whisper.model.Theme
import com.example.whisper.ui.Switcher
import com.example.whisper.ui.rememberWhisperAppState
import com.example.whisper.ui.theme.WhisperTheme
import com.example.whisper.view_model.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            val settingsViewModel: SettingsViewModel by viewModels()
            val settings by settingsViewModel.settings.collectAsStateWithLifecycle()
            val appState = rememberWhisperAppState()
            // Applying the user-selected theme
            WhisperTheme(theme = settings.theme) {
                CompositionLocalProvider() {
                    Switcher(appState)
                }
            }
        }
    }
}

