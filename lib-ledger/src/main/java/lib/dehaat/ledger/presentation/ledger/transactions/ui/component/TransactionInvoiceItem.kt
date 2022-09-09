package lib.dehaat.ledger.presentation.ledger.transactions.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dehaat.androidbase.helper.isNotNull
import lib.dehaat.ledger.R
import lib.dehaat.ledger.datasource.DummyDataSource
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.DBAColors
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.initializer.toDateMonthYear
import lib.dehaat.ledger.presentation.ledger.transactions.constants.TransactionType
import lib.dehaat.ledger.presentation.model.transactions.TransactionViewData
import lib.dehaat.ledger.resources.textBold14Sp
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Preview(
    name = "TransactionInvoiceItem Preview AIMS",
    showBackground = true
)
@Composable
fun TransactionInvoiceItemPreviewAIMS() {
    TransactionInvoiceItem(
        ledgerColors = AIMSColors(),
        data = DummyDataSource.transactionViewData,
        onClick = {}
    )
}

@Preview(
    name = "TransactionInvoiceItem Preview DBA",
    showBackground = true
)
@Composable
fun TransactionInvoiceItemPreviewDBA() {
    TransactionInvoiceItem(
        ledgerColors = DBAColors(),
        data = DummyDataSource.transactionViewData,
        onClick = {}
    )
}

@Composable
fun TransactionInvoiceItem(
    ledgerColors: LedgerColors,
    data: TransactionViewData,
    onClick: (data: TransactionViewData) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick(data) }
            .fillMaxWidth()
            .wrapContentHeight()
            .background(shape = RoundedCornerShape(9.dp), color = Color.White)
            .padding(top = 16.dp, start = 16.dp, end = 8.dp, bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = provideTransactionIcon(data.type)
        Image(painter = painterResource(id = icon), contentDescription = "transaction icon")

        var transactionType = provideTransactionLabel(data.type)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Row {
                val tagLabel = provideTransactionTag(data = data)
                if (tagLabel.isNotNull()) {
                    transactionType = "$transactionType ($tagLabel)"
                }
                Text(
                    text = transactionType,
                    style = textMedium14Sp(textColor = ledgerColors.CtaDarkColor),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                modifier = Modifier.padding(top = 8.dp),
                style = textMedium14Sp(textColor = ledgerColors.TransactionDateColor),
                text = data.date.toDateMonthYear(),
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = data.amount.getAmountInRupees(),
            style = textBold14Sp(textColor = getAmountColor(data.type, ledgerColors = ledgerColors))
        )
        Image(
            painter = painterResource(id = R.drawable.ic_transaction_arrow_right),
            contentDescription = "Proceed"
        )
    }
}

fun getAmountColor(type: String, ledgerColors: LedgerColors) = when (type) {
    TransactionType.INVOICE -> ledgerColors.TransactionAmountColor
    else -> ledgerColors.DownloadInvoiceColor
}

@Composable
private fun provideTransactionIcon(type: String) = when (type) {
    TransactionType.INVOICE -> R.drawable.ic_transactions_invoice
    TransactionType.PAYMENT -> R.drawable.ic_transactions_interest
    TransactionType.CREDIT_NOTE -> R.drawable.ic_transaction_credit_note
    else -> R.drawable.ic_transactions_invoice
}

@Composable
private fun provideTransactionLabel(type: String) = when (type) {
    TransactionType.INVOICE -> "Invoice"
    TransactionType.PAYMENT -> "Payment"
    TransactionType.CREDIT_NOTE -> "Credit Note"
    else -> "Transaction"
}

private fun provideTransactionTag(data: TransactionViewData) = when (data.type) {
    TransactionType.PAYMENT -> data.paymentMode
    TransactionType.CREDIT_NOTE -> data.creditNoteReason
    else -> null
}
