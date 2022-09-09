package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository

class GetCreditSummaryUseCase @Inject constructor(
    val repo: ILedgerRepository
) {
    suspend fun getCreditSummary(partnerId: String) = repo.getCreditSummary(partnerId)

    suspend fun getCreditSummaryV2(partnerId: String) = repo.getCreditSummaryV2(partnerId)
}
