package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository

class GetCreditNoteDetailUseCase @Inject constructor(
    val repo: ILedgerRepository
) {
    suspend operator fun invoke(ledgerId: String) = repo.getCreditNoteDetail(ledgerId)

    suspend fun getCreditNoteDetails(ledgerId: String) = repo.getCreditNoteDetails(ledgerId)
}
