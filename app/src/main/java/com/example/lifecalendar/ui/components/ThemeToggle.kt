package com.example.lifecalendar.ui.components

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.lifecalendar.ui.theme.ThemeState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemeToggle(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentTheme = ThemeState.isDarkTheme
    val rectangleColor = MaterialTheme.colors.primary
    
    Box(
        modifier = modifier
            .combinedClickable(
                onClick = {
                    ThemeState.isDarkTheme = !(currentTheme ?: false)
                },
                onLongClick = {
                    ThemeState.isDarkTheme = null
                    Toast.makeText(context, "Theme reset to system default", Toast.LENGTH_SHORT).show()
                }
            )
            .padding(16.dp)
    ) {
        Canvas(
            modifier = Modifier.size(16.dp)
        ) {
            drawRoundRect(
                color = rectangleColor,
                size = Size(size.width, size.height),
                cornerRadius = CornerRadius(4f, 4f),
                style = Stroke(width = size.width * 0.15f)
            )
        }
    }
} 