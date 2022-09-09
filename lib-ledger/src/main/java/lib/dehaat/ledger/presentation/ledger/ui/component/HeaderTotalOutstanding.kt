package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.datasource.DummyDataSource
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.DBAColors
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.resources.text14Sp
import lib.dehaat.ledger.resources.textBold14Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Preview(
    name = "Total Outstanding Header AIMS",
    showBackground = true
)
@Composable
private fun TotalOutstandingAIMSPreview() {
    HeaderTotalOutstanding(
        creditSummaryData = DummyDataSource.creditSummaryViewData,
        ledgerColors = AIMSColors(),
        isLmsActivated = { true }
    ) {}
}

@Preview(
    name = "Total Outstanding Header DBA",
    showBackground = true
)
@Composable
private fun TotalOutstandingDBAPreview() {
    HeaderTotalOutstanding(
        creditSummaryData = DummyDataSource.creditSummaryViewData,
        ledgerColors = DBAColors(),
        isLmsActivated = { true }
    ) {}
}

@Composable
fun HeaderTotalOutstanding(
    creditSummaryData: CreditSummaryViewData?,
    ledgerColors: LedgerColors,
    isLmsActivated: () -> Boolean?,
    onClickTotalOutstandingInfo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 12.dp,
                start = 32.dp,
                end = 32.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.total_outstanding),
                    style = text14Sp(textColor = ledgerColors.CtaColor)
                )
                if (isLmsActivated() == true) {
                    Icon(
                        modifier = Modifier
                            .padding(start = 7.dp)
                            .clickable { onClickTotalOutstandingInfo() },
                        painter = painterResource(id = R.drawable.ic_info_icon),
                        contentDescription = "info"
                    )
                }
            }

            creditSummaryData?.credit?.totalOutstandingAmount?.let {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = it.getAmountInRupees(),
                    style = textBold14Sp(textColor = ledgerColors.CtaColor)
                )
            }
        }
    }
}
