package com.example.lifecalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

@Composable
fun WeekDot(
    isLived: Boolean,
    isCurrent: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                when {
                    isCurrent -> Color.Red
                    isLived -> MaterialTheme.colors.primary
                    else -> MaterialTheme.colors.primary.copy(alpha = 0.15f)
                }
            )
    )
} 