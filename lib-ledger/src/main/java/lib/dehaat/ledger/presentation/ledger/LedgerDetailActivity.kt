package lib.dehaat.ledger.presentation.ledger

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.amazonaws.mobile.client.AWSMobileClient
import com.dehaat.androidbase.helper.showToast
import dagger.hilt.android.AndroidEntryPoint
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.navigation.LedgerNavigation
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.util.NotificationHandler
import javax.inject.Inject

@AndroidEntryPoint
class LedgerDetailActivity : ComponentActivity() {

    val viewModel: LedgerDetailViewModel by viewModels()

    @Inject
    lateinit var notificationHandler: NotificationHandler

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it?.resultCode == Activity.RESULT_OK) {
            viewModel.getLedgerData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!LedgerSDK.isCurrentAppAvailable()) {
            showToast("Please initialize Ledger SDK with current app.")
            finish()
            return
        }

        val partnerId = intent.getStringExtra(LedgerConstants.KEY_PARTNER_ID)
        val dcName = intent.getStringExtra(LedgerConstants.KEY_DC_NAME)
        AWSMobileClient.getInstance().initialize(this).execute()
        partnerId?.let { partner ->
            setContent {
                LedgerTheme {
                    LedgerNavigation(
                        dcName = dcName ?: "Ledger",
                        partnerId = partner,
                        ledgerColors = LedgerSDK.currentApp.ledgerColors,
                        resultLauncher = resultLauncher,
                        finishActivity = { finish() },
                        viewModel = viewModel,
                        ledgerCallbacks = LedgerSDK.currentApp.ledgerCallBack,
                        onDownloadClick = {
                            val callBacks = LedgerSDK.currentApp.ledgerCallBack
                            callBacks.onClickDownloadInvoice(it)
                            if (it.isFailed) {
                                showToast("Technical problem occurred, try again after some time")
                            } else {
                                notificationHandler.apply {
                                    if (it.progressData.bytesCurrent == 100) {
                                        notificationBuilder.setContentText("Invoice downloaded successfully")
                                        notificationBuilder.setContentIntent(
                                            callBacks.downloadInvoiceIntent.invoke(
                                                this@LedgerDetailActivity,
                                                it.filePath
                                            )
                                        )
                                        showToast("Invoice downloaded successfully")
                                    } else {
                                        notificationBuilder.setContentText("Invoice download in progress")
                                    }
                                    notificationBuilder.setProgress(
                                        it.progressData.bytesTotal,
                                        it.progressData.bytesCurrent,
                                        false
                                    )
                                    notifyBuilder()
                                }
                            }
                        }
                    )
                }
            }
        } ?: run {
            showToast("Partner Id is missing, Please provide partner id.")
            finish()
            return
        }
    }
}
