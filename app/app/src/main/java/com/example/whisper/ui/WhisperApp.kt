// Based off "nowinandroid" project made by The Android Open Source Project

package com.example.whisper.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.whisper.R
import com.example.whisper.model.Auth
import com.example.whisper.navigation.TabDestination
import com.example.whisper.navigation.WhisperNavHost

@Composable
fun WhisperApp(appState: WhisperAppState) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            WhisperTopBar(
                currentDestination = appState.currentTabDestination,
                canNavBack = appState.navController.previousBackStackEntry != null,
                navUp = { appState.navController.navigateUp() }
            )
        },
        bottomBar = {
            BottomNavigation(
                destinations = appState.tabDestinations,
                onNavigateToDestination = appState::navigateToTabDestination,
                currentDestination = appState.currentDestination,
            )
        }
    ) { padding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(Modifier.fillMaxSize()) {
                WhisperNavHost(appState = appState)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WhisperTopBar(
    currentDestination: TabDestination?,
    canNavBack: Boolean,
    navUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { if(currentDestination != null) Text(stringResource(currentDestination.destinationNameId)) else Text("None") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if(canNavBack) {
                IconButton(onClick = navUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_btn)
                    )
                }
            }
        }
    )
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
    val selected = currentDestination.isTabDestination(destination)
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

private fun NavDestination?.isTabDestination(destination: TabDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
