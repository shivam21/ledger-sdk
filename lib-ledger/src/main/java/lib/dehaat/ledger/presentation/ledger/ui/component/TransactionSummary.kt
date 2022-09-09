package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.resources.TextWhite
import lib.dehaat.ledger.resources.text14Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun TransactionSummary(
    viewModel: LedgerDetailViewModel,
    ledgerColors: LedgerColors
) {
    viewModel.uiState.collectAsState().value.transactionSummaryViewData?.let {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(ledgerColors.LedgerPrimaryColor),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.purchase_amount))
                    pushStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                        )
                    )
                    append("\n${it.purchaseAmount.getAmountInRupees()}")
                },
                style = text14Sp(
                    textColor = TextWhite,
                    textAlign = TextAlign.Center
                )
            )
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .padding(vertical = 4.dp)
                    .background(Color.White)
                    .fillMaxHeight()
            )
            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.payment_amount))
                    pushStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                        )
                    )
                    append("\n${it.paymentAmount.getAmountInRupees()}")
                },
                style = text14Sp(
                    textColor = TextWhite,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}
