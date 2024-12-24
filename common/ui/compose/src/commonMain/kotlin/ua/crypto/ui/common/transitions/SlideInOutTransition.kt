package ua.crypto.ui.common.transitions

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import kotlin.math.roundToInt

object SlideInOutTransition {
    fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(durationMillis = DURATION, delayMillis = DELAY),
            initialOffset = { (it * 0.2f).roundToInt() },
        ) + fadeIn(animationSpec = tween(durationMillis = DURATION, delayMillis = DELAY))
    }

    fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(durationMillis = DURATION, delayMillis = DELAY),
            targetOffset = { (it * 0.2f).roundToInt() },
        )
    }

    fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(durationMillis = DURATION, delayMillis = DELAY),
            initialOffset = { (it * 0.2f).roundToInt() },
        )
    }

    fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(durationMillis = DURATION, delayMillis = DELAY),
            targetOffset = { (it * 0.2f).roundToInt() },
        ) + fadeOut(animationSpec = tween(durationMillis = DURATION, delayMillis = DELAY))
    }


    private const val DURATION = 220
    private const val DELAY = 80
}