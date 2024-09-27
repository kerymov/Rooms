package com.example.rooms.presentation.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = Light,
    primaryContainer = Blue40,
    secondary = Yellow,
    onSecondary = Dark,
    secondaryContainer = Light60,
    background = Light,
    onBackground = Dark,
    surfaceTint = BlueLight,
    surface = LightGray,
    onSurface = Dark,
    error = RedError,
    onError = Light,
    errorContainer = RedError,
    onErrorContainer = Light,
    outline = Dark12,
    outlineVariant = Dark60,
    scrim = Dark80
)

@Composable
fun RoomsTheme(
    content: @Composable () -> Unit
) {
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            // Change to customize Status Bar and Insets Controller
//            window.statusBarColor = LightColorScheme.background.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
//            window.navigationBarColor = LightColorScheme.background.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
//        }
//    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun SetSystemBarIconColors(
    isAppearanceLightStatusBars: Boolean = true,
    isAppearanceLightNavigationBars: Boolean = true
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isAppearanceLightStatusBars
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = isAppearanceLightNavigationBars
        }
    }
}

@Composable
fun ChangeSystemBarsColors(
    systemBarColor: Color = LightColorScheme.background,
    navigationBarColor: Color = LightColorScheme.background,
    isAppearanceLightStatusBars: Boolean = true,
    isAppearanceLightNavigationBars: Boolean = true
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = systemBarColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isAppearanceLightStatusBars
            window.navigationBarColor = navigationBarColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = isAppearanceLightNavigationBars
        }
    }
}

@Composable
fun HideSystemBars() {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val insetsController = WindowCompat.getInsetsController(window, view)
    if (!view.isInEditMode) {
        insetsController.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

@Composable
fun ShowSystemBars() {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val insetsController = WindowCompat.getInsetsController(window, view)
    if (!view.isInEditMode) {
        insetsController.apply { show(WindowInsetsCompat.Type.systemBars()) }
    }
}

//@Composable
//fun RoomsTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}