package com.example.lifecalendar.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.Color
import android.widget.GridLayout
import android.widget.RemoteViews
import com.example.lifecalendar.R
import com.example.lifecalendar.LifeCalendarApp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class LifeCalendarWidget4x1 : LifeCalendarWidget() {
    override fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val settingsRepository = LifeCalendarApp.getInstance().settingsRepository
        val birthDate = settingsRepository.birthDate
        val lifeExpectancy = settingsRepository.lifeExpectancy
        val isDarkTheme = settingsRepository.isDarkTheme ?: context.resources.configuration.isNightModeActive()

        val today = LocalDate.now()
        val weeksLived = ChronoUnit.WEEKS.between(birthDate, today)
        val totalWeeks = ChronoUnit.WEEKS.between(birthDate, birthDate.plusYears(lifeExpectancy.toLong()))

        val views = RemoteViews(context.packageName, R.layout.widget_layout_4x1)
        
        // Set semi-transparent background based on theme
        val backgroundColor = if (isDarkTheme) {
            Color.argb((255 * 0.7).toInt(), 0, 0, 0) // 70% opaque black
        } else {
            Color.argb((255 * 0.7).toInt(), 255, 255, 255) // 70% opaque white
        }
        views.setInt(R.id.widget_container, "setBackgroundColor", backgroundColor)

        // Configure grid layout
        views.removeAllViews(R.id.dot_grid)
        
        // Set grid dimensions for 4x1 widget
        val columns = 180 // More columns for denser layout
        val rows = 26 // Fewer rows for compact layout
        
        // Calculate position in the grid
        for (week in 0..totalWeeks) {
            val dot = RemoteViews(context.packageName, R.layout.widget_dot)
            
            // Set dot color based on state and theme
            val dotColor = when {
                week == weeksLived - 1 -> R.color.red // Current week
                week < weeksLived -> if (isDarkTheme) R.color.white else R.color.black // Past weeks
                else -> if (isDarkTheme) R.color.widget_white_10 else R.color.widget_black_10 // Future weeks
            }
            dot.setInt(R.id.dot_image, "setColorFilter", context.getColor(dotColor))

            // Calculate position in the grid (fill top to bottom, then left to right)
            val col = week / rows
            val row = week % rows
            
            // Only add the dot if it fits in our grid
            if (col < columns) {
                // Add dot to grid
                views.addView(R.id.dot_grid, dot)
            }
        }

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
} 