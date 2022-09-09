package lib.dehaat.ledger.presentation.ledger.revamp.state.transactions.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.dehaat.androidbase.helper.showToast
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.RevampLedgerViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgress
import lib.dehaat.ledger.presentation.ledger.details.interest.InterestDetailScreenArgs
import lib.dehaat.ledger.presentation.ledger.details.invoice.RevampInvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.payments.PaymentDetailViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.creditnote.CreditNoteDetailsViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.transactions.TransactionViewModel
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionCard
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionListHeader
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionType
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionsScreen(
    ledgerViewModel: RevampLedgerViewModel,
    ledgerColors: LedgerColors,
    onError: (Exception) -> Unit,
    detailPageNavigationCallback: DetailPageNavigationCallback,
    showFilterSheet: () -> Unit
) {
    val viewModel = hiltViewModel<TransactionViewModel>()
    val transactions = viewModel.transactionsList.collectAsLazyPagingItems()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var weeklyInterestVisibility by remember { mutableStateOf(true) }
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        stickyHeader {
            TransactionListHeader(
                ledgerViewModel,
                showFilterSheet
            )
        }
        items(transactions) { transaction ->
            transaction?.let {
                when (transaction.type) {
                    TransactionType.Invoice().invoiceType -> TransactionCard(
                        transactionType = TransactionType.Invoice(),
                        transaction = transaction
                    ) {
                        detailPageNavigationCallback.navigateToRevampInvoiceDetailPage(
                            RevampInvoiceDetailViewModel.getBundle(
                                transaction.ledgerId,
                                transaction.source
                            )
                        )
                    }
                    TransactionType.CreditNote().creditNoteType -> TransactionCard(
                        transactionType = TransactionType.CreditNote(),
                        transaction = transaction
                    ) {
                        detailPageNavigationCallback.navigateToRevampCreditNoteDetailPage(
                            CreditNoteDetailsViewModel.getBundle(transaction.ledgerId)
                        )
                    }
                    TransactionType.Payment().paymentType -> TransactionCard(
                        transactionType = TransactionType.Payment(),
                        transaction = transaction
                    ) {
                        detailPageNavigationCallback.navigateToRevampPaymentDetailPage(
                            PaymentDetailViewModel.getBundle(transaction.ledgerId)
                        )
                    }
                    TransactionType.Interest().interestType -> {
                        if (weeklyInterestVisibility) {
                            TransactionCard(
                                transactionType = TransactionType.Interest(),
                                transaction = transaction
                            ) {
                                detailPageNavigationCallback.navigateToRevampWeeklyInterestDetailPage(
                                    InterestDetailScreenArgs.getBundle(transaction, ledgerViewModel.uiState.value.summaryViewData)
                                )
                            }
                        }
                    }
                }
            }
        }
        transactions.apply {
            when {
                loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                    item { ShowProgress(ledgerColors) }
                }
                loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached && itemCount == 0 -> {
                    item { NoDataFound {} }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.uiEvent.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collect { event ->
            when (event) {
                UiEvent.RefreshList -> transactions.refresh()
                is UiEvent.ShowSnackbar -> {
                    onError(Exception(event.message))
                    if (LedgerSDK.isDebug) {
                        context.showToast(event.message)
                    } else {
                        context.showToast(R.string.tech_problem)
                    }
                }
                UiEvent.Success -> Unit
            }
        }
    }

    LaunchedEffect(Unit) {
        ledgerViewModel.selectedDaysToFilterEvent.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collect { event ->
            weeklyInterestVisibility = event == DaysToFilter.All
            viewModel.updateSelectedFilter(event)
        }
    }
}
