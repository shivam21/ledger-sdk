package lib.dehaat.ledger.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.dehaat.androidbase.helper.showToast
import kotlinx.coroutines.flow.SharedFlow
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.common.UiEvent

@Composable
fun HandleAPIErrors(
    uiEvent: SharedFlow<UiEvent>
) {
    val errorMessage = stringResource(id = com.agridroid.baselib.R.string.tech_prob)
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        uiEvent.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    if (LedgerSDK.isDebug) {
                        context.showToast(event.message)
                    } else {
                        context.showToast(errorMessage)
                    }
                    LedgerSDK.currentApp.ledgerCallBack.exceptionHandler(Exception(event.message))
                }
                else -> Unit
            }
        }
    }
}

fun Modifier.clickableWithCorners(
    borderSize: Dp,
    backgroundColor: Color = Color.White,
    onClick: () -> Unit
) = this
    .background(shape = RoundedCornerShape(borderSize), color = backgroundColor)
    .clip(RoundedCornerShape(borderSize))
    .clickable(onClick = onClick)
