package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.common.uicomponent.HorizontalSpacer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.resources.ColorFFF5F5
import lib.dehaat.ledger.resources.Error110
import lib.dehaat.ledger.resources.Error90
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Warning10
import lib.dehaat.ledger.resources.textButtonB1
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textParagraphT1
import lib.dehaat.ledger.resources.textParagraphT2
import lib.dehaat.ledger.util.getRoundedAmountInRupees
import lib.dehaat.ledger.util.tooltip.ToolTipOffSet
import lib.dehaat.ledger.util.tooltip.ToolTipScreen
import lib.dehaat.ledger.util.tooltip.TooltipShape
import lib.dehaat.ledger.util.tooltip.ViewOffset
import lib.dehaat.ledger.util.tooltip.model.ArrowPosition

typealias onClickType = () -> Unit

@Composable
fun RepaymentScreen(
	summaryViewData: SummaryViewData,
	onPayNowClick: onClickType,
	onOtherPaymentModeClick: onClickType,
	onShowInvoiceListDetailsClick: onClickType
) = Column(
	modifier = Modifier.fillMaxWidth()
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
	) {
		var toolTipVisibility by remember { mutableStateOf(LedgerSDK.showOutstandingTooltip) }
		var viewOffset by remember { mutableStateOf(ViewOffset()) }

		if (summaryViewData.hideMinimumRepaymentSection) {
			VerticalSpacer(height = 24.dp)

			Column(modifier = Modifier.padding(horizontal = 20.dp)) {
				PaymentButton(payNowClick = onPayNowClick)
			}

			VerticalSpacer(height = 16.dp)
		} else {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.background(if (summaryViewData.isOrderingBlocked) ColorFFF5F5 else Warning10)
					.padding(horizontal = 20.dp)
			) {
				VerticalSpacer(height = 12.dp)
				if (summaryViewData.isOrderingBlocked) {

					Row(verticalAlignment = Alignment.Bottom) {
						Icon(
							modifier = Modifier.size(16.dp),
							painter = painterResource(id = R.drawable.ic_blocked),
							contentDescription = "blocked Icon",
							tint = Error90
						)

						HorizontalSpacer(width = 4.dp)

						Text(
							text = stringResource(R.string.your_ordering_is_blocked_ledger),
							style = textParagraphT1(Error110)
						)
					}

					VerticalSpacer(height = 16.dp)

					Divider(modifier = Modifier.background(Color.White))

					VerticalSpacer(height = 16.dp)
				}

				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = stringResource(R.string.minimum_repayment_amount),
						style = textParagraphT1(Neutral90)
					)

					Text(
						modifier = Modifier
							.onGloballyPositioned {
								viewOffset = viewOffset.copy(
									x = it.boundsInParent().bottomCenter.x,
									y = it.boundsInParent().bottomCenter.y
								)
							},
						text = summaryViewData.minimumRepaymentAmount.getRoundedAmountInRupees(),
						style = textButtonB1(
							textColor = Neutral90,
							textDecoration = TextDecoration.Underline
						)
					)
				}

				Text(
					text = if (summaryViewData.isOrderingBlocked) {
						stringResource(R.string.ledger_pay_immidiately)
					} else {
						stringResource(
							id = R.string.weekly_interest_till_date_,
							summaryViewData.repaymentDate
						)
					},
					style = textCaptionCP1(Neutral70)
				)

				VerticalSpacer(height = 24.dp)

				PaymentButton(payNowClick = onPayNowClick)

				VerticalSpacer(height = 16.dp)
			}

		}

		if (toolTipVisibility && summaryViewData.showToolTipInformation) {
			var toolTipOffSet by remember { mutableStateOf(ToolTipOffSet()) }
			val density = LocalDensity.current
			val shape = TooltipShape(
				cornerRadius = with(density) { 8.dp.toPx() },
				arrowPosition = ArrowPosition.TopRight(
					arrowWidth = with(density) { 12.dp.toPx() },
					arrowHeight = with(density) { 8.dp.toPx() },
					translationFromRight = with(density) { 18.dp.toPx() }
				)
			)

			ToolTipScreen(
				modifier = Modifier
					.onGloballyPositioned { cords ->
						toolTipOffSet = ToolTipOffSet(
							x = viewOffset.x - cords.boundsInParent().width + with(density) { 54.dp.toPx() },
							y = cords.boundsInParent().height - viewOffset.y
						)
					},
				offset = toolTipOffSet,
				title = if (summaryViewData.isOrderingBlocked)
					stringResource(R.string.ordering_blocked_due_to_overdue)
				else stringResource(R.string.this_is_your_overdue_amount),
				shape = shape
			) {
				toolTipVisibility = false
			}
		}
	}

	VerticalSpacer(height = 20.dp)

	Text(
		modifier = Modifier
			.padding(horizontal = 20.dp)
			.fillMaxWidth()
			.clickable(onClick = onOtherPaymentModeClick),
		text = stringResource(id = R.string.know_other_payment_methods),
		style = textParagraphT2(
			textColor = Neutral70,
			textDecoration = TextDecoration.Underline
		),
		textAlign = TextAlign.End
	)

	VerticalSpacer(height = 25.dp)
}
