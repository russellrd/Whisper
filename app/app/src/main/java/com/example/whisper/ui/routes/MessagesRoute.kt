package com.example.whisper.ui.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whisper.ui.view.MessageChannelList

@Composable
fun MessagesRoute(
    modifier: Modifier = Modifier
) {
    MessagesScreen(modifier = modifier)
}

@Composable
fun MessagesScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize().padding(10.dp)
    ) {
        MessageChannelList(modifier = modifier)
    }
}

