package com.example.lifecalendar.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val circleColor = MaterialTheme.colors.primary
    
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Canvas(
            modifier = Modifier.size(16.dp)
        ) {
            drawCircle(
                color = circleColor,
                style = Stroke(width = size.width * 0.15f)
            )
        }
    }
} 