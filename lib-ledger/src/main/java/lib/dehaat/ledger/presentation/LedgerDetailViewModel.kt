package lib.dehaat.ledger.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetCreditLinesUseCase
import lib.dehaat.ledger.domain.usecases.GetCreditSummaryUseCase
import lib.dehaat.ledger.domain.usecases.GetTransactionSummaryUseCase
import lib.dehaat.ledger.entities.creditlines.CreditLineEntity
import lib.dehaat.ledger.entities.creditsummary.CreditSummaryEntity
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_PARTNER_ID
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.state.BottomSheetType
import lib.dehaat.ledger.presentation.ledger.state.LedgerDetailViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.model.creditlines.CreditLineViewData
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.presentation.model.transactions.toStartAndEndDates
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar

@HiltViewModel
class LedgerDetailViewModel @Inject constructor(
    private val getCreditSummaryUseCase: GetCreditSummaryUseCase,
    private val getCreditLinesUseCase: GetCreditLinesUseCase,
    private val getTransactionSummaryUseCase: GetTransactionSummaryUseCase,
    private val mapper: LedgerViewDataMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    var dcName: String = ""
    private val partnerId by lazy {
        savedStateHandle.get<String>(KEY_PARTNER_ID) ?: throw Exception(
            "Partner id should not null"
        )
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

    private val _selectedDaysToFilterEvent = MutableSharedFlow<DaysToFilter>()
    val selectedDaysToFilterEvent: SharedFlow<DaysToFilter> get() = _selectedDaysToFilterEvent

    private val _totalAvailableCreditLimit = MutableStateFlow("")
    val totalAvailableCreditLimit: StateFlow<String> get() = _totalAvailableCreditLimit

    private val viewModelState = MutableStateFlow(LedgerDetailViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        getLedgerData()
    }

    fun getLedgerData() {
        getCreditSummaryFromServer()
        getCreditLinesFromServer()
        getTransactionSummaryFromServer()
    }

    private fun getCreditLinesFromServer() {
        callInViewModelScope {
            callingAPI()
            val response = getCreditLinesUseCase.invoke(partnerId = partnerId)
            calledAPI()
            processCreditLinesResponse(response)
        }
    }

    private fun processCreditLinesResponse(result: APIResultEntity<List<CreditLineEntity>>) {
        result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) { lines ->
            viewModelState.update {
                it.copy(
                    isLoading = false,
                    overAllOutStandingDetailViewData = it.overAllOutStandingDetailViewData.copy(
                        creditLinesUsed = lines.map {
                            it.lenderViewName
                        })
                )
            }
        }
    }

    private fun getCreditSummaryFromServer() {
        callInViewModelScope {
            callingAPI()
            val response = getCreditSummaryUseCase.getCreditSummary(partnerId = partnerId)
            calledAPI()
            processCreditSummaryResponse(response)
        }
    }

    private fun processCreditSummaryResponse(result: APIResultEntity<CreditSummaryEntity?>) {
        result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) {
            it?.let { creditSummaryEntity ->
                val creditSummaryViewData = mapper.toCreditSummaryViewData(creditSummaryEntity)
                callInViewModelScope {
                    _totalAvailableCreditLimit.emit(creditSummaryViewData.credit.totalAvailableCreditLimit)
                }
                viewModelState.update { it ->
                    it.copy(
                        isLoading = false,
                        creditSummaryViewData = creditSummaryViewData,
                        overAllOutStandingDetailViewData = getOverAllSummaryData(
                            creditSummaryViewData
                        )
                    )
                }
            }
        }
    }

    fun getTransactionSummaryFromServer(daysToFilter: DaysToFilter? = null) = callInViewModelScope {
        callingAPI()
        val dates = daysToFilter?.toStartAndEndDates()
        val response = getTransactionSummaryUseCase.invoke(partnerId, dates?.first, dates?.second)
        calledAPI()
        processTransactionSummaryResponse(response)
    }

    private fun processTransactionSummaryResponse(
        result: APIResultEntity<TransactionSummaryEntity?>
    ) = result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) {
        it?.let { entity ->
            val transactionSummaryViewData = mapper.toTransactionSummaryViewData(entity)
            viewModelState.update { ledgerDetailViewModelState ->
                ledgerDetailViewModelState.copy(
                    isLoading = false,
                    transactionSummaryViewData = transactionSummaryViewData
                )
            }
        }
    }

    private fun sendShowSnackBarEvent(message: String) {
        updateAPIFailure()
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowSnackbar(message))
        }
    }

    private fun updateAPIFailure() = viewModelState.update {
        it.copy(isError = true)
    }

    private fun calledAPI() = updateProgressDialog(false)

    private fun callingAPI() = updateProgressDialog(true)

    fun updateProgressDialog(show: Boolean) = viewModelState.update {
        it.copy(isLoading = show)
    }

    private fun getOverAllSummaryData(creditSummaryViewData: CreditSummaryViewData) =
        with(creditSummaryViewData.credit) {
            viewModelState.value.overAllOutStandingDetailViewData.copy(
                principleOutstanding = principalOutstandingAmount,
                interestOutstanding = interestOutstandingAmount,
                overdueInterestOutstanding = overdueInterestOutstandingAmount,
                penaltyOutstanding = penaltyOutstandingAmount,
                undeliveredInvoices = creditSummaryViewData.info.undeliveredInvoiceAmount,
            )
        }

    fun isLMSActivated() =
        viewModelState.value.creditSummaryViewData?.credit?.externalFinancierSupported

    fun openAllOutstandingModal() {
        viewModelState.update {
            it.copy(
                bottomSheetType = BottomSheetType.OverAllOutStanding(data = viewModelState.value.overAllOutStandingDetailViewData)
            )
        }
    }

    fun closeOutstandingDialog() {
        viewModelState.update {
            it.copy(
                showOutstandingDialog = false
            )
        }
    }

    fun openOutstandingDialog() {
        viewModelState.update {
            it.copy(
                showOutstandingDialog = true
            )
        }
    }

    fun showLenderOutstandingModal(data: CreditLineViewData) {
        viewModelState.update {
            it.copy(
                lenderOutStandingDetailViewData = data.run {
                    it.lenderOutStandingDetailViewData.copy(
                        outstanding = totalOutstandingAmount,
                        principleOutstanding = principalOutstandingAmount,
                        interestOutstanding = interestOutstandingAmount,
                        overdueInterestOutstanding = overdueInterestOutstandingAmount,
                        penaltyOutstanding = penaltyOutstandingAmount,
                        advanceAmount = advanceAmount,
                        sanctionedCreditLimit = creditLimit,
                        lenderName = lenderViewName
                    )
                }
            )
        }
        viewModelState.update {
            it.copy(
                bottomSheetType = BottomSheetType.LenderOutStanding(data = it.lenderOutStandingDetailViewData)
            )
        }
    }

    fun showDaysFilterBottomSheet() {
        viewModelState.update {
            it.copy(
                bottomSheetType = BottomSheetType.DaysFilterTypeSheet(selectedFilter = it.selectedDaysFilter)
            )
        }
    }

    fun showDaysRangeFilterDialog(show: Boolean) = viewModelState.update {
        it.copy(showFilterRangeDialog = show)
    }

    fun updateSelectedFilter(selectedFilter: DaysToFilter) {
        viewModelState.update {
            it.copy(selectedDaysFilter = selectedFilter)
        }
        callInViewModelScope {
            _selectedDaysToFilterEvent.emit(selectedFilter)
        }
    }
}
