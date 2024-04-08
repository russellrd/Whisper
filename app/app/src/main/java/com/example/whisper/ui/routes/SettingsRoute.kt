package com.example.whisper.ui.routes

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.whisper.model.Language
import com.example.whisper.model.Settings
import com.example.whisper.model.Theme
import com.example.whisper.view_model.AuthStateViewModel
import com.example.whisper.view_model.SettingsViewModel

@Composable
fun SettingsRoute(
    modifier: Modifier = Modifier
) {
    SettingsScreen(modifier = modifier)
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel(),
    authViewModel: AuthStateViewModel = viewModel()
) {
    val settings: Settings by settingsViewModel.settings.collectAsStateWithLifecycle()
    var expandedLanguage by remember { mutableStateOf(false) }
    var expandedTheme by remember { mutableStateOf(false) }

    // Create a dropdown menu for Langauges
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                .fillMaxWidth(0.3f)
                .background(
                    color = MaterialTheme.colorScheme.primary
                )
            ) {
                val shape = RoundedCornerShape(30)
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .border(
                            shape = shape,
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        .clickable(
                            onClick = { expandedLanguage = true }
                        ),
                    shape = shape,
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .wrapContentSize(),
                    ) {
                        Text(
                            text = settings.language.displayName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }

                DropdownMenu(
                    expanded = expandedLanguage,
                    onDismissRequest = { expandedLanguage = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Language.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(it.displayName) },
                            onClick = {
                                settingsViewModel.save(Settings(language = it))
                                expandedLanguage = false
                                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(settings.language.code))
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Create a dropdown menu for Themes
            Box(modifier = Modifier
                .fillMaxWidth(0.3f)
                .background(color = MaterialTheme.colorScheme.primary)
            ) {
                val themeShape = RoundedCornerShape(30)
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .border(
                            shape = themeShape,
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        .clickable(onClick = { expandedTheme = true }),
                    shape = themeShape,
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.wrapContentSize(),
                    ) {
                        Text(
                            text = settings.theme.displayName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }

                DropdownMenu(
                    expanded = expandedTheme,
                    onDismissRequest = { expandedTheme = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Theme.entries.forEach { theme ->
                        DropdownMenuItem(
                            text = { Text(theme.displayName) },
                            onClick = {
                                settingsViewModel.save(settings.copy(theme = theme))
                                expandedTheme = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))


            Spacer(modifier = Modifier.height(30.dp))

            // Create a button for logging out
            Box(modifier = Modifier.padding(60.dp, 0.dp)) {
                Button(
                    onClick = { authViewModel.logout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = "Logout",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}
