package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.domain.ILedgerRepository
import javax.inject.Inject

class GetABSTransactionsUseCase @Inject constructor(val repo: ILedgerRepository) {
    suspend operator fun invoke(
        partnerId: String,
        limit: Int,
        offset: Int
    ) = repo.getABSTransactions(partnerId, limit, offset)
}
