package com.example.lifecalendar.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

@Composable
fun WeekGrid(
    weeksLived: Long,
    totalWeeks: Long,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    
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
        val horizontalPadding = if (isLandscape) 20.dp else 32.dp
        val verticalPadding = if (isLandscape) 16.dp else (horizontalPadding.value / goldenRatio).dp
        
        val availableWidth = maxWidth - horizontalPadding
        val availableHeight = maxHeight - verticalPadding
        
        // Calculate dot size and spacing with golden ratio
        val dotSize = with(density) {
            if (isLandscape) {
                min(
                    availableWidth.toPx() / (weeksPerRow * 1.08f), // Less spacing factor for bigger dots in landscape
                    availableHeight.toPx() / (rows.toFloat() * 1.05f) // Less spacing for bigger dots in landscape
                )
            } else {
                min(
                    availableWidth.toPx() / (weeksPerRow * 1.15f), // Original tighter horizontal spacing
                    availableHeight.toPx() / (rows.toFloat() * 1.08f) // Original more vertical breathing room
                )
            }
        }
        
        // Convert back to dp with golden ratio adjustments
        val finalDotSize = with(density) { 
            if (isLandscape) {
                dotSize.toDp() * 1.1f // 30% larger dots in landscape
            } else {
                dotSize.toDp() * 0.85f // Original size in portrait
            }
        }
        val horizontalSpacing = with(density) { 
            if (isLandscape) {
                (dotSize * 0.12f).toDp() // Slightly tighter in landscape
            } else {
                (dotSize * 0.15f).toDp() // Original tighter horizontal
            }
        }
        val verticalSpacing = with(density) { 
            if (isLandscape) {
                (dotSize * 0.12f * goldenRatio).toDp() // Proportional vertical spacing
            } else {
                (dotSize * 0.15f * goldenRatio).toDp() // Original more vertical spacing
            }
        }
        
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLandscape) {
                // Landscape mode: distribute years in multiple columns
                val totalYears = ceil(totalWeeks.toDouble() / 52).toInt()
                val yearGap = verticalSpacing * 2 // Gap between years
                
                // Calculate how many year columns we can fit
                val singleYearWidth = with(density) {
                    (52 * (finalDotSize.toPx() + horizontalSpacing.toPx())).toDp()
                }
                val columnsCount = with(density) {
                    val availableForColumns = availableWidth.toPx()
                    val columnWidthWithGap = singleYearWidth.toPx() + (yearGap * 2).toPx()
                    floor(availableForColumns / columnWidthWithGap).toInt().coerceAtLeast(1).coerceAtMost(3)
                }
                
                val yearsPerColumn = ceil(totalYears.toDouble() / columnsCount).toInt()
                
                Row(
                    modifier = Modifier.padding(horizontal = horizontalPadding / 2, vertical = verticalPadding / 2),
                    horizontalArrangement = Arrangement.spacedBy(yearGap * 2),
                    verticalAlignment = Alignment.Top
                ) {
                    repeat(columnsCount) { columnIndex ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(yearGap),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val startYear = columnIndex * yearsPerColumn
                            val endYear = min(startYear + yearsPerColumn, totalYears)
                            
                            for (yearIndex in startYear until endYear) {
                                // Add extra spacing before each 5-year mark (except the first year in column)
                                if (yearIndex > startYear && yearIndex % 5 == 0) {
                                    Spacer(modifier = Modifier.height(yearGap))
                                }
                                
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    val yearStartWeek = yearIndex * 52L
                                    val yearEndWeek = min(yearStartWeek + 52, totalWeeks)
                                    val weeksInYear = (yearEndWeek - yearStartWeek).toInt()
                                    
                                    val rowsInYear = ceil(weeksInYear.toDouble() / 52).toInt()
                                    
                                    repeat(rowsInYear) { rowInYear ->
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val rowStartWeek = rowInYear * 52
                                            val rowEndWeek = min(rowStartWeek + 52, weeksInYear)
                                            
                                            repeat(rowEndWeek - rowStartWeek) { weekInRow ->
                                                val globalWeekNumber = yearStartWeek + rowStartWeek + weekInRow
                                                
                                                WeekDot(
                                                    isLived = globalWeekNumber < weeksLived,
                                                    isCurrent = globalWeekNumber == weeksLived - 1,
                                                    modifier = Modifier.size(finalDotSize)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Portrait mode: keep original behavior exactly
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
} 