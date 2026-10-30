package com.sunless.lynq.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunless.lynq.ui.LynqViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: LynqViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Screen.Dashboard.route,
        // Standard horizontal slide + fade, the way other apps switch screens.
        enterTransition = {
            slideInHorizontally(tween(240)) { it / 4 } + fadeIn(tween(200))
        },
        exitTransition = {
            slideOutHorizontally(tween(240)) { -it / 4 } + fadeOut(tween(180))
        },
        popEnterTransition = {
            slideInHorizontally(tween(240)) { -it / 4 } + fadeIn(tween(200))
        },
        popExitTransition = {
            slideOutHorizontally(tween(240)) { it / 4 } + fadeOut(tween(180))
        },
    ) {
        composable(Screen.Dashboard.route) { DashboardScreen(navController, viewModel) }
        composable(Screen.Messages.route) { MessagesScreen(navController, viewModel) }
        composable(Screen.Emergency.route) { EmergencyScreen(navController, viewModel) }
        composable(Screen.MeshExplorer.route) { MeshExplorerScreen(navController, viewModel) }
        composable(Screen.Settings.route) { SettingsScreen(navController, viewModel) }
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