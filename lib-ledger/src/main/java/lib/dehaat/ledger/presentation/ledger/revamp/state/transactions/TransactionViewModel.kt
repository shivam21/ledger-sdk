package lib.dehaat.ledger.presentation.ledger.revamp.state.transactions

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import lib.dehaat.ledger.domain.usecases.GetTransactionsUseCase
import lib.dehaat.ledger.entities.revamp.transaction.TransactionEntityV2
import lib.dehaat.ledger.framework.network.BasePagingSourceWithResponse
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.model.revamp.transactions.TransactionViewDataV2
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.util.getFailureError

@HiltViewModel
class TransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val mapper: LedgerViewDataMapper
) : BaseViewModel() {

    private val partnerId by lazy {
        savedStateHandle.get<String>(LedgerConstants.KEY_PARTNER_ID) ?: ""
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

    private var daysToFilter: DaysToFilter = DaysToFilter.All

    var transactionsList: Flow<PagingData<TransactionViewDataV2>>
        private set

    init {
        transactionsList = getTransactionPaging()
    }

    private fun getTransactionPaging() = Pager(
        config = PagingConfig(pageSize = 1, enablePlaceholders = true),
        pagingSourceFactory = { getPagingSource() }
    ).flow.cachedIn(viewModelScope)

    private fun getPagingSource() =
        object : BasePagingSourceWithResponse<TransactionViewDataV2, List<TransactionEntityV2>>(
            { pageNumber: Int, pageSize: Int ->
                val response = getTransactionsFromServer(pageSize, pageNumber)
                processTransactionListResponse(response)
            },
            onResponse = { _, _ -> },
            parseDataList = {
                mapper.toTransactionEntity(it)
            }
        ) {}

    private suspend fun getTransactionsFromServer(
        pageSize: Int,
        pageNumber: Int
    ): APIResultEntity<List<TransactionEntityV2>> {
        val (fromDate, toDate) = getFromAndToDate()
        return getTransactionsUseCase.getTransactionsV2(
            partnerId = partnerId,
            fromDate = fromDate,
            toDate = toDate,
            limit = pageSize,
            offset = (pageNumber - 1) * pageSize
        )
    }

    private fun processTransactionListResponse(
        response: APIResultEntity<List<TransactionEntityV2>>
    ) = when (response) {
        is APIResultEntity.Success -> {
            response.data
        }
        is APIResultEntity.Failure -> {
            sendFailureEvent(response.getFailureError())
            emptyList()
        }
    }

    private fun sendFailureEvent(message: String) {
        callInViewModelScope { _uiEvent.emit(UiEvent.ShowSnackbar(message)) }
        Log.d("ERRORS", "sendFailureEvent: $message")
    }

    private fun getFromAndToDate() = when (val filter = daysToFilter) {
        DaysToFilter.All -> Pair(null, null)
        DaysToFilter.SevenDays -> calculateTimeInMillisecond(7)
        DaysToFilter.OneMonth -> calculateTimeInMillisecond(31)
        DaysToFilter.ThreeMonth -> calculateTimeInMillisecond(31 * 3)
        is DaysToFilter.CustomDays -> calculateCustomDaysMillisecond(filter)
    }

    private fun calculateCustomDaysMillisecond(dayCount: DaysToFilter.CustomDays): Pair<Long, Long> {
        val currentDaySec = dayCount.toDateMilliSec / 1000
        val pastDaySec = dayCount.fromDateMilliSec / 1000
        return Pair(pastDaySec, currentDaySec)
    }

    private fun calculateTimeInMillisecond(dayCount: Int): Pair<Long, Long> {
        val daysSec = dayCount * 24 * 60 * 60
        val currentDaySec = System.currentTimeMillis() / 1000
        val pastDaySec = currentDaySec.minus(daysSec)
        return Pair(pastDaySec, currentDaySec)
    }

    fun updateSelectedFilter(daysToFilter: DaysToFilter) {
        this.daysToFilter = daysToFilter
        transactionsList = getTransactionPaging()
        refresh()
    }

    private fun refresh() {
        callInViewModelScope { _uiEvent.emit(UiEvent.RefreshList) }
    }
}
