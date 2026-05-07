package com.aipromptgenerater.aitricker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aipromptgenerater.aitricker.ui.screens.GenerateScreen
import com.aipromptgenerater.aitricker.ui.screens.HomeScreen
import com.aipromptgenerater.aitricker.ui.theme.AiPromptTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AiPromptTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                onNavigateToGenerate = { type -> navController.navigate("generate/$type") },
                                onNavigateToBilling = { /* Navigate to Billing */ }
                            )
                        }
                        composable("generate/{type}") { backStackEntry ->
                            val type = backStackEntry.arguments?.getString("type") ?: "App"
                            GenerateScreen(type = type, onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
