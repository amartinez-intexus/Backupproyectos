package com.example.pocantelop

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.pocantelop.databinding.ActivityMainBinding
import fr.antelop.sdk.*
import fr.antelop.sdk.authentication.*
import fr.antelop.sdk.exception.WalletValidationException
import fr.antelop.sdk.ui.prompt.AntelopKeypadView
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(), CustomDialog.CustomDialogListener {

    private var walletManager: WalletManager? = null
    private lateinit var binding: ActivityMainBinding
    private val walletViewModel: WalletViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val walletManagerCallback: WalletManagerCallback = object : WalletManagerCallback {
            override fun onConnectionError(p0: AntelopError, p1: Any?) {
            }

            override fun onConnectionSuccess(wallet: Wallet, p1: Any?) {
                walletViewModel.wallet = wallet
                if (walletViewModel.passcode != null) {
                    walletViewModel.passcode = null
                    CustomDialog().show(supportFragmentManager, "MainActivity")
                }
                else
                    startWallet()
            }

            override fun onCredentialsRequired(reason: CustomerCredentialsRequiredReason, error: AntelopError?, callbackData: Any?) {
                try {
                    binding.keyPadLayout.visibility = View.VISIBLE
                    binding.keypad.initializeView(object: AntelopKeypadView.KeypadCallback {
                        override fun onPasscodeEntryDone(passcode: ByteArray?) {
                            walletViewModel.passcode = passcode!!
                            walletManager?.connect(null, CustomerAuthenticationPasscode(walletViewModel.passcode!!))
                        }

                        override fun onKeyPressed() {
                        }

                        override fun onExtraButtonPressed() {
                        }

                    })
                }
                catch (ex: WalletValidationException){
                    ex.printStackTrace()
                }
            }

            override fun onProvisioningRequired(p0: Any?) {
                val provisioningIntent = Intent(applicationContext, ProvisioningActivity::class.java)
                startActivity(provisioningIntent)
            }

            override fun onAsyncRequestSuccess(p0: AsyncRequestType, p1: Any?) {
                Toast.makeText(applicationContext, applicationContext.getString(R.string.provisioning_device_error), Toast.LENGTH_LONG).show()
            }

            override fun onAsyncRequestError(p0: AsyncRequestType, p1: AntelopError, p2: Any?) {
                Toast.makeText(applicationContext, applicationContext.getString(R.string.provisioning_device_error), Toast.LENGTH_LONG).show()
            }

            override fun onLocalAuthenticationSuccess(
                p0: CustomerAuthenticationMethodType,
                p1: Any?
            ) {
                Toast.makeText(applicationContext, applicationContext.getString(R.string.provisioning_device_error), Toast.LENGTH_LONG).show()
            }

            override fun onLocalAuthenticationError(
                p0: CustomerAuthenticationMethodType,
                p1: LocalAuthenticationErrorReason,
                p2: String?,
                p3: Any?
            ) {
                Toast.makeText(applicationContext, applicationContext.getString(R.string.provisioning_device_error), Toast.LENGTH_LONG).show()
            }
        }
        walletManager = WalletManager(this, walletManagerCallback, null);
    }

    override fun onResume() {
        super.onResume()
        walletManager!!.connect()
    }

    private fun startWallet() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
    }

    override fun onDialogPositiveClick(dialog: Dialog?, id: String) {
        registraWallet()
        walletViewModel.registrarWallet(applicationContext, id)
    }

    private fun registraWallet() {
        walletViewModel.registrarWalletMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            startWallet()
        }
    }
}