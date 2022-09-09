package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.SeaGreen10
import lib.dehaat.ledger.resources.textButtonB2
import lib.dehaat.ledger.resources.textParagraphT1
import lib.dehaat.ledger.resources.textParagraphT2

@Preview(
    showBackground = true,
    name = "RevampKeyValuePair Preview"
)
@Composable
private fun RevampKeyValuePairPreview() = LedgerTheme {
    RevampKeyValueChip(
        modifier = Modifier,
        key = "Key",
        value = "$ 6000",
        backgroundColor = SeaGreen10
    )
}

@Preview(
    showBackground = true,
    name = "KeyValuePair Preview"
)
@Composable
private fun KeyValuePairPreview() = LedgerTheme {
    RevampKeyValuePair(
        pair = Pair("Key", "Value"),
        style = Pair(textParagraphT1(), textParagraphT1())
    )
}

@Composable
fun RevampKeyValueChip(
    modifier: Modifier,
    key: String,
    value: String,
    backgroundColor: Color
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .background(color = backgroundColor, RoundedCornerShape(8.dp)),
    verticalArrangement = Arrangement.SpaceBetween
) {
    Text(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        text = key,
        style = textParagraphT2(Neutral80)
    )
    Text(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        text = value,
        style = textButtonB2(Neutral80)
    )
}

@Composable
fun RevampKeyValuePair(
    pair: Pair<String, String>,
    style: Pair<TextStyle, TextStyle>
) = Row(
    modifier = Modifier
        .fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Text(
        text = pair.first,
        style = style.first
    )

    Text(
        text = pair.second,
        style = style.second
    )
}
