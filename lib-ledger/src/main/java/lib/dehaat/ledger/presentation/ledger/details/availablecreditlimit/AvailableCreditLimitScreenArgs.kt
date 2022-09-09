package lib.dehaat.ledger.presentation.ledger.details.availablecreditlimit

import android.os.Bundle
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.availablecreditlimit.AvailableCreditLimitViewState
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero

class AvailableCreditLimitScreenArgs(
	private val bundle: Bundle
) {

	fun getArgs() = AvailableCreditLimitViewState(
		totalAvailableCreditLimit = bundle.getString(TOTAL_AVAILABLE_CREDIT_LIMIT, ""),
		totalCreditLimit = bundle.getString(TOTAL_CREDIT_LIMIT, ""),
		outstandingAndDeliveredAmount = bundle.getString(OUTSTANDING_AND_DELIVERED_AMOUNT, ""),
		permanentCreditLimit = bundle.getString(PERMANENT_CREDIT_LIMIT, ""),
		bufferLimit = bundle.getString(BUFFER_LIMIT, ""),
		totalLimit = bundle.getString(TOTAL_LIMIT, "")
	)

	companion object {
		private const val TOTAL_AVAILABLE_CREDIT_LIMIT = "TOTAL_AVAILABLE_CREDIT_LIMIT"
		private const val TOTAL_CREDIT_LIMIT = "TOTAL_CREDIT_LIMIT"
		private const val OUTSTANDING_AND_DELIVERED_AMOUNT = "OUTSTANDING_AND_DELIVERED_AMOUNT"
		private const val PERMANENT_CREDIT_LIMIT = "PERMANENT_CREDIT_LIMIT"
		private const val BUFFER_LIMIT = "BUFFER_LIMIT"
		private const val TOTAL_LIMIT = "TOTAL_LIMIT"

		fun getBundle(
			viewState: AvailableCreditLimitViewState?
		) = Bundle().apply {
			viewState?.let {
				putString(TOTAL_AVAILABLE_CREDIT_LIMIT, it.totalAvailableCreditLimit)
				putString(TOTAL_CREDIT_LIMIT, it.totalCreditLimit)
				putString(
					OUTSTANDING_AND_DELIVERED_AMOUNT,
					(it.totalCreditLimit.toDoubleOrNull().orZero() -
							it.totalAvailableCreditLimit.toDoubleOrNull().orZero()).toString()
				)
				putString(PERMANENT_CREDIT_LIMIT, it.permanentCreditLimit)
				putString(BUFFER_LIMIT, it.bufferLimit)
				putString(TOTAL_LIMIT, it.totalLimit)
			}
		}
	}
}
