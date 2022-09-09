package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.datasource.DummyDataSource
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.SeaGreen110
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textHeadingH3
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.resources.textParagraphT2
import lib.dehaat.ledger.util.getAmountInRupees
import lib.dehaat.ledger.util.getAmountInRupeesWithoutDecimal

@Preview(
    showBackground = true,
    name = "LedgerHeaderScreen Preview DBA"
)
@Composable
private fun LedgerHeaderScreenPreview() {
    LedgerHeaderScreen(
        summaryViewData = DummyDataSource.summaryViewData,
        saveInterest = true,
        showPayNowButton = true,
        onPayNowClick = {},
        onTotalOutstandingDetailsClick = {},
        onShowInvoiceListDetailsClick = {}
    ) {}
}

@Preview(
    showBackground = true,
    name = "LedgerHeaderScreen Preview AIMS"
)
@Composable
private fun LedgerHeaderScreenAIMSPreview() {
    LedgerHeaderScreen(
        summaryViewData = DummyDataSource.summaryViewData,
        saveInterest = true,
        showPayNowButton = false,
        onPayNowClick = {},
        onTotalOutstandingDetailsClick = {},
        onShowInvoiceListDetailsClick = {}
    ) {}
}

@Composable
fun LedgerHeaderScreen(
    summaryViewData: SummaryViewData?,
    saveInterest: Boolean,
    showPayNowButton: Boolean,
    onPayNowClick: () -> Unit,
    onTotalOutstandingDetailsClick: () -> Unit,
    onShowInvoiceListDetailsClick: () -> Unit,
    onOtherPaymentModeClick: () -> Unit
) = Column(
    modifier = Modifier
        .background(Color.White)
        .padding(horizontal = 20.dp)
        .padding(bottom = 12.dp)
        .fillMaxWidth()
) {
    val totalOutstandingAmount = summaryViewData?.totalOutstandingAmount?.toDoubleOrNull() ?: 0.0
    VerticalSpacer(height = 24.dp)
    Text(
        text = stringResource(id = R.string.total_outstanding),
        style = textParagraphT1Highlight(Neutral90)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = summaryViewData?.totalOutstandingAmount.getAmountInRupeesWithoutDecimal(),
            style = textHeadingH3(
                textColor = if (totalOutstandingAmount < 0) SeaGreen110 else Neutral80
            )
        )

        ViewDetails(onTotalOutstandingDetailsClick)
    }

    if (totalOutstandingAmount < 0) {
        val outstandingAmount = totalOutstandingAmount * -1
        VerticalSpacer(height = 4.dp)
        Text(
            text = stringResource(
                id = R.string.your_advance_amount,
                outstandingAmount.toString().getAmountInRupeesWithoutDecimal()
            ),
            style = textCaptionCP1(Neutral80)
        )
    }

    if (saveInterest) {
        Spacer(modifier = Modifier.height(12.dp))

        SaveInterestScreen(
            summaryViewData = summaryViewData,
            showDetails = false,
            onViewDetailsClick = onShowInvoiceListDetailsClick
        )
    }

    if (showPayNowButton) {

        Spacer(modifier = Modifier.height(12.dp))
        PaymentButton(payNowClick = onPayNowClick)
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOtherPaymentModeClick),
            text = stringResource(id = R.string.know_other_payment_methods),
            style = textParagraphT2(
                textColor = Neutral70,
                textDecoration = TextDecoration.Underline
            ),
            textAlign = TextAlign.End
        )
    }
}
