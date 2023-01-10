package lib.dehaat.ledger.presentation.ledger.ui.component

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dehaat.androidbase.helper.isTrue
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.toDateMonthYear
import lib.dehaat.ledger.presentation.RevampLedgerViewModel
import lib.dehaat.ledger.presentation.ledger.transactions.ui.component.AbsBanner
import lib.dehaat.ledger.resources.Neutral60
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.textParagraphT2Highlight
import lib.dehaat.ledger.resources.textSubHeadingS3

@Composable
fun TransactionListHeader(
	ledgerViewModel: RevampLedgerViewModel,
	onFilterClick: () -> Unit,
	openABSDetailScreen: (Bundle) -> Unit
) = Column(
	modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
) {
	val transactionUIState by ledgerViewModel.transactionUIState.collectAsState()
	val uiState = ledgerViewModel.uiState.collectAsState()
	val filters = uiState.value.appliedFilter
	val abs = transactionUIState.summaryViewData?.abs
	Divider()
	Text(
		modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 12.dp),
		text = stringResource(id = R.string.all_transactions_list),
		style = textSubHeadingS3(Neutral80)
	)

	Divider()

	if (abs?.showBanner.isTrue()) {
		AbsBanner(abs, ledgerViewModel.ledgerAnalytics,openABSDetailScreen)
	}
	Spacer(modifier = Modifier.height(8.dp))

	FilterHeader(filters, onFilterClick)

	ledgerViewModel.getSelectedDates(filters)?.let {
		SelectedFilters(
			text = stringResource(
				id = R.string.from_to,
				it.first.toDateMonthYear(),
				it.second.toDateMonthYear()
			)
		)
	}
}

@Composable
private fun SelectedFilters(text: String) = Text(
	modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
	text = text,
	style = textParagraphT2Highlight(Neutral60)
)
