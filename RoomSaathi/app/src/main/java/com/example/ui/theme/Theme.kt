package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = GeometricOrange,
    onPrimary = Color.White,
    primaryContainer = PrimaryOrangeDark,
    secondary = Color(0xFFFFB74D),
    onSecondary = GeometricDark,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurface = Color.White,
    error = GeometricRed
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GeometricOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0B2),
    secondary = GeometricDark,
    onSecondary = Color.White,
    background = GeometricWhite,
    surface = GeometricWhite,
    surfaceVariant = GeometricGrayContainer,
    onSurface = GeometricDark,
    outline = GeometricBorder,
    error = GeometricRed
  )

@Composable
fun RoomSaathiTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    shapes = GeometricShapes,
    content = content
  )
}
