package com.example.whisper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.whisper.ui.Switcher
import com.example.whisper.ui.rememberWhisperAppState
import com.example.whisper.ui.theme.WhisperTheme
import com.example.whisper.view_model.AuthState
import com.example.whisper.view_model.AuthStateViewModel

class MainActivity : ComponentActivity() {
    private val authState by viewModels<AuthStateViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appState = rememberWhisperAppState()
            WhisperTheme {
                CompositionLocalProvider(AuthState provides authState) {
                    Switcher(appState)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WhisperTheme {
        Greeting("Android")
    }
}