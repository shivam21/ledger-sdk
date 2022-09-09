package lib.dehaat.ledger.presentation.ledger.credits.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.presentation.ledger.credits.LedgerCreditViewModel
import lib.dehaat.ledger.presentation.ledger.credits.ui.component.AvailableCreditLimitInfoForLmsAndNonLmsUseModal
import lib.dehaat.ledger.presentation.ledger.credits.ui.component.AvailableCreditLimitInfoModal
import lib.dehaat.ledger.presentation.ledger.credits.ui.component.AvailableCreditLimitView
import lib.dehaat.ledger.presentation.ledger.credits.ui.component.CreditLineCard
import lib.dehaat.ledger.presentation.model.creditlines.CreditLineViewData
import lib.dehaat.ledger.util.HandleAPIErrors
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun CreditsScreen(
    ledgerDetailViewModel: LedgerDetailViewModel,
    ledgerColors: LedgerColors,
    viewModel: LedgerCreditViewModel = hiltViewModel(),
    isLmsActivated: () -> Boolean?,
    openLenderOutstandingBottomSheet: (CreditLineViewData) -> Unit
) {
    HandleAPIErrors(viewModel.uiEvent)
    val uiState by viewModel.uiState.collectAsState()
    val totalAvailableCreditLimit by ledgerDetailViewModel.totalAvailableCreditLimit.collectAsState()
    if (!uiState.isError) {
        Column {
            if (isLmsActivated() == true && totalAvailableCreditLimit.isNotEmpty()) {
                AvailableCreditLimitView(
                    limitInRupees = totalAvailableCreditLimit.getAmountInRupees(),
                    ledgerColors = ledgerColors,
                    isLmsActivated = isLmsActivated,
                    onInfoIconClick = {
                        viewModel.showAvailableCreditLimitInfoForLmsAndNonLmsUseModal()
                    }
                )
            }

            if (isLmsActivated() == false) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                ) {
                    items(uiState.creditLinesViewData) { item ->
                        CreditLineCard(
                            ledgerColors = ledgerColors,
                            data = item,
                            onOutstandingInfoIconClick = {
                                openLenderOutstandingBottomSheet(it)
                            },
                            isLmsActivated = isLmsActivated
                        )
                        Divider(color = Color.Transparent, thickness = 12.dp)
                    }
                }
            }

            if (uiState.showAvailableCreditLimitInfoModal) {
                AvailableCreditLimitInfoModal(
                    ledgerColors = ledgerColors,
                    onOkClick = {
                        viewModel.hideAvailableCreditLimitInfoModal()
                    }
                )
            }

            if (uiState.showAvailableCreditLimitInfoForLmsAndNonLmsUseModal) {
                AvailableCreditLimitInfoForLmsAndNonLmsUseModal(
                    title = stringResource(id = R.string.credit_limit_info),
                    ledgerColors = ledgerColors,
                    lmsActivated = isLmsActivated(),
                    onOkClick = {
                        viewModel.hideAvailableCreditLimitInfoForLmsAndNonLmsUseModal()
                    }
                )
            }
        }
    }
}
