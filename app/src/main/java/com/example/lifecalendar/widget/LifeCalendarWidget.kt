package com.example.lifecalendar.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.GridLayout
import android.widget.RemoteViews
import com.example.lifecalendar.R
import com.example.lifecalendar.LifeCalendarApp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

open class LifeCalendarWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        // Handle all update scenarios
        when (intent.action) {
            Intent.ACTION_CONFIGURATION_CHANGED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_DATE_CHANGED,
            "android.intent.action.THEME_CHANGED",
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val thisWidget = ComponentName(context, LifeCalendarWidget::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    protected open fun updateAppWidget(
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

        // Get widget dimensions
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
        val width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

        // Calculate grid dimensions
        val weeksPerRow = 52 // One year
        val rows = (totalWeeks / weeksPerRow) + 1

        val views = RemoteViews(context.packageName, R.layout.widget_layout)
        
        // Set semi-transparent background based on theme
        val backgroundColor = if (isDarkTheme) {
            Color.argb((255 * 0.7).toInt(), 0, 0, 0) // 70% opaque black
        } else {
            Color.argb((255 * 0.7).toInt(), 255, 255, 255) // 70% opaque white
        }
        views.setInt(R.id.widget_container, "setBackgroundColor", backgroundColor)

        // Configure grid layout
        views.removeAllViews(R.id.dot_grid)
        
        // Add dots
        var currentRow = 0
        var currentCol = 0
        
        for (week in 0..totalWeeks) {
            val dot = RemoteViews(context.packageName, R.layout.widget_dot)
            
            // Set dot color based on state and theme
            val dotColor = when {
                week == weeksLived - 1 -> R.color.red // Current week
                week < weeksLived -> if (isDarkTheme) R.color.white else R.color.black // Past weeks
                else -> if (isDarkTheme) R.color.white_15 else R.color.black_15 // Future weeks with lower opacity
            }
            dot.setInt(R.id.dot_image, "setColorFilter", context.getColor(dotColor))

            // Create layout params for the dot
            val params = GridLayout.LayoutParams()
            params.rowSpec = GridLayout.spec(currentRow, 1f)
            params.columnSpec = GridLayout.spec(currentCol, 1f)
            
            // Add dot to grid
            views.addView(R.id.dot_grid, dot)

            // Update position
            currentCol++
            if (currentCol >= weeksPerRow) {
                currentCol = 0
                currentRow++
            }
        }

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun android.content.res.Configuration.isNightModeActive(): Boolean {
        return uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == 
               android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        private const val weeksPerRow = 52 // One year
    }
} 