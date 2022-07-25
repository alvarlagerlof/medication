package com.example.medication

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.work.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    @Inject lateinit var repository: DatabaseRepository

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repository.schedule.collect {
                for (scheduleItem in it) {
                    DailyWork.schedule(applicationContext, scheduleItem.uid, scheduleItem.time)
                }
            }
        }

//        val now = LocalTime.now()
//        val fiveAm = LocalTime.parse("05:00:00")
//        val timeDiff = Duration.ofHours(24).minus(Duration.between(fiveAm, now))
//        val request =
//            OneTimeWorkRequestBuilder<ReminderWorker>()
//                .setInitialDelay(timeDiff)
//                .build()
//
//        WorkManager
//            .getInstance(applicationContext)
//            .enqueueUniqueWork("melatonin", ExistingWorkPolicy.KEEP, request)

        // WindowCompat.setDecorFitsSystemWindows(window, false)

        // https://developer.android.com/training/scheduling/alarms
        // Check that app still has alarms permission
         val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        setContent {
           val navController = rememberAnimatedNavController()
//
//            if (!alarmManager.canScheduleExactAlarms()) {
//                Toast.makeText(LocalContext.current, "Can't schedule alarms", Toast.LENGTH_LONG)
//                    .show()
//            }

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