package com.dehaat.ledger

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.LedgerParentApp
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.callbacks.LedgerCallbacks
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.detail.invoice.InvoiceDetailDataViewData

class AppChooserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dummy(
                onClickDBAButton = {
                    LedgerSDK.init(applicationContext, LedgerParentApp.DBA(ledgerCallBack = object : LedgerCallbacks{
                        override fun onClickPayNow(creditSummaryViewData: CreditSummaryViewData?) {
                            showToast(creditSummaryViewData.toString())
                        }



                        override fun onClickDownloadInvoice(invoiceDetailDataViewData: InvoiceDetailDataViewData?) {
                            showToast(invoiceDetailDataViewData.toString())
                        }
                    }))
                    LedgerSDK.openLedger(this, "123456", dcName = "DC DBA")
                },
                onClickAIMSButton = {
                    LedgerSDK.init(applicationContext, LedgerParentApp.AIMS())
                    LedgerSDK.openLedger(this, "123456", dcName = "DC AIMS")
                })
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