package lib.dehaat.ledger.presentation.ledger

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amazonaws.mobile.client.AWSMobileClient
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.navigation.LedgerNavigation
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.util.NotificationHandler

@AndroidEntryPoint
class LedgerDetailFragment : Fragment() {

    val viewModel: LedgerDetailViewModel by viewModels()

    private lateinit var args: Args

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
        args = getArgs(arguments)
        setLedgerLanguage()

        if (!LedgerSDK.isCurrentAppAvailable() && LedgerSDK.isDebug) {
            showToast(getString(R.string.initialise_ledger))
            finish()
            return
        }

        if (args.partnerId.isEmpty() && LedgerSDK.isDebug) {
            showToast("PartnerId missing (check dehaat-center-API)")
            finish()
            return
        }

        AWSMobileClient.getInstance().initialize(requireActivity()).execute()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            LedgerTheme {
                LedgerNavigation(
                    dcName = args.dcName,
                    partnerId = args.partnerId,
                    isDCFinanced = args.isDCFinanced,
                    ledgerColors = LedgerSDK.currentApp.ledgerColors,
                    resultLauncher = resultLauncher,
                    finishActivity = { finish() },
                    viewModel = viewModel,
                    ledgerCallbacks = LedgerSDK.currentApp.ledgerCallBack,
                    onDownloadClick = {
                        val ledgerCallbacks = LedgerSDK.currentApp.ledgerCallBack
                        notificationHandler.notificationBuilder.setSmallIcon(LedgerSDK.appIcon)
                        if (it.isFailed) {
                            showToast(getString(R.string.tech_problem))
                        } else {
                            notificationHandler.apply {
                                if (it.progressData.bytesCurrent == 100) {
                                    notificationBuilder.apply {
                                        setContentText(getString(R.string.invoice_download_success))
                                        setContentIntent(
                                            ledgerCallbacks.downloadInvoiceIntent.invoke(
                                                requireActivity(),
                                                it.filePath
                                            )
                                        )
                                    }
                                    ledgerCallbacks.onDownloadInvoiceSuccess(it)
                                    showToast(getString(R.string.invoice_download_success))
                                } else {
                                    notificationBuilder.setContentText(getString(R.string.invoice_download_in_progress))
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
    }

    private fun finish() = requireActivity().supportFragmentManager.popBackStack()

    private fun showToast(message: String) = Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()

    private fun setLedgerLanguage() {
        val locale = Locale(LedgerSDK.locale)
        val res = this.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
        Locale.setDefault(locale)
    }

    companion object {
        private const val KEY_DC_FINANCED = "KEY_DC_FINANCED"

        fun getArgs(intent: Bundle?) = Args(
            partnerId = intent?.getString(LedgerConstants.KEY_PARTNER_ID) ?: "",
            dcName = intent?.getString(LedgerConstants.KEY_DC_NAME) ?: "",
            isDCFinanced = intent?.getBoolean(KEY_DC_FINANCED) ?: false
        )

        data class Args(
            val partnerId: String,
            val dcName: String,
            val isDCFinanced: Boolean
        ) {
            fun build() = LedgerDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(LedgerConstants.KEY_PARTNER_ID, partnerId)
                    putString(LedgerConstants.KEY_DC_NAME, dcName)
                    putBoolean(KEY_DC_FINANCED, isDCFinanced)
                }
            }
        }
    }
}
