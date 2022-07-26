package lib.dehaat.ledger.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHandler @Inject constructor(
    @ApplicationContext val context: Context
) {
    private lateinit var notificationManager: NotificationManager
    lateinit var notificationBuilder: NotificationCompat.Builder
        private set
    private val channelId = "DehaatLedger"
    private val notificationId = 126

    init {
        createNotificationManager()
        createNotificationChannel()
        createNotificationBuilder()
    }

    fun notifyBuilder() {
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun createNotificationManager() {
        notificationManager = context.getSystemService(NotificationManager::class.java)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "Invoice Download",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }

    private fun createNotificationBuilder() {
        notificationBuilder = with(
            NotificationCompat.Builder(context, channelId)
        ) {
            setContentTitle("Invoice Download")
            setAutoCancel(true)
            setContentText("Invoice download in progress")
            setOnlyAlertOnce(true)
            setProgress(100, 0, true)
            priority = NotificationCompat.PRIORITY_HIGH
            this
        }
    }
}
