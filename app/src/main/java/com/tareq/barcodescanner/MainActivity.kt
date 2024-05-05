package com.tareq.barcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tareq.design_system.ui.BarcodeScannerTheme
import com.tareq.design_system.ui.background
import com.tareq.scanner.ScannerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            BarcodeScannerTheme {
                Scaffold(
                    modifier = Modifier.drawBehind { drawRect(background) },
                    bottomBar = {}
                ) { paddingValues ->
                    ScannerScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}
