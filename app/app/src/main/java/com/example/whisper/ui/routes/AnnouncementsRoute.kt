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
            //in the announcements channel page, we can navigate to two other pages
            //the first is when you click on a channel, it will show the logs of announcements
            //the second is to open the manage subscriptions page which shows all the announcement channels
            //and allows users to subscribe and unsubscribe to them
            navigateToAnnouncementPage = navigateToAnnouncementPage,
            navigateToManageSubscriptions = navigateToManageSubscriptions
        )
    }
}
