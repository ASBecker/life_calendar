package com.example.lifecalendar.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifecalendar.R
import com.example.lifecalendar.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    var birthdate by remember { mutableStateOf<LocalDate?>(null) }
    var lifespan by remember { mutableStateOf("80") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Birthdate Selection
        Button(
            onClick = {
                val today = LocalDate.now()
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        birthdate = LocalDate.of(year, month + 1, day)
                    },
                    today.year - 30, // Default to 30 years ago
                    today.monthValue - 1,
                    today.dayOfMonth
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = birthdate?.toString() ?: stringResource(R.string.select_birthdate)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lifespan Input
        OutlinedTextField(
            value = lifespan,
            onValueChange = { if (it.length <= 3) lifespan = it },
            label = { Text(stringResource(R.string.enter_lifespan)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Continue Button
        Button(
            onClick = {
                scope.launch {
                    birthdate?.let { date ->
                        viewModel.saveBirthdate(date)
                        viewModel.saveLifespan(lifespan.toIntOrNull() ?: 78)
                        viewModel.completeOnboarding()
                        onComplete()
                    }
                }
            },
            enabled = birthdate != null && lifespan.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.continue_button))
        }
    }
} 