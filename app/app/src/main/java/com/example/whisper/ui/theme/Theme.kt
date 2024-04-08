package com.example.whisper.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.whisper.model.Theme

// Creating the darkColour theme
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Creating the lightColour theme
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// Creating the warmColour theme
private val WarmColorScheme = lightColorScheme(
    primary = myWarmPrimary,
    secondary = myWarmSecondary,
    tertiary = myWarmTertiary
)

// Creating the coolColour theme
private val CoolColorScheme = lightColorScheme(
    primary = myCoolPrimary,
    secondary = myCoolSecondary,
    tertiary = myCoolTertiary
)

@Composable
fun WhisperTheme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    // Mapping the user-selected themes to a known app theme
    val colorScheme = when (theme) {
        Theme.DARK -> DarkColorScheme
        Theme.LIGHT -> LightColorScheme
        Theme.COOL -> CoolColorScheme
        Theme.WARM -> WarmColorScheme
        else -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
