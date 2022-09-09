package lib.dehaat.ledger.presentation.ledger.revamp.state

sealed class UIState {
    object LOADING : UIState()
    data class ERROR(val message: String) : UIState()
    object SUCCESS : UIState()
}
