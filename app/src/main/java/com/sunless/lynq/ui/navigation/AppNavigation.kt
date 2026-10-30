package com.sunless.lynq.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.sunless.lynq.ui.screens.DashboardScreen
import com.sunless.lynq.ui.screens.EmergencyScreen
import com.sunless.lynq.ui.screens.MeshExplorerScreen
import com.sunless.lynq.ui.screens.MessagesScreen
import com.sunless.lynq.ui.screens.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        // Terminal feel: quick crossfade + a small vertical shift, no bouncy
        // material motion. Short enough to feel instant, smooth enough not
        // to flash-cut.
        enterTransition = {
            fadeIn(tween(200)) + slideInVertically(tween(240)) { it / 20 }
        },
        exitTransition = { fadeOut(tween(160)) },
        popEnterTransition = { fadeIn(tween(200)) },
        popExitTransition = {
            fadeOut(tween(160)) + slideOutVertically(tween(240)) { it / 20 }
        },
    ) {
        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.Messages.route) { MessagesScreen(navController) }
        composable(Screen.Emergency.route) { EmergencyScreen(navController) }
        composable(Screen.MeshExplorer.route) { MeshExplorerScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
    }
}

/**
 * Tab-style navigation: single instance per tab, state restored when
 * returning to a tab, and back from any tab exits the app instead of
 * rewinding through every screen you ever visited.
 */
fun NavHostController.navigateTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Messages : Screen("messages")
    object Emergency : Screen("emergency")
    object MeshExplorer : Screen("mesh_explorer")
    object Settings : Screen("settings")

    companion object {
        fun fromRoute(route: String): Screen = when (route.substringBefore("/")) {
            Messages.route -> Messages
            Emergency.route -> Emergency
            MeshExplorer.route -> MeshExplorer
            Settings.route -> Settings
            else -> Dashboard
        }
    }
}