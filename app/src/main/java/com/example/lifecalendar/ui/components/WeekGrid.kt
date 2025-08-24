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
    val rows = if (totalWeeks % weeksPerRow == 0L) {
        totalWeeks / weeksPerRow
    } else {
        (totalWeeks / weeksPerRow) + 1
    }
    val goldenRatio = 1.618f
    
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Calculate available space with golden ratio proportions
        // Use less horizontal padding and more vertical space
        val horizontalPadding = 32.dp
        val verticalPadding = (horizontalPadding.value / goldenRatio).dp
        
        val availableWidth = maxWidth - horizontalPadding
        val availableHeight = maxHeight - verticalPadding
        
        // Calculate dot size and spacing with golden ratio
        val dotSize = with(density) {
            min(
                availableWidth.toPx() / (weeksPerRow * 1.15f), // Tighter horizontal spacing
                availableHeight.toPx() / (rows.toFloat() * 1.08f) // More vertical breathing room
            )
        }
        
        // Convert back to dp with golden ratio adjustments
        val finalDotSize = with(density) { dotSize.toDp() * 0.85f } // Slightly larger dots
        val horizontalSpacing = with(density) { (dotSize * 0.15f).toDp() } // Tighter horizontal
        val verticalSpacing = with(density) { (dotSize * 0.15f * goldenRatio).toDp() } // More vertical spacing
        
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(horizontal = horizontalPadding / 2, vertical = verticalPadding / 2),
                verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(rows.toInt()) { rowIndex ->
                    // Add extra spacing before each 5-year mark (except the first row)
                    if (rowIndex > 0 && rowIndex % 5 == 0) {
                        Spacer(modifier = Modifier.height(verticalSpacing * goldenRatio))
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
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