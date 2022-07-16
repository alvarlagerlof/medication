package com.example.medication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "start") {
                composable("start") { Start(navController) }
                composable(
                    "detail?uid={uid}",
                    arguments = listOf(navArgument("uid") {
                        nullable = true
                    })
                ) { backStackEntry ->
                    Detail(navController)
                }
            }
        }
    }
}