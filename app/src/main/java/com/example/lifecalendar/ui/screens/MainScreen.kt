package com.example.lifecalendar.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lifecalendar.ui.navigation.Screen
import com.example.lifecalendar.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val navController = rememberNavController()
    val onboardingCompleted by viewModel.onboardingCompleted.collectAsState(initial = false)
    
    NavHost(
        navController = navController,
        startDestination = if (onboardingCompleted) Screen.Home.route else Screen.Onboarding.route
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
} 