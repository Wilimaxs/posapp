package com.project.posapp.feature.cashier.receivable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.project.posapp.core.theme.AppBackground
import com.project.posapp.core.theme.Spacing
import com.project.posapp.feature.cashier.receivable.composables.ReceivableDetailPanel
import com.project.posapp.feature.cashier.receivable.composables.ReceivableFilterBar
import com.project.posapp.feature.cashier.receivable.composables.ReceivableList
import com.project.posapp.feature.cashier.receivable.composables.ReceivableSummarySection
import com.project.posapp.feature.cashier.receivable.composables.ReceivableTransactionDialog

@Composable
fun ReceivableScreen(
    viewModel: ReceivableViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(Spacing.Large)
    ) {
        ReceivableSummarySection(
            summary = state.summary,
            isLoading = state.isSummaryLoading,
            errorMessage = state.summaryErrorMessage,
            onRetry = viewModel::loadSummary
        )

        Spacer(
            modifier = Modifier.height(Spacing.Compact)
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement =
                Arrangement.spacedBy(Spacing.Standard)
        ) {
            Column(
                modifier = Modifier.weight(0.42f)
            ) {
                ReceivableFilterBar(
                    state = state,
                    onSearchChange =
                        viewModel::onSearchChange,
                    onDueStatusChange =
                        viewModel::onDueStatusChange,
                    onSortChange =
                        viewModel::onSortChange
                )

                Spacer(
                    modifier =
                        Modifier.height(Spacing.Standard)
                )

                ReceivableList(
                    state = state,
                    onReceivableClick =
                        viewModel::selectReceivable,
                    onRetry = viewModel::loadList,
                    onLoadMore = viewModel::loadNextPage,
                    modifier = Modifier.weight(1f)
                )
            }

            ReceivableDetailPanel(
                detail = state.detail,
                isLoading = state.isDetailLoading,
                errorMessage = state.detailErrorMessage,
                onRetry = {
                    viewModel.loadDetail()
                },
                onShowTransaction =
                    viewModel::showTransactionDialog,
                modifier = Modifier.weight(0.58f)
            )
        }
    }

    if (state.showTransactionDialog) {
        state.detail?.let { detail ->
            ReceivableTransactionDialog(
                detail = detail,
                onDismiss =
                    viewModel::dismissTransactionDialog
            )
        }
    }
}