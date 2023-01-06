package lib.dehaat.ledger.presentation.ledger.abs.state

data class ABSDetailViewModelState(
    val isLoading: Boolean = false,
    val amount: Double = 0.0
) {
    fun toUiState() = ABSDetailUIState(isLoading = isLoading, amount = amount)
}

data class ABSDetailUIState(val isLoading: Boolean, val amount: Double)