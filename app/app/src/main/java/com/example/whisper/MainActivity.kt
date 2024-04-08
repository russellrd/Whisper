package com.example.whisper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.whisper.ui.Switcher
import com.example.whisper.ui.rememberWhisperAppState
import com.example.whisper.ui.theme.WhisperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appState = rememberWhisperAppState()
            WhisperTheme {
                CompositionLocalProvider() {
                    Switcher(appState)
                }
            }
        }
    }
}
