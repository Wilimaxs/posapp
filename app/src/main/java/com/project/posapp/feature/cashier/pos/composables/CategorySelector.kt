package com.project.posapp.feature.cashier.pos.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.project.posapp.core.theme.Spacing
import com.project.posapp.model.ProductCategory

@Composable
fun CategorySelector(
    categories: List<ProductCategory>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long?) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(state = rememberScrollState())
            .padding(bottom = Spacing.Standard, top = Spacing.Tight),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Tight)
    ) {
        FilterChip(
            selected = selectedCategoryId == null,
            onClick = { onCategorySelected(null) },
            label = {
                Text(
                    "Semua",
                    modifier = Modifier.padding(all = Spacing.Tight),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )
        categories.forEach { category ->
            FilterChip(
                selected = selectedCategoryId == category.id,
                onClick = { onCategorySelected(category.id) },
                label = {
                    Text(
                        category.name,
                        modifier = Modifier.padding(all = Spacing.Tight),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        }
    }
}