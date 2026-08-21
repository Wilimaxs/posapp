package com.project.posapp.feature.cashier.history

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
import com.project.posapp.feature.cashier.history.composables.HistoryDetailPanel
import com.project.posapp.feature.cashier.history.composables.HistoryFilterBar
import com.project.posapp.feature.cashier.history.composables.HistorySummarySection
import com.project.posapp.feature.cashier.history.composables.HistoryTransactionList

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(Spacing.Large)
    ) {

        HistorySummarySection(
            summary = state.summary,
            isLoading = state.isSummaryLoading,
            errorMessage = state.summaryErrorMessage,
            onRetry = viewModel::loadSummary
        )

        Spacer(
            modifier = Modifier.height(Spacing.Standard)
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(
                Spacing.Standard
            )
        ) {
            Column(
                modifier = Modifier.weight(0.42f)
            ) {
                HistoryFilterBar(
                    state = state,
                    onSearchChange = viewModel::onSearchChange,
                    onDateFilterChange = viewModel::onDateFilterChange,
                    onStartDateChange = viewModel::onStartDateChange,
                    onEndDateChange = viewModel::onEndDateChange,
                    onApplyCustomFilter =
                        viewModel::applyCustomDateFilter,
                    onPaymentStatusChange =
                        viewModel::onPaymentStatusChange
                )

                Spacer(
                    modifier = Modifier.height(Spacing.Standard)
                )

                HistoryTransactionList(
                    state = state,
                    onTransactionClick =
                        viewModel::selectTransaction,
                    onRetry = viewModel::loadTransactions,
                    onLoadMore = viewModel::loadNextPage,
                    modifier = Modifier.weight(1f)
                )
            }

            HistoryDetailPanel(
                detail = state.detail,
                isLoading = state.isDetailLoading,
                errorMessage = state.detailErrorMessage,
                onRetry = {
                    viewModel.loadDetail()
                },
                modifier = Modifier.weight(0.58f)
            )
        }
    }
}