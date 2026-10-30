package com.sunless.lynq.ui.navigation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sunless.lynq.ui.screens.DashboardScreen
import com.sunless.lynq.ui.screens.EmergencyScreen
import com.sunless.lynq.ui.screens.MeshExplorerScreen
import com.sunless.lynq.ui.screens.MessagesScreen
import com.sunless.lynq.ui.screens.SettingsScreen

import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunless.lynq.ui.LynqViewModel

private val TabOrder = mapOf(
    Screen.Dashboard.route to 0,
    Screen.Messages.route to 1,
    Screen.Emergency.route to 2,
    Screen.MeshExplorer.route to 3,
    Screen.Settings.route to 4,
)

private fun getSlideDirection(fromRoute: String?, toRoute: String?): Int {
    val fromIdx = TabOrder[fromRoute?.substringBefore("/")] ?: 0
    val toIdx = TabOrder[toRoute?.substringBefore("/")] ?: 0
    return if (toIdx >= fromIdx) 1 else -1
}

// Fluid easeOutQuint / Apple dynamic curve for silky flowy transitions
private val FluidSpringEasing = CubicBezierEasing(0.22f, 1.0f, 0.36f, 1.0f)
private const val SLIDE_DURATION = 350
private const val FADE_DURATION = 260

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
        enterTransition = {
            val dir = getSlideDirection(initialState.destination.route, targetState.destination.route)
            slideInHorizontally(
                animationSpec = tween(SLIDE_DURATION, easing = FluidSpringEasing),
            ) { fullWidth -> dir * fullWidth } +
                fadeIn(animationSpec = tween(FADE_DURATION, easing = FastOutSlowInEasing))
        },
        exitTransition = {
            val dir = getSlideDirection(initialState.destination.route, targetState.destination.route)
            slideOutHorizontally(
                animationSpec = tween(SLIDE_DURATION, easing = FluidSpringEasing),
            ) { fullWidth -> -dir * (fullWidth / 3) } +
                fadeOut(animationSpec = tween(FADE_DURATION, easing = FastOutSlowInEasing))
        },
        popEnterTransition = {
            val dir = getSlideDirection(initialState.destination.route, targetState.destination.route)
            slideInHorizontally(
                animationSpec = tween(SLIDE_DURATION, easing = FluidSpringEasing),
            ) { fullWidth -> -dir * (fullWidth / 3) } +
                fadeIn(animationSpec = tween(FADE_DURATION, easing = FastOutSlowInEasing))
        },
        popExitTransition = {
            val dir = getSlideDirection(initialState.destination.route, targetState.destination.route)
            slideOutHorizontally(
                animationSpec = tween(SLIDE_DURATION, easing = FluidSpringEasing),
            ) { fullWidth -> dir * fullWidth } +
                fadeOut(animationSpec = tween(FADE_DURATION, easing = FastOutSlowInEasing))
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