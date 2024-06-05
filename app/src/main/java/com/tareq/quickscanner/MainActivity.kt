package com.tareq.quickscanner

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.tareq.quickscanner.naviagtion.BottomBar
import com.tareq.quickscanner.naviagtion.QuickScanNavHost
import com.tareq.design_system.ui.BarcodeScannerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            CompositionLocalProvider(LocalNavigationProvider provides rememberNavController()) {
                BarcodeScannerTheme {
                    Scaffold(
                        modifier = Modifier.safeDrawingPadding(),
                        containerColor = MaterialTheme.colorScheme.background,
                        bottomBar = {
                            BottomBar()
                        }
                    ) {
                        QuickScanNavHost()
                    }
                }
            }
        }
    }
}
