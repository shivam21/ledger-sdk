package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(pagerState: PagerState, ledgerColors: LedgerColors, onClickTab: (index: Int) -> Unit) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
    ) {
        val currentPage = pagerState.currentPage
        val modifier = Modifier
            .weight(1f)
        when (currentPage) {
            1 -> {
                TabDefault(
                    label = stringResource(R.string.transactions),
                    modifier = modifier
                        .padding(end = 8.dp)
                        .clickable {
                            onClickTab(0)
                        },
                    ledgerColors = ledgerColors
                )
                TabSelected(
                    label = stringResource(R.string.credits),
                    modifier = modifier
                        .padding(start = 8.dp)
                        .clickable {
                            onClickTab(1)
                        },
                    ledgerColors = ledgerColors
                )
            }
            else -> {
                TabSelected(
                    label = stringResource(id = R.string.transactions),
                    modifier = modifier
                        .padding(end = 8.dp)
                        .clickable {
                            onClickTab(0)
                        },
                    ledgerColors = ledgerColors
                )
                TabDefault(
                    label = stringResource(id = R.string.credits),
                    modifier = modifier
                        .padding(start = 8.dp)
                        .clickable {
                            onClickTab(1)
                        },
                    ledgerColors = ledgerColors
                )
            }
        }
    }
}

