package com.example.whisper.ui.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whisper.ui.view.AllAnnouncementChannelList

@Composable
fun ManageSubscriptionsRoute(
    modifier: Modifier = Modifier,
    navigateToAnnouncementPage: (String) -> Unit,
) {
    ManageSubscriptionsScreen(
        modifier = modifier,
        navigateToAnnouncementPage = navigateToAnnouncementPage,
    )
}

@Composable
fun ManageSubscriptionsScreen(
    modifier: Modifier = Modifier,
    navigateToAnnouncementPage: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        AllAnnouncementChannelList(
            modifier = modifier,
            navigateToAnnouncementPage = navigateToAnnouncementPage,
        )
    }
}
