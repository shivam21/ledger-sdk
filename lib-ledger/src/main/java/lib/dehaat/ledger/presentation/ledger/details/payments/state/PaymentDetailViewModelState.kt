package lib.dehaat.ledger.presentation.ledger.details.payments.state

import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.model.detail.payment.PaymentDetailSummaryViewData

data class PaymentDetailViewModelState(
    val isLoading: Boolean = false,
    val paymentDetailSummaryViewData: PaymentDetailSummaryViewData? = null,
    val isSuccess: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) {
    fun toUiState() = PaymentDetailUIState(
        isLoading = isLoading,
        paymentDetailSummaryViewData = paymentDetailSummaryViewData,
        state = when {
            isSuccess -> UIState.SUCCESS
            isError -> UIState.ERROR(errorMessage)
            isLoading -> UIState.LOADING
            else -> UIState.SUCCESS
        }
    )
}

data class PaymentDetailUIState(
    val isLoading: Boolean,
    val paymentDetailSummaryViewData: PaymentDetailSummaryViewData?,
    val state: UIState
)
