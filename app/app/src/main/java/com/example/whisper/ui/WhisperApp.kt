// Based off "nowinandroid" project made by The Android Open Source Project

package com.example.whisper.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.whisper.navigation.TabDestination
import com.example.whisper.navigation.WhisperNavHost

@Composable
fun WhisperApp(appState: WhisperAppState) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            BottomNavigation(
                destinations = appState.topLevelDestinations,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentDestination,
            )
        }
    ) { padding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val destination = appState.currentTopLevelDestination
            if (destination != null) {
                // TODO: Add top bar
            }
            Column(Modifier.fillMaxSize()) {
                WhisperNavHost(appState = appState)
            }
        }

    }
}

@Composable
private fun BottomNavigation(
    destinations: List<TabDestination>,
    onNavigateToDestination: (TabDestination) -> Unit,
    currentDestination: NavDestination?
) {
    NavigationBar {
        destinations.forEach { destination ->
            AddItem(
                destination = destination,
                onNavigateToDestination = onNavigateToDestination,
                currentDestination = currentDestination
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    destination: TabDestination,
    onNavigateToDestination: (TabDestination) -> Unit,
    currentDestination: NavDestination?
) {
    val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
    NavigationBarItem(
        selected = true,
        onClick = { onNavigateToDestination(destination) },
        label = {
            Text(stringResource(destination.destinationNameId))
        },
        icon = {
            Icon(
                imageVector = if(selected) destination.selectedIcon else destination.unselectedIcon,
                contentDescription = stringResource(destination.destinationNameId)
            )
        }
    )
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TabDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
