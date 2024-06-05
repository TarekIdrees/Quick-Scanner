package com.tareq.quickscanner.naviagtion

import androidx.annotation.DrawableRes
import com.tareq.core.design.system.R
sealed class BottomBarScreen(
    val route: String,
    @DrawableRes val screenIcon: Int,
){
    data object Scanner : BottomBarScreen(
        route = "Scanner",
        screenIcon = R.drawable.ic_scanner_placeholder
    )

    data object Archive: BottomBarScreen(
        route = "Archive",
        screenIcon = R.drawable.ic_bookmark
    )
}