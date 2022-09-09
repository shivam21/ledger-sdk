package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.textParagraphT2
import lib.dehaat.ledger.util.clickableWithCorners

@Composable
fun ViewDetails(
    onViewDetailsClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .clickableWithCorners(4.dp, onClick = onViewDetailsClick),
        text = stringResource(id = R.string.view_details),
        style = textParagraphT2(
            textColor = Neutral70,
            textDecoration = TextDecoration.Underline
        )
    )
}
