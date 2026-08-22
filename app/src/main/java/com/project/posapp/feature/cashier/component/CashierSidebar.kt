package com.project.posapp.feature.cashier.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.project.posapp.core.theme.Spacing
import com.project.posapp.route.Screen

private val menuItems = listOf(
    CashierMenuItem(
        screen = Screen.POS,
        icon = Icons.Outlined.PointOfSale
    ),
    CashierMenuItem(
        screen = Screen.HISTORY,
        icon = Icons.Outlined.HistoryEdu
    ),
    CashierMenuItem(
        screen = Screen.RECEIVABLE,
        icon = Icons.Outlined.AccountBalanceWallet
    )
)

@Composable
fun CashierSidebar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(88.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            .padding(
                vertical = Spacing.Large
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TR",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(
            modifier = Modifier.size(Spacing.Structural)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            menuItems.forEach { item ->
                SidebarItem(
                    title = item.screen.route,
                    icon = item.icon,
                    selected = item.screen == currentScreen,
                    onClick = { onNavigate(item.screen) }
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profil",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(
                modifier = Modifier.size(Spacing.Tight)
            )
            Text(
                text = "Admin",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Kasir Utama",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}