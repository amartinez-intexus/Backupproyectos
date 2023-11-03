package com.example.pocantelop.core.services

import android.content.Context
import android.util.Log
import fr.antelop.sdk.*
import fr.antelop.sdk.authentication.CustomerAuthenticationMethodType
import fr.antelop.sdk.authentication.CustomerCredentialsRequiredReason
import fr.antelop.sdk.authentication.LocalAuthenticationErrorReason
import fr.antelop.sdk.exception.WalletValidationException
import fr.antelop.sdk.transaction.hce.DefaultHceTransactionListener
import fr.antelop.sdk.transaction.hce.HceTransaction


class HceTransactionListener(): DefaultHceTransactionListener() {

    private var walletManager: WalletManager? = null
    private var isWalletManagerConnected = false
    private val walletManagerCallback = object : WalletManagerCallback {
        override fun onConnectionError(p0: AntelopError, p1: Any?) {}

        override fun onConnectionSuccess(p0: Wallet, p1: Any?) {
            isWalletManagerConnected   = true
        }

        override fun onCredentialsRequired(p0: CustomerCredentialsRequiredReason, p1: AntelopError?, p2: Any?) {
        }

        override fun onProvisioningRequired(p0: Any?) {
        }

        override fun onAsyncRequestSuccess(p0: AsyncRequestType, p1: Any?) {
        }

        override fun onAsyncRequestError(p0: AsyncRequestType, p1: AntelopError, p2: Any?) {
        }

        override fun onLocalAuthenticationSuccess(p0: CustomerAuthenticationMethodType, p1: Any?) {
        }

        override fun onLocalAuthenticationError(p0: CustomerAuthenticationMethodType, p1: LocalAuthenticationErrorReason, p2: String?, p3: Any?) {
        }

    }

    override fun onTransactionStart(context: Context) {
        try {
            isWalletManagerConnected = false
            walletManager = WalletManager(context, walletManagerCallback, null)
            walletManager!!.connect()
        } catch (ex: WalletValidationException) {
            Log.e("Transaction error", ex.message)
        }
    }

    override fun onTransactionError(p0: Context, p1: AntelopError) {
        super.onTransactionError(p0, p1)
        Log.e("Transaction error", p1.message)
    }

    override fun onTransactionDone(p0: Context, p1: HceTransaction) {
        super.onTransactionDone(p0, p1)
        Log.e("Transaction Done", p1.id)
    }
}