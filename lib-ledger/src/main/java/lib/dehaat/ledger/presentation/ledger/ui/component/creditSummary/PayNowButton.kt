package lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.resources.textMedium16Sp

@Preview(
    name = "PayNowButton",
    showBackground = true
)
@Composable
private fun PayNowButtonPreview() {
    PayNowButton(ledgerColors = AIMSColors()) {}
}

@Composable
fun PayNowButton(
    ledgerColors: LedgerColors,
    onPayNowClick: () -> Unit
) = Row(
    modifier = Modifier
        .padding(vertical = 12.dp)
        .padding(end = 18.dp)
        .fillMaxWidth(),
    horizontalArrangement = Arrangement.End
) {
    Text(
        modifier = Modifier
            .clickable {
                onPayNowClick()
            }
            .background(
                color = ledgerColors.DownloadInvoiceColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp, horizontal = 32.dp),
        text = buildAnnotatedString {
            append(stringResource(R.string.pay_now_text))
            pushStyle(style = SpanStyle(fontSize = 12.sp))
            append("\n")
            append(stringResource(id = R.string.using_this_app))
        },
        style = textMedium16Sp(textColor = Color.White),
        textAlign = TextAlign.Center
    )
}
