package com.example.umafacts.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Mauve,
    onPrimary = Base,
    primaryContainer = Surface1,
    onPrimaryContainer = Text,

    secondary = Blue,
    onSecondary = Base,
    secondaryContainer = Surface1,
    onSecondaryContainer = Text,

    tertiary = Pink,
    onTertiary = Base,
    tertiaryContainer = Surface1,
    onTertiaryContainer = Text,

    background = Base,
    onBackground = Text,

    surface = Surface0,
    onSurface = Text,

    surfaceVariant = Surface1,
    onSurfaceVariant = Subtext1,

    outline = Surface2,

    error = Red,
    onError = Base,
    errorContainer = Surface1,
    onErrorContainer = Red,

    inverseSurface = Surface1,
    inverseOnSurface = Base,

    scrim = Crust
)

private val LightColorScheme = lightColorScheme(
    primary = MauveLight,
    onPrimary = BaseLight,
    primaryContainer = Surface1Light,
    onPrimaryContainer = TextLight,

    secondary = BlueLight,
    onSecondary = BaseLight,
    secondaryContainer = Surface1Light,
    onSecondaryContainer = TextLight,

    tertiary = PinkLight,
    onTertiary = BaseLight,
    tertiaryContainer = Surface1Light,
    onTertiaryContainer = TextLight,

    background = BaseLight,
    onBackground = TextLight,

    surface = Surface0Light,
    onSurface = TextLight,

    surfaceVariant = Surface1Light,
    onSurfaceVariant = Subtext1Light,

    outline = Surface2Light,

    error = RedLight,
    onError = BaseLight,
    errorContainer = Surface1Light,
    onErrorContainer = RedLight,

    inverseSurface = Surface1Light,
    inverseOnSurface = BaseLight,

    scrim = CrustLight
)

@Composable
fun UmaFactsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}