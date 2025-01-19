package com.example.lifecalendar.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun LifeIcon(
    progress: Float, // 0.0 to 1.0
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.White)
    ) {
        Canvas(
            modifier = Modifier.size(108.dp) // Standard launcher icon size
        ) {
            val dotSize = size.width / 8 // Leaves room for spacing
            val spacing = size.width / 4
            
            // Calculate how many dots should be filled
            val filledDots = (progress * 9).roundToInt().coerceIn(0, 9)
            
            // Draw 3x3 grid of dots
            for (row in 0..2) {
                for (col in 0..2) {
                    val dotIndex = row * 3 + col
                    val dotColor = when {
                        dotIndex < filledDots -> Color.Black
                        else -> Color.LightGray
                    }
                    
                    drawCircle(
                        color = dotColor,
                        radius = dotSize / 2,
                        center = Offset(
                            x = spacing * (col + 1),
                            y = spacing * (row + 1)
                        )
                    )
                }
            }
        }
    }
} 