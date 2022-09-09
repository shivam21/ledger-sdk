package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.datasource.DummyDataSource
import lib.dehaat.ledger.initializer.toDateMonthName
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Pumpkin120
import lib.dehaat.ledger.resources.Warning10
import lib.dehaat.ledger.resources.textParagraphT1
import lib.dehaat.ledger.resources.textParagraphT2
import lib.dehaat.ledger.resources.textSubHeadingS3
import lib.dehaat.ledger.util.getAmountInRupees

@Preview(
	showBackground = true
)
@Composable
private fun SaveInterestScreenPreview() {
	SaveInterestScreen(
		summaryViewData = DummyDataSource.summaryViewData,
		showDetails = false,
		onViewDetailsClick = {}
	)
}

@Composable
fun SaveInterestScreen(
	summaryViewData: SummaryViewData?,
	showDetails: Boolean,
	onViewDetailsClick: () -> Unit
) {
	if (summaryViewData?.minOutstandingAmountDue?.toDoubleOrNull().orZero() > 0) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.background(shape = RoundedCornerShape(8.dp), color = Warning10)
				.padding(horizontal = 12.dp),
		) {
			Spacer(modifier = Modifier.height(12.dp))

			Text(
				text = stringResource(id = R.string.save_interest),
				style = textSubHeadingS3(Pumpkin120)
			)

			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.fillMaxWidth()
			) {
				Text(
					modifier = Modifier.padding(),
					text = stringResource(
						id = R.string.pay_till_date,
						summaryViewData?.minInterestOutstandingDate?.toDateMonthName() ?: ""
					),
					style = textParagraphT1(Neutral90)
				)

				Text(
					text = summaryViewData?.minOutstandingAmountDue.getAmountInRupees(),
					style = textSubHeadingS3(Neutral90)
				)
			}

			if (showDetails) {
				Spacer(modifier = Modifier.height(20.dp))
				Text(
					modifier = Modifier
						.fillMaxWidth()
						.clickable(onClick = onViewDetailsClick),
					text = stringResource(id = R.string.view_details),
					style = textParagraphT2(
						textColor = Neutral70,
						textDecoration = TextDecoration.Underline
					),
					textAlign = TextAlign.End
				)
			}

			Spacer(modifier = Modifier.height(16.dp))
		}
	}
}

fun Double?.orZero() = this ?: 0.0
