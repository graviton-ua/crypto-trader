package ua.crypto.ui.common.transitions

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

object ScaleInOutTransition {
    fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return scaleIntoContainer()
    }

    fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
    }

    fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
    }

    fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return scaleOutOfContainer()
    }


    private fun scaleIntoContainer(
        direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
        initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f
    ): EnterTransition {
        return scaleIn(
            animationSpec = tween(220, delayMillis = 90),
            initialScale = initialScale
        ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
    }

    private fun scaleOutOfContainer(
        direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
        targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
    ): ExitTransition {
        return scaleOut(
            animationSpec = tween(
                durationMillis = 220,
                delayMillis = 90
            ), targetScale = targetScale
        ) + fadeOut(tween(delayMillis = 90))
    }

    private enum class ScaleTransitionDirection { INWARDS, OUTWARDS }
}