package com.tareq.barcodescanner.naviagtion

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tareq.barcodescanner.LocalNavigationProvider
import com.tareq.feature.archive.ArchiveScreen
import com.tareq.scanner.ScannerScreen

@Composable
fun QuickScanNavHost(modifier: Modifier = Modifier) {
    val navController = LocalNavigationProvider.current
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BottomBarScreen.Scanner.route,
        enterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(route = BottomBarScreen.Scanner.route) {
            ScannerScreen()
        }
        composable(route = BottomBarScreen.Archive.route) {
            ArchiveScreen()
        }
    }
}