package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository

class GetPaymentDetailUseCase @Inject constructor(
    val repo: ILedgerRepository
) {
    suspend operator fun invoke(ledgerId: String) = repo.getPaymentDetail(ledgerId)
}
