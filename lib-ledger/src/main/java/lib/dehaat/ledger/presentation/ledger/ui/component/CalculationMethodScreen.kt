package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.SeaGreen10
import lib.dehaat.ledger.resources.SeaGreen20
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.resources.textParagraphT2Highlight

@Preview(
    showBackground = true,
    name = "CalculationMethodScreen Preview"
)
@Composable
private fun CalculationMethodScreenPrev() = LedgerTheme {
    CalculationMethodScreen(
        backgroundColor = SeaGreen10,
        dividerColor = SeaGreen20,
        title = "Total Outstanding Calculation Method:",
        first = Pair("Purchases till Date", "₹ 4,20,000"),
        second = Pair("Total Credit Note Amount", "- ₹ 20,000"),
        total = Pair("Total Purchase", "₹ 4,00,000")
    )
}

@Composable
fun CalculationMethodScreen(
    backgroundColor: Color,
    dividerColor: Color,
    title: String,
    first: Pair<String, String>,
    second: Pair<String, String>,
    total: Pair<String, String>
) = Column(
    modifier = Modifier.fillMaxWidth()
) {
    Text(
        modifier = Modifier.padding(horizontal = 8.dp),
        text = title,
        style = textParagraphT1Highlight(Neutral90)
    )

    VerticalSpacer(height = 8.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = first.first,
                style = textParagraphT2Highlight(Neutral80)
            )

            Text(
                text = first.second,
                style = textParagraphT2Highlight(Neutral90)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = second.first,
                style = textParagraphT2Highlight(Neutral80)
            )

            Text(
                text = second.second,
                style = textParagraphT2Highlight(Neutral90)
            )
        }

        Divider(color = dividerColor)

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = total.first,
                style = textParagraphT1Highlight(Neutral80)
            )

            Text(
                text = total.second,
                style = textParagraphT1Highlight(Neutral90)
            )
        }

        VerticalSpacer(height = 8.dp)
    }
}
