package com.example.medication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.TransformOrigin
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberAnimatedNavController()

            AnimatedNavHost(navController = navController, startDestination = "start") {
                composable("start") { Start(navController) }
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
                ) { backStackEntry ->
                    Detail(navController)
                }
            }
        }
    }
}