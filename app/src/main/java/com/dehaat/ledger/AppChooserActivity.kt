package com.dehaat.ledger

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerParentApp
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.callbacks.LedgerCallBack
import lib.dehaat.ledger.navigation.navigateTo
import lib.dehaat.ledger.presentation.ledger.LedgerDetailFragment

@AndroidEntryPoint
class AppChooserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = LEDGER_SELECTOR_ROUTE
            ) {
                composable(LEDGER_SELECTOR_ROUTE) {
                    Dummy(
                        onClickDBAButton = { openDBA(navController) },
                        onClickAIMSButton = { openAIMS(navController) }
                    )
                }
                composable(LEDGER_SCREEN_ROUTE) {
                    FragmentContainer(
                        modifier = Modifier,
                        fragmentManager = supportFragmentManager,
                        commit = {
                            add(
                                it,
                                LedgerSDK.getLedgerFragment(
                                    partnerId = "123456",
                                    dcName = "DC DBA",
                                    isDCFinanced = true
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    private fun openDBA(navController: NavHostController) {
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
                                LedgerDetailFragment::class.java
                            ).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) },
                            -PendingIntent.FLAG_ONE_SHOT
                        )
                    },
                    exceptionHandler = {
                        Log.d("LEDGER_ERROR_LOGGER", "${it.localizedMessage}")
                    },
                    firebaseScreenLogger = { _, route ->
                        Log.d("TAG", "openDBA: $route")
                    }
                )
            ),
            bucket = "fnfsandboxec2odoo",
            appIcon = R.drawable.ic_info_icon,
            debugMode = true
        )

        try {
            navController.navigateTo(LEDGER_SCREEN_ROUTE, bundleOf())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openAIMS(navController: NavHostController) {
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
                            LedgerDetailFragment::class.java
                        ).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) },
                        -PendingIntent.FLAG_ONE_SHOT
                    )
                },
                exceptionHandler = {
                    Log.d("LEDGER_ERROR_LOGGER", "${it.localizedMessage}")
                },
                firebaseScreenLogger = { _, route ->
                    Log.d("TAG", "openAIMS: $route")
                }
            ),
            bucket = "fnfsandboxec2odoo",
            appIcon = R.drawable.ic_info_icon,
            debugMode = true
        )
        try {
            navController.navigateTo(LEDGER_SCREEN_ROUTE, bundleOf())
            LedgerSDK.getLedgerFragment(
                partnerId = "123456",
                dcName = "DC AIMS",
                isDCFinanced = true
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val LEDGER_SELECTOR_ROUTE = "ledger_selector"
        const val LEDGER_SCREEN_ROUTE = "ledger_screen"
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

@Composable
fun FragmentContainer(
    modifier: Modifier,
    fragmentManager: FragmentManager,
    commit: FragmentTransaction.(containerId: Int) -> Unit
) {
    val containerId by remember { mutableStateOf(View.generateViewId()) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            fragmentManager.findFragmentById(containerId)?.view
                ?.also { (it.parent as? ViewGroup)?.removeView(it) }
                ?: FragmentContainerView(context)
                    .apply { id = containerId }
                    .also {
                        fragmentManager.commit { commit(it.id) }
                    }
        }
    )
}
