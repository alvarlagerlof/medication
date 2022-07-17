package com.example.medication

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WindowCompat.setDecorFitsSystemWindows(window, false)

        // https://developer.android.com/training/scheduling/alarms
        // Check that app still has alarms permission
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        setContent {
            val navController = rememberAnimatedNavController()

            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(LocalContext.current, "Can't schedule alarms", Toast.LENGTH_LONG)
                    .show()
            }

            AnimatedNavHost(navController = navController, startDestination = "start") {
                composable("start") { Start(navController, alarmManager = alarmManager) }
                composable(
                    "detail?uid={uid}&edit={edit}",
                    enterTransition = {
                        scaleIn(
                            transformOrigin = TransformOrigin.Center,
                            initialScale = 0.9f,
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) + fadeIn(
                            animationSpec = tween(200, easing = FastOutSlowInEasing)
                        )
                    },
                    exitTransition = {
                        scaleOut(
                            transformOrigin = TransformOrigin.Center,
                            targetScale = 0.9f,
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) + fadeOut(
                            animationSpec = tween(200, easing = FastOutSlowInEasing)
                        )
                    },
//                    popEnterTransition = {
//                        slideIntoContainer(
//                            AnimatedContentScope.SlideDirection.Right,
//                            animationSpec = tween(700)
//                        )
//                    },
//                    popExitTransition = {
//                        slideOutOfContainer(
//                            AnimatedContentScope.SlideDirection.Right,
//                            animationSpec = tween(700)
//                        )
//                    },
                    arguments = listOf(
                        navArgument("uid") { type = NavType.IntType },
                        navArgument("edit") { type = NavType.BoolType })
                ) {
                    Detail(navController)
                }
            }
        }
    }
}