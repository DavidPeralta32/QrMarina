package com.unindetec.qrmarina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unindetec.qrmarina.ui.components.PermisosCamara
import com.unindetec.qrmarina.ui.screens.HomeScreen
import com.unindetec.qrmarina.ui.screens.ScannerScreen

sealed class Screen(val route: String) {
    object Permission : Screen("permission")
    object Home : Screen("home")
    object Scanner : Screen("scanner")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Permission.route) {
            PermisosCamara(navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Scanner.route) {
            ScannerScreen(navController)
        }
    }
}