package com.example.lifecalendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.lifecalendar.LifeCalendarApp

private val DarkColorPalette = darkColors(
    primary = Color.White,
    background = Color.Black,
    surface = Color.Black
)

private val LightColorPalette = lightColors(
    primary = Color.Black,
    background = Color.White,
    surface = Color.White
)

object ThemeState {
    private val repository = LifeCalendarApp.getInstance().settingsRepository
    private val mutableState = mutableStateOf<Boolean?>(repository.isDarkTheme)
    
    var isDarkTheme: Boolean?
        get() = mutableState.value
        set(value) {
            mutableState.value = value
            if (value != null) {
                repository.isDarkTheme = value
            } else {
                repository.isDarkTheme = null
            }
        }
}

@Composable
fun LifeCalendarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val isDark = ThemeState.isDarkTheme
    val colors = if (isDark ?: darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
} 