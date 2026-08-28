package com.project.posapp.feature.cashier.receivable.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.posapp.core.network.onError
import com.project.posapp.core.network.onSuccess
import com.project.posapp.model.ReceivableDetail
import com.project.posapp.repository.ReceivableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceivablePaymentViewModel @Inject constructor(
    private val repository: ReceivableRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ReceivablePaymentUiState())

    val uiState = _uiState.asStateFlow()

    fun initialize(detail: ReceivableDetail) {
        if (_uiState.value.detail?.saleId == detail.saleId) {
            return
        }

        _uiState.value = ReceivablePaymentUiState(
            detail = detail
        )
    }

    fun onAmountChange(value: String) {
        val cleanValue = value
            .filter(Char::isDigit)
            .take(12)

        _uiState.update {
            it.copy(
                amount = cleanValue,
                errorMessage = null
            )
        }
    }

    fun onNotesChange(value: String) {
        _uiState.update {
            it.copy(
                notes = value.take(255)
            )
        }
    }

    fun submit() {
        val state = _uiState.value
        val saleId = state.detail?.saleId ?: return

        if (!state.canSubmit) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            repository.createPayment(
                saleId = saleId,
                amount = state.amountValue,
                notes = state.notes
            ).onSuccess { result ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        payment = result.data,
                        errorMessage = null
                    )
                }
            }.onError { result ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun dismissError() {
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }

    fun reset() {
        _uiState.value = ReceivablePaymentUiState()
    }
}