package lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import lib.dehaat.ledger.initializer.themes.DBAColors
import lib.dehaat.ledger.initializer.themes.LedgerColors

@Preview(
    name = "Payment Options DBA",
    showBackground = true
)
@Composable
private fun PaymentOptionsPreview() {
    PaymentOptionsButton(DBAColors()) { }
}

@Composable
fun PaymentOptionsButton(
    ledgerColors: LedgerColors,
    paymentOptionsButtonClick: () -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .border(
            BorderStroke(1.dp, ledgerColors.DownloadInvoiceColor),
            shape = RoundedCornerShape(9.dp)
        )
        .clickable { paymentOptionsButtonClick() },
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        text = stringResource(id = R.string.pay_by_bank_deposit_net_banking_amp_upi),
        modifier = Modifier.padding(16.dp)
    )

    Icon(
        painter = painterResource(id = R.drawable.ic_transaction_arrow_right),
        contentDescription = "",
        modifier = Modifier.padding(16.dp),
        tint = ledgerColors.DownloadInvoiceColor
    )
}
