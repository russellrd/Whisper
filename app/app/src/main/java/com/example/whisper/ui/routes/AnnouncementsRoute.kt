package com.example.whisper.ui.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.whisper.ui.view.AnnouncementChannelList

@Composable
fun AnnouncementsRoute(
    modifier: Modifier = Modifier,
    navigateToAnnouncementPage: (String) -> Unit,
    navigateToManageSubscriptions: () -> Unit
) {
    AnnouncementsScreen(
        modifier = modifier,
        navigateToAnnouncementPage = navigateToAnnouncementPage,
        navigateToManageSubscriptions = navigateToManageSubscriptions
    )
}

@Composable
fun AnnouncementsScreen(
    modifier: Modifier = Modifier,
    navigateToAnnouncementPage: (String) -> Unit,
    navigateToManageSubscriptions: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        AnnouncementChannelList(
            modifier = modifier,
            navigateToAnnouncementPage = navigateToAnnouncementPage,
            navigateToManageSubscriptions = navigateToManageSubscriptions
        )
    }
}
