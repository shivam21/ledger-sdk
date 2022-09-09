package lib.dehaat.ledger.presentation.ledger.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.dehaat.androidbase.helper.showToast
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.resources.textMedium16Sp

@Composable
fun NoDataFound(
    message: String? = null,
    onError: (Exception) -> Unit
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = "No Data found",
        style = textMedium16Sp()
    )
    val context = LocalContext.current
    message?.let {
        if (LedgerSDK.isDebug) {
            context.showToast(it)
        } else {
            context.showToast(R.string.tech_problem)
        }
        onError(Exception(it))
    }
}
