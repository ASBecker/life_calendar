package com.example.lifecalendar.data

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "life_calendar_settings",
        Context.MODE_PRIVATE
    )

    var birthDate: LocalDate
        get() = prefs.getString(KEY_BIRTH_DATE, null)?.let { dateStr ->
            try {
                LocalDate.parse(dateStr)
            } catch (e: Exception) {
                DEFAULT_BIRTH_DATE
            }
        } ?: DEFAULT_BIRTH_DATE
        set(value) {
            prefs.edit().putString(KEY_BIRTH_DATE, value.toString()).apply()
        }

    var lifeExpectancy: Int
        get() = prefs.getInt(KEY_LIFE_EXPECTANCY, DEFAULT_LIFE_EXPECTANCY)
        set(value) {
            if (value in 1..150) {
                prefs.edit().putInt(KEY_LIFE_EXPECTANCY, value).apply()
            }
        }

    var isDarkTheme: Boolean?
        get() = if (prefs.contains(KEY_DARK_THEME)) {
            prefs.getBoolean(KEY_DARK_THEME, false)
        } else null
        set(value) {
            if (value == null) {
                prefs.edit().remove(KEY_DARK_THEME).apply()
            } else {
                prefs.edit().putBoolean(KEY_DARK_THEME, value).apply()
            }
        }

    companion object {
        private const val KEY_BIRTH_DATE = "birth_date"
        private const val KEY_LIFE_EXPECTANCY = "life_expectancy"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val DEFAULT_LIFE_EXPECTANCY = 90
        private val DEFAULT_BIRTH_DATE = LocalDate.of(1990, 1, 1)
    }
} 