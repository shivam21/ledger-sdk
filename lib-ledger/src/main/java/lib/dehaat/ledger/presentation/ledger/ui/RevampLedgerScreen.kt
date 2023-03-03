package lib.dehaat.ledger.presentation.ledger.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.launch
import lib.dehaat.ledger.R
import lib.dehaat.ledger.framework.model.outstanding.OutstandingData
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.RevampLedgerViewModel
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.SpaceMedium
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.bottomsheets.FilterScreen
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.creditlimit.AvailableCreditLimitNudgeScreen
import lib.dehaat.ledger.presentation.ledger.details.loanlist.InvoiceListViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.outstandingcalculation.OutstandingCalculationUiState
import lib.dehaat.ledger.presentation.ledger.revamp.state.transactions.ui.TransactionsScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.LedgerHeaderScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.OutStandingPaymentView
import lib.dehaat.ledger.presentation.ledger.ui.component.TotalOutstandingCalculation
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.presentation.model.transactions.getNumberOfDays
import lib.dehaat.ledger.resources.Background
import lib.dehaat.ledger.resources.Neutral30
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Primary110
import lib.dehaat.ledger.resources.Pumpkin120
import lib.dehaat.ledger.resources.SeaGreen100
import lib.dehaat.ledger.resources.Success10
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textParagraphT2Highlight
import lib.dehaat.ledger.resources.textSubHeadingS3
import lib.dehaat.ledger.util.DottedShape
import lib.dehaat.ledger.util.getAmountInRupeesWithoutDecimal
import lib.dehaat.ledger.util.toDoubleOrZero
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RevampLedgerScreen(
	viewModel: RevampLedgerViewModel,
	ledgerColors: LedgerColors,
	detailPageNavigationCallback: DetailPageNavigationCallback,
	onPayNowClick: () -> Unit,
	onOtherPaymentModeClick: () -> Unit,
	onError: (Exception) -> Unit,
	onBackPress: () -> Unit
) {
	val lifecycleOwner = LocalLifecycleOwner.current
	val uiState by viewModel.uiState.collectAsState()
	val transactionUIState by viewModel.transactionUIState.collectAsState()
	val scaffoldState = rememberScaffoldState()
	val scope = rememberCoroutineScope()
	val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
	val nestedScrollViewState = rememberNestedScrollViewState()
	var outstandingCalculationVisibility by rememberSaveable { mutableStateOf(false) }
	outstandingCalculationVisibility = !sheetState.isVisible && uiState.state == UIState.SUCCESS
	var filter: Pair<DaysToFilter, Int?> by remember {
		mutableStateOf(Pair(DaysToFilter.All, null))
	}
	LaunchedEffect(Unit) {
		viewModel.selectedDaysToFilterEvent.flowWithLifecycle(
			lifecycleOwner.lifecycle,
			Lifecycle.State.STARTED
		).collect { event ->
			viewModel.getTransactionSummaryFromServer(event)
			filter = Pair(event, event.getNumberOfDays())
		}
	}

	CommonContainer(
		title = viewModel.dcName,
		onBackPress = onBackPress,
		scaffoldState = scaffoldState,
		backgroundColor = Background,
		ledgerColors = ledgerColors,
		bottomBar = {
			if (uiState.summaryViewData?.externalFinancierSupported == false) {
				AnimatedVisibility(
					visible = outstandingCalculationVisibility,
					enter = expandVertically(animationSpec = tween(500)),
					exit = shrinkVertically(animationSpec = tween(500))
				) {
					TotalOutstandingCalculation(
						viewModel,
						filter
					)
				}
			} else {
				transactionUIState.outstandingCalculationUiState?.let {
					AnimatedVisibility(
						visible = sheetState.isVisible.not(),
						enter = expandVertically(animationSpec = tween(500)),
						exit = shrinkVertically(animationSpec = tween(500))
					) {
						OutstandingCalculationScreen(outstandingCalculationUiState = it, false) {
							scope.launch {
								sheetState.animateTo(ModalBottomSheetValue.Expanded)
							}
						}
					}
				}
			}
		}
	) {
		when (uiState.state) {
			is UIState.SUCCESS -> {
				ModalBottomSheetLayout(
					modifier = Modifier.padding(it),
					sheetContent = {
						Column(modifier = Modifier.padding(bottom = 1.dp)) {
							if (uiState.showFilterSheet) {
								FilterScreen(
									appliedFilter = uiState.appliedFilter,
									onFilterApply = { daysToFilter ->
										viewModel.updateFilter(daysToFilter)
										scope.launch {
											sheetState.animateTo(ModalBottomSheetValue.Hidden)
										}
									},
									getStartEndDate = { daysToFilter ->
										viewModel.getSelectedDates(daysToFilter)
									},
									stateChange = sheetState.isVisible
								) {
									scope.launch {
										sheetState.animateTo(ModalBottomSheetValue.Hidden)
									}
								}
							}
							transactionUIState.outstandingCalculationUiState?.let {
								OutstandingCalculationScreen(it) {
									scope.launch {
										sheetState.animateTo(ModalBottomSheetValue.Hidden)
									}
								}
							}
						}
					},
					sheetState = sheetState,
					sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
				) {
					VerticalNestedScrollView(
						state = nestedScrollViewState,
						header = {
							val outstanding by LedgerSDK.outstandingDataFlow.collectAsState(
								OutstandingData(false)
							)
							Column {
								if (outstanding.showDialog) {
									OutStandingPaymentView(outstanding.amount)
								}
								uiState.summaryViewData?.let {
									if (it.totalAvailableCreditLimit.toDoubleOrZero() < 0.0)
										it.minimumRepaymentAmount?.let {
											if (it.toDoubleOrZero() > 0.0) {
												AvailableCreditLimitNudgeScreen(
													it.getAmountInRupeesWithoutDecimal()
												)
												SpaceMedium()
											}
										}
								}
								LedgerHeaderScreen(
									summaryViewData = uiState.summaryViewData,

									showPayNowButton = LedgerSDK.isDBA,
									onPayNowClick = onPayNowClick,
									onTotalOutstandingDetailsClick = {
										scope.launch {
											sheetState.animateTo(ModalBottomSheetValue.Expanded)
										}
									},
									onShowInvoiceListDetailsClick = {
										detailPageNavigationCallback.navigateToInvoiceListPage(
											InvoiceListViewModel.getBundle(
												uiState.summaryViewData?.minInterestOutstandingDate,
												uiState.summaryViewData?.minOutstandingAmountDue,
												viewModel.partnerId
											)
										)
									},
									onOtherPaymentModeClick = onOtherPaymentModeClick
								)
							}
						},
						content = {
							TransactionsScreen(
								viewModel,
								ledgerColors,
								onError,
								detailPageNavigationCallback
							) {
								scope.launch {
									viewModel.showFilterBottomSheet()
									sheetState.animateTo(ModalBottomSheetValue.Expanded)
								}
							}
						}
					)
				}
			}
			is UIState.LOADING -> {
				ShowProgressDialog(ledgerColors) {
					viewModel.updateProgressDialog(false)
				}
			}
			is UIState.ERROR -> {
				NoDataFound((uiState.state as? UIState.ERROR)?.message, onError)
			}
		}
	}
}

@Composable
private fun OutstandingCalculationScreen(
	outstandingCalculationUiState: OutstandingCalculationUiState,
	showPartially: Boolean = true,
	showBottomSheet: () -> Unit
) = Column(
	modifier = Modifier
		.fillMaxWidth()
		.background(Color.White)
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.clickable(onClick = showBottomSheet)
			.padding(vertical = 12.dp, horizontal = 20.dp)
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Row {
			Image(
				painter = painterResource(id = R.drawable.ic_idea_bulb),
				contentDescription = stringResource(id = R.string.accessibility_icon)
			)

			Spacer(modifier = Modifier.width(8.dp))

			Text(
				text = stringResource(R.string.ledger_outstanding_calculation),
				style = TextStyle(
					fontWeight = FontWeight.SemiBold,
					fontSize = 14.sp,
					lineHeight = 18.sp,
					textDecoration = TextDecoration.Underline
				)
			)
		}

		Icon(
			modifier = Modifier.background(shape = CircleShape, color = Color.White),
			painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
			contentDescription = "",
			tint = SeaGreen100
		)
	}

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceEvenly,
		verticalAlignment = Alignment.CenterVertically
	) {
		Column(verticalArrangement = Arrangement.Center) {
			Text(text = stringResource(R.string.ledger_total_outstanding), style = textCaptionCP1(Neutral80))
			Text(
				text = outstandingCalculationUiState.totalOutstanding,
				style = textParagraphT2Highlight(Neutral80)
			)
		}

		Icon(
			modifier = Modifier.padding(horizontal = 8.dp),
			painter = painterResource(id = R.drawable.ic_equal),
			contentDescription = stringResource(id = R.string.accessibility_icon),
			tint = Neutral90
		)

		Column(verticalArrangement = Arrangement.Center) {
			Text(text = stringResource(R.string.ledger_total_purchase), style = textCaptionCP1(Neutral80))
			Text(
				text = outstandingCalculationUiState.totalPurchase,
				style = textParagraphT2Highlight(Neutral80)
			)
		}

		Icon(
			modifier = Modifier.padding(horizontal = 8.dp),
			painter = painterResource(id = R.drawable.ledger_minus),
			contentDescription = stringResource(id = R.string.accessibility_icon),
			tint = Neutral90
		)

		Column(verticalArrangement = Arrangement.Center) {
			Text(text = stringResource(R.string.ledger_total_payment), style = textCaptionCP1(Neutral80))
			Text(
				text = outstandingCalculationUiState.totalPayment,
				style = textParagraphT2Highlight(Neutral80)
			)
		}
	}


	VerticalSpacer(height = 16.dp)
	if (showPartially) {

		Divider(modifier = Modifier.height(16.dp))

		Text(
			modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp),
			text = stringResource(R.string.ledger_total_calculation),
			style = textSubHeadingS3(Neutral80)
		)

		Divider()

		VerticalSpacer(height = 12.dp)
		CalculationKeyValuePair(
			title = stringResource(R.string.ledger_total_invoice_amount),
			value = outstandingCalculationUiState.totalInvoiceAmount,
			Pumpkin120
		)

		VerticalSpacer(height = 12.dp)
		CalculationKeyValuePair(
			title = stringResource(R.string.ledger_total_debit_note_amount),
			value = outstandingCalculationUiState.totalDebitNoteAmount,
			Pumpkin120
		)

		VerticalSpacer(height = 12.dp)
		CalculationKeyValuePair(
			title = stringResource(R.string.ledger_outstanding_interest_amount),
			value = outstandingCalculationUiState.outstandingInterestAmount,
			Pumpkin120
		)

		VerticalSpacer(height = 12.dp)
		CalculationKeyValuePair(
			title = stringResource(R.string.ledger_paid_interest_amount),
			value = outstandingCalculationUiState.paidInterestAmount,
			Pumpkin120
		)

		VerticalSpacer(height = 12.dp)
		CalculationKeyValuePair(
			title = stringResource(R.string.ledger_credit_note_interst_returned_amount),
			value = outstandingCalculationUiState.creditNoteAmount,
			Primary110
		)

		VerticalSpacer(height = 12.dp)
		CalculationKeyValuePair(
			title = stringResource(R.string.ledger_credit_note_amount),
			value = outstandingCalculationUiState.totalCreditNoteAmount,
			Primary110
		)

		VerticalSpacer(height = 12.dp)

		Divider(
			modifier = Modifier
				.padding(horizontal = 20.dp)
				.background(
					color = Neutral30,
					shape = DottedShape(8.dp)
				)
		)

		VerticalSpacer(height = 8.dp)

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 20.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.ledger_total_purchases),
				style = textSubHeadingS3(Neutral90)
			)

			Text(
				text = outstandingCalculationUiState.totalPurchase,
				style = textSubHeadingS3(Neutral90)
			)
		}
		VerticalSpacer(height = 16.dp)
		Divider(modifier = Modifier.height(16.dp))


		Text(
			modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp),
			text = stringResource(R.string.ledger_total_payment_calculation),
			style = textSubHeadingS3(Neutral80)
		)

		Divider()

		VerticalSpacer(height = 12.dp)
		CalculationKeyValuePair(
			title = stringResource(R.string.ledger_total_payment_made_by_you),
			value = outstandingCalculationUiState.paidAmount,
			Primary110
		)

		VerticalSpacer(height = 12.dp)
		CalculationKeyValuePair(
			title = stringResource(R.string.ledger_paid_refund),
			value = outstandingCalculationUiState.paidRefund,
			Pumpkin120
		)

		VerticalSpacer(height = 12.dp)

		Divider(
			modifier = Modifier
				.padding(horizontal = 20.dp)
				.background(
					color = Neutral30,
					shape = DottedShape(8.dp)
				)
		)

		VerticalSpacer(height = 8.dp)
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 20.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.ledger_total_paid),
				style = textSubHeadingS3(Neutral90)
			)

			Text(
				text = outstandingCalculationUiState.totalPaid,
				style = textSubHeadingS3(Neutral90)
			)
		}
		VerticalSpacer(height = 16.dp)
	}
}

@Composable
fun CalculationKeyValuePair(
	title: String,
	value: String,
	valueColor: Color,
) = Row(
	modifier = Modifier
		.fillMaxWidth()
		.padding(horizontal = 20.dp),
	horizontalArrangement = Arrangement.SpaceBetween
) {
	Text(
		text = title,
		style = TextStyle(
			fontWeight = FontWeight.Normal,
			fontSize = 14.sp,
			lineHeight = 20.sp,
			textDecoration = TextDecoration.Underline
		)
	)

	Text(text = value, style = textParagraphT2Highlight(valueColor))
}
