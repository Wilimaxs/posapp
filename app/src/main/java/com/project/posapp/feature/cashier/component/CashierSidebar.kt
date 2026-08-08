package com.project.posapp.feature.cashier.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.posapp.route.Screen

@Composable
fun CashierSidebar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
) {
    val menus = listOf(
        Screen.POS,
        Screen.TRANSACTION,
//        Screen.RECEIVABLE,
//        Screen.RETURN,
//        Screen.SHIFT
    )
    Column(
        modifier = Modifier
            .width(190.dp)
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            text = "POS",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        menus.forEach { screen ->
            SidebarItem(
                screen = screen,
                selected = screen == currentScreen,
                onClick = {
                    onNavigate(screen)
                }
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        HorizontalDivider()

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Column {
            Text(
                text = "Admin",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Kasir Utama",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}