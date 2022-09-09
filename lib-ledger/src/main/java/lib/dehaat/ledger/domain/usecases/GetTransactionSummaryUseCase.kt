package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository

class GetTransactionSummaryUseCase @Inject constructor(
    private val repo: ILedgerRepository
) {
    suspend operator fun invoke(
        partnerId: String,
        fromDate: Long?,
        toDate: Long?
    ) = repo.getTransactionSummary(partnerId, fromDate, toDate)
}
