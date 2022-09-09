package lib.dehaat.ledger.domain.revamp.usecase

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository

class GetInvoiceListUseCase @Inject constructor(val repo: ILedgerRepository) {
    suspend fun getInvoices(
        partnerId: String,
        limit: Int,
        offset: Int,
        isInterestApproached: Boolean
    ) = repo.getInterestApproachedInvoices(
        partnerId,
        limit,
        offset,
        isInterestApproached
    )
}
