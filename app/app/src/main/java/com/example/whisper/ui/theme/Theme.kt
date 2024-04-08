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

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

private val WarmColorScheme = lightColorScheme(
    primary = myWarmPrimary,
    secondary = myWarmSecondary,
    tertiary = myWarmTertiary
)

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
    val colorScheme = when (theme) {
        Theme.DARK -> DarkColorScheme
        Theme.LIGHT -> LightColorScheme
        Theme.COOL -> CoolColorScheme
        Theme.WARM -> WarmColorScheme
        else -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    }

//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}