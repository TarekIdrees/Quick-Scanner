package com.tareq.design_system.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable

@Composable
fun ContentVisibilityAnimation(
    state: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = state,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 300)
        ) + slideInVertically(),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 300)
        ) + slideOutVertically()
    ) {
        content()
    }
}