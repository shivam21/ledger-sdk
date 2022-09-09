package lib.dehaat.ledger.presentation.ledger.credits.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.datasource.DummyDataSource
import lib.dehaat.ledger.util.getAmountInRupees
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.DBAColors
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.model.creditlines.CreditLineViewData
import lib.dehaat.ledger.resources.text12Sp
import lib.dehaat.ledger.resources.textBold14Sp

@Preview(
    name = "available credit limit AIMS",
    showBackground = true
)
@Composable
private fun CreditLineCardPreviewAIMS() {
    CreditLineCard(
        ledgerColors = AIMSColors(),
        data = DummyDataSource.creditLineViewData,
        onOutstandingInfoIconClick = {}
    ) { true }
}

@Preview(
    name = "available credit limit DBA",
    showBackground = true
)
@Composable
private fun CreditLineCardPreviewDBA() {
    CreditLineCard(
        ledgerColors = DBAColors(),
        data = DummyDataSource.creditLineViewData,
        onOutstandingInfoIconClick = {}
    ) { true }
}

@Composable
fun CreditLineCard(
    ledgerColors: LedgerColors,
    data: CreditLineViewData,
    onOutstandingInfoIconClick: (CreditLineViewData) -> Unit,
    isLmsActivated: () -> Boolean?
) {

    Column(
        modifier = Modifier
            .shadow(10.dp, shape = RoundedCornerShape(9.dp))
            .background(
                shape = RoundedCornerShape(9.dp),
                color = ledgerColors.CreditLineCardBG
            )
            .padding(16.dp)
    ) {

        Text(
            modifier = Modifier,
            style = textBold14Sp(textColor = ledgerColors.LenderNameColor),
            text = data.lenderViewName,
            maxLines = 1
        )

        Row(
            modifier = Modifier.padding(top = 27.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.outstanding),
                style = text12Sp(textColor = ledgerColors.TransactionDateColor),
                maxLines = 1
            )

            /*if (!data.belongsToGapl) {
                Image(
                    modifier = Modifier
                        .padding(start = 7.dp)
                        .clickable { onOutstandingInfoIconClick(data) },
                    painter = painterResource(id = R.drawable.ic_info_icon),
                    contentDescription = "info"
                )
            }*/
        }

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = data.totalOutstandingAmount.getAmountInRupees(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ledgerColors.LenderNameColor,
            maxLines = 1
        )

        Divider(
            Modifier
                .padding(vertical = 15.dp)
                .background(ledgerColors.CreditLineCardDividerColor),
            thickness = 1.dp
        )

        SanctionedCreditLimitView(
            limitInRupees = data.availableCreditLimit.getAmountInRupees(),
            ledgerColors = ledgerColors,
            isLmsActivated = isLmsActivated
        )
    }
}
