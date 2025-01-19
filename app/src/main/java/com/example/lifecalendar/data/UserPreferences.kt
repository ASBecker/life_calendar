package com.example.lifecalendar.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences(private val context: Context) {
    companion object {
        private val BIRTHDATE = longPreferencesKey("birthdate")
        private val LIFESPAN = intPreferencesKey("lifespan")
        private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val birthdateFlow: Flow<LocalDate?> = context.dataStore.data.map { preferences ->
        preferences[BIRTHDATE]?.let { LocalDate.ofEpochDay(it) }
    }

    val lifespanFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[LIFESPAN] ?: 80 // Default lifespan of 80 years
    }

    val onboardingCompletedFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED] ?: false
    }

    suspend fun saveBirthdate(date: LocalDate) {
        context.dataStore.edit { preferences ->
            preferences[BIRTHDATE] = date.toEpochDay()
        }
    }

    suspend fun saveLifespan(years: Int) {
        context.dataStore.edit { preferences ->
            preferences[LIFESPAN] = years
        }
    }

    suspend fun completeOnboarding() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = true
        }
    }
} 