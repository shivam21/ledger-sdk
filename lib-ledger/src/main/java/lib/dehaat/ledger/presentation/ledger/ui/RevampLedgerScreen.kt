package lib.dehaat.ledger.presentation.ledger.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.launch
import lib.dehaat.ledger.framework.model.outstanding.OutstandingData
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.RevampLedgerViewModel
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.SpaceMedium
import lib.dehaat.ledger.presentation.ledger.bottomsheets.FilterScreen
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.creditlimit.AvailableCreditLimitNudgeScreen
import lib.dehaat.ledger.presentation.ledger.details.loanlist.InvoiceListViewModel
import lib.dehaat.ledger.presentation.ledger.details.totaloutstanding.TotalOutstandingScreenArgs
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.transactions.ui.TransactionsScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.LedgerHeaderScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.OutStandingPaymentView
import lib.dehaat.ledger.presentation.ledger.ui.component.TotalOutstandingCalculation
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.presentation.model.transactions.getNumberOfDays
import lib.dehaat.ledger.resources.Background
import lib.dehaat.ledger.util.getAmountInRupeesWithoutDecimal
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RevampLedgerScreen(
    viewModel: RevampLedgerViewModel,
    ledgerColors: LedgerColors,
    detailPageNavigationCallback: DetailPageNavigationCallback,
    onPayNowClick: () -> Unit,
    onOtherPaymentModeClick: () -> Unit,
    onError: (Exception) -> Unit,
    onBackPress: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val nestedScrollViewState = rememberNestedScrollViewState()
    var outstandingCalculationVisibility by rememberSaveable { mutableStateOf(false) }
    outstandingCalculationVisibility = !sheetState.isVisible && uiState.state == UIState.SUCCESS
    var filter: Pair<DaysToFilter, Int?> by remember {
        mutableStateOf(Pair(DaysToFilter.All, null))
    }
    LaunchedEffect(Unit) {
        viewModel.selectedDaysToFilterEvent.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collect { event ->
            viewModel.getTransactionSummaryFromServer(event)
            filter = Pair(event, event.getNumberOfDays())
        }
    }

    CommonContainer(
        title = viewModel.dcName,
        onBackPress = onBackPress,
        scaffoldState = scaffoldState,
        backgroundColor = Background,
        ledgerColors = ledgerColors,
        bottomBar = {
            if (uiState.summaryViewData?.externalFinancierSupported == false) {
                AnimatedVisibility(
                    visible = outstandingCalculationVisibility,
                    enter = expandVertically(animationSpec = tween(500)),
                    exit = shrinkVertically(animationSpec = tween(500))
                ) {
                    TotalOutstandingCalculation(
                        viewModel,
                        filter
                    )
                }
            }
        }
    ) {
        when (uiState.state) {
            is UIState.SUCCESS -> {
                ModalBottomSheetLayout(
                    modifier = Modifier.padding(it),
                    sheetContent = {
                        if (uiState.showFilterSheet) {
                            FilterScreen(
                                appliedFilter = uiState.appliedFilter,
                                onFilterApply = { daysToFilter ->
                                    viewModel.updateFilter(daysToFilter)
                                    scope.launch {
                                        sheetState.animateTo(ModalBottomSheetValue.Hidden)
                                    }
                                },
                                getStartEndDate = { daysToFilter ->
                                    viewModel.getSelectedDates(daysToFilter)
                                },
                                stateChange = sheetState.isVisible
                            ) {
                                scope.launch {
                                    sheetState.animateTo(ModalBottomSheetValue.Hidden)
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(1.dp))
                        }
                    },
                    sheetState = sheetState,
                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    VerticalNestedScrollView(
                        state = nestedScrollViewState,
                        header = {
                            val outstanding by LedgerSDK.outstandingDataFlow.collectAsState(
                                OutstandingData(false)
                            )
                            Column {
                                if (outstanding.showDialog) {
                                    OutStandingPaymentView(outstanding.amount)
                                }
                                uiState.summaryViewData?.let {
                                    if (it.totalAvailableCreditLimit.toDouble() < 0.0)
                                        it.minimumRepaymentAmount?.let {
                                            if (it.toDouble() > 0.0) {
                                                AvailableCreditLimitNudgeScreen(
                                                    it.getAmountInRupeesWithoutDecimal())
                                                SpaceMedium()
                                            }
                                        }
                                }
                                LedgerHeaderScreen(
                                    summaryViewData = uiState.summaryViewData,
                                    saveInterest = true,
                                    showPayNowButton = LedgerSDK.isDBA,
                                    onPayNowClick = onPayNowClick,
                                    onTotalOutstandingDetailsClick = {
                                        detailPageNavigationCallback.navigateToOutstandingDetailPage(
                                            TotalOutstandingScreenArgs.getBundle(viewModel.outstandingCreditLimitViewState)
                                        )
                                    },
                                    onShowInvoiceListDetailsClick = {
                                        detailPageNavigationCallback.navigateToInvoiceListPage(
                                            InvoiceListViewModel.getBundle(
                                                uiState.summaryViewData?.minInterestOutstandingDate,
                                                uiState.summaryViewData?.minOutstandingAmountDue,
                                                viewModel.partnerId
                                            )
                                        )
                                    },
                                    onOtherPaymentModeClick = onOtherPaymentModeClick
                                )
                            }
                        },
                        content = {
                            TransactionsScreen(
                                viewModel,
                                ledgerColors,
                                onError,
                                detailPageNavigationCallback
                            ) {
                                scope.launch {
                                    viewModel.showFilterBottomSheet()
                                    sheetState.animateTo(ModalBottomSheetValue.Expanded)
                                }
                            }
                        }
                    )
                }
            }
            is UIState.LOADING -> {
                ShowProgressDialog(ledgerColors) {
                    viewModel.updateProgressDialog(false)
                }
            }
            is UIState.ERROR -> {
                NoDataFound((uiState.state as? UIState.ERROR)?.message, onError)
            }
        }
    }
}
