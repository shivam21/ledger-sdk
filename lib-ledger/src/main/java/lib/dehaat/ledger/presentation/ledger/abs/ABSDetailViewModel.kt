package lib.dehaat.ledger.presentation.ledger.abs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.cleanarch.base.entity.result.api.APIResultEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetABSTransactionsUseCase
import lib.dehaat.ledger.entities.abs.ABSTransactionEntity
import lib.dehaat.ledger.framework.network.BasePagingSourceWithResponse
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.abs.state.ABSDetailViewModelState
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.model.abs.ABSTransactionViewData
import lib.dehaat.ledger.util.getFailureError
import javax.inject.Inject

@HiltViewModel
class ABSDetailViewModel @Inject constructor(
    private val transactionsUseCase: GetABSTransactionsUseCase,
    private val mapper: LedgerViewDataMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val partnerId by lazy {
        savedStateHandle.get<String>(LedgerConstants.KEY_PARTNER_ID).orEmpty()
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

    private val viewModelState = MutableStateFlow(ABSDetailViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        viewModelState.update {
            it.copy(amount = savedStateHandle.get<Double>(LedgerConstants.ABS_AMOUNT).orZero())
        }
    }

    val transactions = getABSTransactions()

    private fun getABSTransactions() = Pager(
        config = PagingConfig(pageSize = 1, enablePlaceholders = true),
        pagingSourceFactory = { getPagingSource() }
    ).flow.cachedIn(viewModelScope)

    private fun getPagingSource() =
        object :
            BasePagingSourceWithResponse<ABSTransactionViewData, List<ABSTransactionEntity>?>(
                { pageNumber: Int, pageSize: Int ->
                    processTransactionResponse(
                        transactionsUseCase.invoke(
                            partnerId,
                            pageSize,
                            (pageNumber - 1) * pageSize
                        )
                    )
                },
                parseDataList = { mapper.toABSTransactionsViewData(it) }
            ) {}

    private fun sendShowSnackBarEvent(message: String) {
        viewModelState.update {
            it.copy(isLoading = false)
        }
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowSnackbar(message))
        }
    }

    private fun processTransactionResponse(response: APIResultEntity<List<ABSTransactionEntity>?>) =
        when (response) {
            is APIResultEntity.Success -> {
                response.data
            }
            is APIResultEntity.Failure -> {
                sendShowSnackBarEvent((response.getFailureError()))
                emptyList()
            }
        }
}