// Based off "nowinandroid" project made by The Android Open Source Project

package com.example.whisper.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.whisper.navigation.ANNOUNCEMENTS_ROUTE
import com.example.whisper.navigation.MESSAGES_ROUTE
import com.example.whisper.navigation.SETTINGS_ROUTE
import com.example.whisper.navigation.TabDestination
import com.example.whisper.navigation.navigateToAnnouncements
import com.example.whisper.navigation.navigateToMessages
import com.example.whisper.navigation.navigateToSettings

@Composable
fun rememberWhisperAppState(
    navController: NavHostController = rememberNavController()
): WhisperAppState {
    return remember(
        navController
    ) {
        WhisperAppState(
            navController = navController
        )
    }
}

@Stable
class WhisperAppState(
    val navController: NavHostController,
) {
    // Store current nav destination
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    // Store current tab destination
    val currentTabDestination: TabDestination?
        // Map route strings to tab destinations
        @Composable get() = when (currentDestination?.route) {
            MESSAGES_ROUTE -> TabDestination.MESSAGES
            ANNOUNCEMENTS_ROUTE -> TabDestination.ANNOUNCEMENTS
            SETTINGS_ROUTE -> TabDestination.SETTINGS
            else -> null
        }

    // Store list of possible tab destinations
    val tabDestinations: List<TabDestination> = TabDestination.entries

    // Navigate to a desired tab destination
    fun navigateToTabDestination(topLevelDestination: TabDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        // Map tab destination to nav controller navigation
        when (topLevelDestination) {
            TabDestination.MESSAGES -> navController.navigateToMessages(
                topLevelNavOptions
            )

            TabDestination.ANNOUNCEMENTS -> navController.navigateToAnnouncements(
                topLevelNavOptions
            )

            TabDestination.SETTINGS -> navController.navigateToSettings(
                topLevelNavOptions
            )
        }
    }
}
