package com.example.lifecalendar.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.lifecalendar.data.UserPreferences
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)
    
    val onboardingCompleted = userPreferences.onboardingCompletedFlow
    val birthdate = userPreferences.birthdateFlow
    val lifespan = userPreferences.lifespanFlow

    suspend fun saveBirthdate(date: LocalDate) {
        userPreferences.saveBirthdate(date)
    }

    suspend fun saveLifespan(years: Int) {
        userPreferences.saveLifespan(years)
    }

    suspend fun completeOnboarding() {
        userPreferences.completeOnboarding()
    }

    fun calculateWeeks(birthdate: LocalDate, lifespan: Int): Pair<Long, Long> {
        val today = LocalDate.now()
        val endDate = birthdate.plusYears(lifespan.toLong())
        
        val weeksLived = ChronoUnit.WEEKS.between(birthdate, today)
        val totalWeeks = ChronoUnit.WEEKS.between(birthdate, endDate)
        
        return Pair(weeksLived, totalWeeks)
    }
} 