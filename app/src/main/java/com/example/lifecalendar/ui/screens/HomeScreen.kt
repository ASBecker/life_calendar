package com.example.lifecalendar.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.lifecalendar.LifeCalendarApp
import com.example.lifecalendar.ui.components.SettingsButton
import com.example.lifecalendar.ui.components.ThemeToggle
import com.example.lifecalendar.ui.components.WeekGrid
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private val GRID_PADDING = 24.dp
private val GOLDEN_RATIO = 1.618f
private val CONTROLS_SPACING = (GRID_PADDING.value / GOLDEN_RATIO).dp

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val repository = remember { LifeCalendarApp.getInstance().settingsRepository }
    
    var showSettings by remember { mutableStateOf(false) }
    var birthDate by remember { mutableStateOf(repository.birthDate) }
    var lifeExpectancy by remember { mutableStateOf(repository.lifeExpectancy) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Effect to persist changes
    LaunchedEffect(birthDate) {
        repository.birthDate = birthDate
    }
    LaunchedEffect(lifeExpectancy) {
        repository.lifeExpectancy = lifeExpectancy
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top right controls
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = GRID_PADDING, end = GRID_PADDING),
            horizontalArrangement = Arrangement.spacedBy(CONTROLS_SPACING)
        ) {
            SettingsButton(
                onClick = { showSettings = true }
            )
            ThemeToggle()
        }

        // Week grid centered
        val today = LocalDate.now()
        val weeksLived = ChronoUnit.WEEKS.between(birthDate, today)
        val totalWeeks = ChronoUnit.WEEKS.between(birthDate, birthDate.plusYears(lifeExpectancy.toLong()))

        WeekGrid(
            weeksLived = weeksLived,
            totalWeeks = totalWeeks,
            modifier = Modifier.fillMaxSize()
        )

        // Settings dialog
        if (showSettings) {
            AlertDialog(
                onDismissRequest = { showSettings = false },
                title = { Text("Settings") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Birth date row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = birthDate.toString(),
                                onValueChange = { dateStr ->
                                    try {
                                        birthDate = LocalDate.parse(dateStr)
                                    } catch (e: Exception) {
                                        // Invalid date format, ignore
                                    }
                                },
                                label = { Text("Birth Date") },
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { showDatePicker = true }) {
                                Text("Pick")
                            }
                        }
                        
                        OutlinedTextField(
                            value = lifeExpectancy.toString(),
                            onValueChange = { str ->
                                str.toIntOrNull()?.let { age ->
                                    if (age in 1..150) {
                                        lifeExpectancy = age
                                    }
                                }
                            },
                            label = { Text("Life Expectancy (years)") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showSettings = false }) {
                        Text("Done")
                    }
                }
            )
        }

        // Date picker
        if (showDatePicker) {
            val date = birthDate
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    birthDate = LocalDate.of(year, month + 1, dayOfMonth)
                    showDatePicker = false
                },
                date.year,
                date.monthValue - 1,
                date.dayOfMonth
            ).show()
            showDatePicker = false
        }
    }
} 