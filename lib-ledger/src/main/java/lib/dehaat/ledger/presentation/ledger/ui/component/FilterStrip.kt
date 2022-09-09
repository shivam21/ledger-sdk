package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors

@Composable
fun FilterStrip(
    modifier: Modifier = Modifier,
    ledgerColors: LedgerColors,
    withPenalty: Boolean,
    onWithPenaltyChange: (Boolean) -> Unit,
    onDaysToFilterIconClick: () -> Unit,
    onDateRangeFilterIconClick: () -> Unit,
    isLmsActivated: () -> Boolean?
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        /*if (isLmsActivated() == true) {
            Text(text = "Invoice with Penalty")
            Switch(
                modifier = Modifier.padding(start = 12.dp),
                checked = withPenalty,
                onCheckedChange = {
                    onWithPenaltyChange(it)
                }
            )
        }*/

        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.clickable {
                    onDaysToFilterIconClick()
                },
                painter = painterResource(id = R.drawable.ic_days_filter),
                contentDescription = "Days Filter",
                tint = ledgerColors.FilterIconsColor
            )
            Icon(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .clickable {
                        onDateRangeFilterIconClick()
                    },
                painter = painterResource(id = R.drawable.ic_calender_filter),
                contentDescription = "Days Filter",
                tint = ledgerColors.FilterIconsColor
            )
        }
    }
}
