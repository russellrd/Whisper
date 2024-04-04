package com.example.whisper.ui.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DirectMessagesRoute(
    userId: String,
    modifier: Modifier = Modifier
) {
    DirectMessagesScreen(
        userId = userId,
        modifier = modifier
    )
}

@Composable
fun DirectMessagesScreen(
    userId: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text("Hello $userId")
    }
}
