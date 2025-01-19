package com.example.lifecalendar

import android.app.Application
import androidx.work.Configuration
import com.example.lifecalendar.data.SettingsRepository

class LifeCalendarApp : Application() {
    lateinit var settingsRepository: SettingsRepository
        private set

    override fun onCreate() {
        super.onCreate()
        // Initialize WorkManager for background tasks
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
        androidx.work.WorkManager.initialize(this, config)
        settingsRepository = SettingsRepository(this)
    }

    companion object {
        private var instance: LifeCalendarApp? = null

        fun getInstance(): LifeCalendarApp {
            return instance ?: throw IllegalStateException("Application not initialized")
        }
    }

    init {
        instance = this
    }
} 