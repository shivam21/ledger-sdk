package lib.dehaat.ledger.presentation.ledger.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.*
import lib.dehaat.ledger.initializer.Utils
import lib.dehaat.ledger.initializer.sdf
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.DBAColors
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.ledger.transactions.LedgerTransactionViewModel
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.resources.TextWhite
import lib.dehaat.ledger.resources.text16Sp
import lib.dehaat.ledger.resources.textMedium20Sp

@Preview(
    name = "RangeFilterDialog Preview AIMS",
    showBackground = true
)
@Composable
fun PreviewAIMS() {
    RangeFilterDialog(ledgerColors = AIMSColors(), filtered = { _, _ -> })
}

@Preview(
    name = "RangeFilterDialog Preview DBA",
    showBackground = true
)
@Composable
fun PreviewDBA() {
    RangeFilterDialog(ledgerColors = DBAColors(), filtered = { _, _ -> })
}

@Composable
fun RangeFilterDialog(
    ledgerColors: LedgerColors,
    filtered: (Long?, Long?) -> Unit
) {
    val currentDate = Calendar.getInstance().time
    val date = sdf.format(currentDate)
    var showDialog by remember { mutableStateOf(true) }
    var startRange by remember { mutableStateOf(date) }
    var endRange by remember { mutableStateOf(date) }
    val context = LocalContext.current

    val ledgerTransactionViewModel = viewModel<LedgerTransactionViewModel>()
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Card(
                shape = RoundedCornerShape(4.dp),
                elevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ledgerColors.TransactionAmountColor)
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        text = "Calendar Range Selector",
                        style = textMedium20Sp(TextWhite)
                    )

                    Text(
                        modifier = Modifier
                            .clickable {
                                Utils.openDatePickerDialog(context) {
                                    startRange = it
                                }
                            }
                            .background(ledgerColors.FilterDialogDateBGColor)
                            .padding(horizontal = 64.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        text = startRange,
                        style = text16Sp(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                        text = "To",
                        style = text16Sp(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        modifier = Modifier
                            .clickable {
                                Utils.openDatePickerDialog(context) {
                                    endRange = it
                                }
                            }
                            .background(ledgerColors.FilterDialogDateBGColor)
                            .padding(horizontal = 64.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        text = endRange,
                        style = text16Sp(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                    Divider(
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .clickable {
                                    showDialog = false
                                    filtered(null, null)
                                }
                                .padding(16.dp),
                            text = "Cancel",
                            style = text16Sp(textColor = Color.Red, fontWeight = FontWeight.Bold)
                        )
                        Divider(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                        )
                        Text(
                            modifier = Modifier
                                .clickable {
                                    showDialog = false
                                    val from = sdf.parse(startRange)?.time ?: 0
                                    val to = sdf.parse(endRange)?.time ?: 0
                                    ledgerTransactionViewModel.applyDaysFilter(
                                        DaysToFilter.CustomDays(from, to)
                                    )
                                    filtered(from, to)
                                }
                                .padding(16.dp),
                            text = "Ok",
                            style = text16Sp(textColor = Color.Green, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
