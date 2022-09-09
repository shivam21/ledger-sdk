package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import lib.dehaat.ledger.resources.LedgerTheme

@Preview(
    showBackground = true
)
@Composable
fun FilterHeaderPreview() = LedgerTheme {
    FilterHeader {}
}

@Composable
fun FilterHeader(
    onFilterClick: () -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onFilterClick)
        .padding(
            horizontal = 20.dp,
            vertical = 6.dp
        ),
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_filter),
        contentDescription = stringResource(id = R.string.accessibility_icon)
    )

    Spacer(modifier = Modifier.width(16.dp))

    Text(
        text = stringResource(id = R.string.filter)
    )

    Spacer(modifier = Modifier.width(4.dp))

    Text(
        text = stringResource(id = R.string.transactions_so_far)
    )

    Spacer(modifier = Modifier.width(10.dp))

    Icon(
        painter = painterResource(id = R.drawable.ic_down),
        contentDescription = stringResource(id = R.string.accessibility_icon)
    )

    Spacer(modifier = Modifier.height(16.dp))
}
