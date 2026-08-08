package com.project.posapp.feature.cashier.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.posapp.route.Screen

@Composable
fun SidebarItem(
    screen: Screen,
    selected: Boolean,
    onClick: () -> Unit
) {
    val icon = when (screen) {
        Screen.POS -> Icons.Outlined.ShoppingCart
        Screen.TRANSACTION -> Icons.AutoMirrored.Outlined.ReceiptLong
//        Screen.RECEIVABLE -> Icons.Outlined.AccountBalanceWallet
//        Screen.RETURN -> Icons.AutoMirrored.Outlined.KeyboardReturn
//        Screen.SHIFT -> Icons.Outlined.Schedule
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 12.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = screen.title
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = screen.title,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}