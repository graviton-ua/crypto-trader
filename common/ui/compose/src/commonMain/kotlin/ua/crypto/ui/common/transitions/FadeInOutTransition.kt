package ua.crypto.ui.common.transitions

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

object FadeInOutTransition {
    fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return fadeIn(animationSpec = tween(durationMillis = DURATION, delayMillis = DELAY))
    }

    fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return fadeOut(animationSpec = tween(durationMillis = DURATION, delayMillis = DELAY))
    }

    fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return enterTransition()
    }

    fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return exitTransition()
    }

    private const val DURATION = 220
    private const val DELAY = 80
}