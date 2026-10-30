package com.sunless.lynq.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sunless.lynq.ui.screens.DashboardScreen
import com.sunless.lynq.ui.screens.MessagesScreen
import com.sunless.lynq.ui.screens.EmergencyScreen
import com.sunless.lynq.ui.screens.MeshExplorerScreen
import com.sunless.lynq.ui.screens.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.Messages.route) { MessagesScreen(navController) }
        composable(Screen.Emergency.route) { EmergencyScreen(navController) }
        composable(Screen.MeshExplorer.route) { MeshExplorerScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
    }
}

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Messages : Screen("messages")
    object Emergency : Screen("emergency")
    object MeshExplorer : Screen("mesh_explorer")
    object Settings : Screen("settings")
    
    companion object {
        fun fromRoute(route: String): Screen {
            return when (route.substringBefore("/")) {
                Dashboard.route -> Dashboard
                Messages.route -> Messages
                Emergency.route -> Emergency
                MeshExplorer.route -> MeshExplorer
                Settings.route -> Settings
                else -> Dashboard
            }
        }
    }
}