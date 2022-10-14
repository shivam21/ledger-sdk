package lib.dehaat.ledger.initializer

import android.content.Context
import android.os.Environment
import androidx.annotation.DrawableRes
import com.facebook.drawee.backends.pipeline.Fresco
import java.io.File
import lib.dehaat.ledger.presentation.ledger.LedgerDetailFragment

object LedgerSDK {
    internal lateinit var currentApp: LedgerParentApp
    internal lateinit var bucket: String
    internal var locale: String = "en"
    internal var appIcon: Int = 0
        private set
    val isDBA: Boolean
        get() = currentApp is LedgerParentApp.DBA
    var isDebug: Boolean = false
        private set

    fun init(
        context: Context,
        app: LedgerParentApp,
        bucket: String,
        @DrawableRes appIcon: Int,
        debugMode: Boolean,
        language: String? = null
    ) {
        currentApp = app
        this.bucket = bucket
        this.appIcon = appIcon
        this.isDebug = debugMode
        language?.let { lang -> locale = lang }
        Fresco.initialize(context)
    }

    fun isCurrentAppAvailable() = ::currentApp.isInitialized && ::bucket.isInitialized

    @Throws(Exception::class)
    fun getLedgerFragment(
        partnerId: String,
        dcName: String,
        isDCFinanced: Boolean
    ) = if (isCurrentAppAvailable()) {
        LedgerDetailFragment.Companion.Args(
            partnerId = partnerId,
            dcName = dcName,
            isDCFinanced = isDCFinanced
        ).build()
    } else {
        throw Exception("Ledger not initialised Exception")
    }

    fun getFile(context: Context): File? = try {
        File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS
            ),
            "DeHaat"
        ).apply { mkdir() }
    } catch (e: Exception) {
        if (::currentApp.isInitialized) {
            currentApp.ledgerCallBack.exceptionHandler(e)
        }
        null
    }
}
