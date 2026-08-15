package com.project.posapp.feature.cashier.pos.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.posapp.core.network.NetworkResult
import com.project.posapp.repository.PosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class PosPreviewViewModel @Inject constructor(
    private val repository: PosRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(value = PosPreviewUiState())
    val uiState = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    fun loadPreview(
        customerId: Long?,
        items: Map<Long, Int>
    ) {
        if (_uiState.value.isLoading || items.isEmpty()) {
            return
        }
        countdownJob?.cancel()

        viewModelScope.launch {
            _uiState.value = PosPreviewUiState(
                isLoading = true
            )

            when (val result = repository.checkoutPreview(customerId = customerId, items = items)
            ) {
                is NetworkResult.Success -> {
                    val preview = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            preview = preview,
                            errorMessage = null
                        )
                    }
                    startCountdown(expiresAt = preview.expiresAt)
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun selectedPaymentSchema(
        schema: PosPaymentScheme
    ) {
        _uiState.update {
            it.copy(
                paymentSchema = schema
            )
        }
    }

    fun selectedPaymentMethode(
        method: PosPaymentMethod
    ) {
        if (method == _uiState.value.paymentMethod) {
            _uiState.update {
                it.copy(
                    paymentMethod = null
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    paymentMethod = method
                )
            }
        }
    }

    fun onDownPaymentChange(
        value: String
    ) {
        val amount = value.filter(predicate = Char::isDigit).take(n = 15)

        _uiState.update {
            it.copy(
                downPayment = amount
            )
        }
    }

    fun onDueDateChange(
        value: String
    ) {
        _uiState.update {
            it.copy(
                dueDate = value
            )
        }
    }

    fun dismiss() {
        countdownJob?.cancel()

        _uiState.value = PosPreviewUiState()
    }

    private fun startCountdown(
        expiresAt: String?
    ) {
        countdownJob?.cancel()

        val expiresInstant = expiresAt
            ?.let {
                runCatching {
                    Instant.parse(it)
                }.getOrNull()
            }
            ?: run {
                _uiState.update {
                    it.copy(remainingSeconds = null)
                }
                return
            }

        countdownJob = viewModelScope.launch {
            while (true) {
                val remainingSeconds = (
                        expiresInstant.epochSecond -
                                Instant.now().epochSecond
                        ).coerceAtLeast(0L)

                _uiState.update {
                    it.copy(
                        remainingSeconds = remainingSeconds
                    )
                }

                if (remainingSeconds == 0L) {
                    break
                }

                delay(1_000)
            }
        }
    }
}