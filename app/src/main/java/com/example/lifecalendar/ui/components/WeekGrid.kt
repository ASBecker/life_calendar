package com.example.lifecalendar.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun WeekGrid(
    weeksLived: Long,
    totalWeeks: Long,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val weeksPerRow = 52 // One year of weeks per row
    val rows = (totalWeeks / weeksPerRow) + 1
    
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Calculate available space considering padding
        val availableWidth = maxWidth - 48.dp // 24.dp padding on each side
        val availableHeight = maxHeight - 48.dp // 24.dp padding on each side
        
        // Calculate dot size and spacing
        val dotSize = with(density) {
            min(
                availableWidth.toPx() / (weeksPerRow + 1), // +1 for spacing
                availableHeight.toPx() / (rows.toFloat() + 1) // +1 for spacing
            )
        }
        
        // Convert back to dp
        val finalDotSize = with(density) { dotSize.toDp() * 0.8f } // 80% of available space for dots
        val spacing = with(density) { (dotSize * 0.2f).toDp() } // 20% of dot size for spacing
        
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(rows.toInt()) { rowIndex ->
                    // Add extra spacing before each 5-year mark (except the first row)
                    if (rowIndex > 0 && rowIndex % 5 == 0) {
                        Spacer(modifier = Modifier.height(spacing))
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        val weeksInThisRow = if (rowIndex == rows.toInt() - 1) {
                            (totalWeeks % weeksPerRow).toInt().takeIf { it > 0 } ?: weeksPerRow.toInt()
                        } else {
                            weeksPerRow.toInt()
                        }

                        repeat(weeksInThisRow) { columnIndex ->
                            val weekNumber = (rowIndex * weeksPerRow + columnIndex).toLong()
                            WeekDot(
                                isLived = weekNumber < weeksLived,
                                isCurrent = weekNumber == weeksLived - 1, // Current week is the last lived week
                                modifier = Modifier.size(finalDotSize)
                            )
                        }
                    }
                }
            }
        }
    }
} 