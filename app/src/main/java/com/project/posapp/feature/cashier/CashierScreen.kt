package com.project.posapp.feature.cashier

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.posapp.feature.cashier.component.CashierHeader
import com.project.posapp.feature.cashier.component.CashierSidebar
import com.project.posapp.feature.cashier.pos.PosScreen
import com.project.posapp.feature.cashier.history.TransactionScreen
import com.project.posapp.route.Screen

@Composable
fun CashierScreen() {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = Screen.fromRoute(backStackEntry?.destination?.route)

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        CashierSidebar(
            currentScreen = currentScreen,
            onNavigate = { screen ->
                if (screen != currentScreen) {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                        popUpTo(Screen.POS.route) { saveState = true }
                        restoreState = true
                    }
                }
            }
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CashierHeader(
                title = currentScreen.title
            )

            NavHost(
                navController = navController,
                startDestination = Screen.POS.route,
                modifier = Modifier.fillMaxSize(),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                composable(Screen.POS.route) {
                    PosScreen()
                }

                composable(Screen.HISTORY.route) {
                    TransactionScreen()
                }
            }
        }
    }
}