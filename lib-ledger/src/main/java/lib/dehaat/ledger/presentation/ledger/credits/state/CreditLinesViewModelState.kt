package lib.dehaat.ledger.presentation.ledger.credits.state

import lib.dehaat.ledger.presentation.model.creditlines.CreditLineViewData

data class CreditLinesViewModelState(
    val creditLinesViewData: List<CreditLineViewData>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val showAvailableCreditLimitInfoModal: Boolean = false,
    val showAvailableCreditLimitInfoForLmsAndNonLmsUseModal: Boolean = false,
) {
    fun toUiState() = CreditLinesUIState(
        creditLinesViewData = creditLinesViewData ?: emptyList(),
        isLoading = isLoading,
        isError = isError,
        showAvailableCreditLimitInfoModal = showAvailableCreditLimitInfoModal,
        showAvailableCreditLimitInfoForLmsAndNonLmsUseModal = showAvailableCreditLimitInfoForLmsAndNonLmsUseModal,
    )
}

data class CreditLinesUIState(
    val creditLinesViewData: List<CreditLineViewData>,
    val isLoading: Boolean,
    val isError: Boolean,
    val showAvailableCreditLimitInfoModal: Boolean,
    val showAvailableCreditLimitInfoForLmsAndNonLmsUseModal: Boolean,
)
