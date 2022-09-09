package lib.dehaat.ledger.presentation.ledger.creditlimit

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.ledger.ui.component.ViewDetails
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.textHeadingH3
import lib.dehaat.ledger.resources.textParagraphT1Highlight

@Preview(
    showBackground = true
)
@Composable
fun AvailableCreditLimitScreenPreview() = LedgerTheme {
    AvailableCreditLimitScreen("₹40,000") {}
}

@Composable
fun AvailableCreditLimitScreen(
    totalAvailableCreditLimit: String,
    onViewDetailsClick: () -> Unit
) = Column(
    modifier = Modifier
        .background(Color.White)
        .padding(horizontal = 20.dp)
) {
    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = stringResource(id = R.string.available_credit_limit_for_order_placement),
        style = textParagraphT1Highlight(Neutral90)
    )

    Spacer(modifier = Modifier.height(6.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = totalAvailableCreditLimit,
            style = textHeadingH3(Neutral80)
        )

        ViewDetails(onViewDetailsClick)
    }
    Spacer(modifier = Modifier.height(18.dp))
}
