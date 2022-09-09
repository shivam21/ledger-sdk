package com.dehaat.ledger

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerParentApp
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.callbacks.LedgerCallBack
import lib.dehaat.ledger.presentation.ledger.LedgerDetailActivity

class AppChooserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dummy(
                onClickDBAButton = { openDBA() },
                onClickAIMSButton = { openAIMS() }
            )
        }
    }

    private fun openDBA() {
        LedgerSDK.init(
            applicationContext,
            LedgerParentApp.DBA(
                ledgerCallBack = LedgerCallBack(
                    onClickPayNow = { showToast(it.toString()) },
                    onRevampPayNowClick = {
                        showToast("summaryViewData?.minInterestAmountDue ${it.toString()}")
                    },
                    onDownloadInvoiceSuccess = { showToast(it.toString()) },
                    onPaymentOptionsClick = { resultLauncher ->
                        showToast(resultLauncher.toString())
                    },
                    downloadInvoiceIntent = { context, path ->
                        PendingIntent.getActivity(
                            this,
                            0,
                            Intent(
                                this,
                                LedgerDetailActivity::class.java
                            ).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) },
                            -PendingIntent.FLAG_ONE_SHOT
                        )
                    },
                    exceptionHandler = {}
                )
            ),
            bucket = "fnfsandboxec2odoo",
            appIcon = R.drawable.ic_info_icon,
            debugMode = true
        )

        try {
            LedgerSDK.openLedger(
                context = this,
                partnerId = "123456",
                dcName = "DC DBA",
                isDCFinanced = true,
                language = "en"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openAIMS() {
        LedgerSDK.init(
            applicationContext,
            LedgerParentApp.AIMS(
                downloadInvoiceClick = { showToast(it.toString()) },
                downloadInvoiceIntent = { context, path ->
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(
                            this,
                            LedgerDetailActivity::class.java
                        ).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) },
                        -PendingIntent.FLAG_ONE_SHOT
                    )
                },
                exceptionHandler = {}
            ),
            bucket = "fnfsandboxec2odoo",
            appIcon = R.drawable.ic_info_icon,
            debugMode = true
        )
        try {
            LedgerSDK.openLedger(
                context = this,
                partnerId = "123456",
                dcName = "DC AIMS",
                isDCFinanced = true,
                language = "hi"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}

@Composable
fun Dummy(onClickDBAButton: () -> Unit, onClickAIMSButton: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .clickable {
                    onClickDBAButton()
                }
                .padding(top = 16.dp)
                .background(shape = RoundedCornerShape(40.dp), color = Color(0xFF27AE60))
                .padding(vertical = 16.dp, horizontal = 40.dp),
            text = "Ledger for DBA",
            color = Color.White,
            maxLines = 1
        )

        Text(
            modifier = Modifier
                .clickable {
                    onClickAIMSButton()
                }
                .padding(top = 16.dp)
                .background(shape = RoundedCornerShape(40.dp), color = Color(0xFF4749A0))
                .padding(vertical = 16.dp, horizontal = 40.dp),
            text = "Ledger for AIMS",
            color = Color.White,
            maxLines = 1
        )
    }
}
