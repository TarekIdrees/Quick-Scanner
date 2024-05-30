package com.tareq.barcodescanner.naviagtion

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tareq.barcodescanner.LocalNavigationProvider
import com.tareq.design_system.ui.Brand
import com.tareq.design_system.ui.white30
import com.tareq.design_system.ui.white87

@Composable
fun BottomBar() {
    val navController = LocalNavigationProvider.current

    val screens = listOf(
        BottomBarScreen.Scanner,
        BottomBarScreen.Archive,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = white30,
    ) {
        screens.forEach { screen ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = screen.screenIcon),
                        contentDescription = "Navigation icon"
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Brand,
                    indicatorColor = white30,
                    unselectedIconColor = white87
                )
            )
        }
    }
}