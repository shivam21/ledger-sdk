package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.resources.SeaGreen100
import lib.dehaat.ledger.resources.TextWhite
import lib.dehaat.ledger.resources.textButtonB1
import lib.dehaat.ledger.util.clickableWithCorners

@Preview(
    name = "PaymentButton Preview",
    showBackground = true
)
@Composable
private fun PaymentButtonPreviewDBA() {
    PaymentButton {}
}

@Composable
fun PaymentButton(
    payNowClick: () -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
) {
    Text(
        text = stringResource(id = R.string.pay_now),
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithCorners(
                borderSize = 8.dp,
                backgroundColor = SeaGreen100,
                onClick = payNowClick
            )
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center,
        style = textButtonB1(TextWhite)
    )
}
