package lib.dehaat.ledger.initializer

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import com.facebook.drawee.backends.pipeline.Fresco
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.ledger.LedgerDetailActivity

object LedgerSDK {
    lateinit var currentApp: LedgerParentApp
    lateinit var bucket: String
    var appIcon: Int = 0
        private set

    fun init(context: Context, app: LedgerParentApp, bucket: String, @DrawableRes appIcon: Int) {
        currentApp = app
        this.bucket = bucket
        this.appIcon = appIcon
        Fresco.initialize(context)
    }

    fun isCurrentAppAvailable() = ::currentApp.isInitialized && ::bucket.isInitialized

    fun openLedger(context: Context, partnerId: String, dcName: String) {
        context.startActivity(
            Intent(context, LedgerDetailActivity::class.java).apply {
                this.putExtra(LedgerConstants.KEY_PARTNER_ID, partnerId)
                this.putExtra(LedgerConstants.KEY_DC_NAME, dcName)
            }
        )
    }

    val isDBA: Boolean
        get() = currentApp is LedgerParentApp.DBA

    val isAIMS: Boolean
        get() = currentApp is LedgerParentApp.AIMS
}