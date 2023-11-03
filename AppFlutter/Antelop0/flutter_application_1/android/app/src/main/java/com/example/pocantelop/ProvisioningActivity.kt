package com.example.pocantelop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.antelop.sdk.*

class ProvisioningActivity: AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback  {

    lateinit var walletProvisioning: WalletProvisioning

    override fun onResume() {
        super.onResume()
        val walletProvisioningCallback = object : WalletProvisioningCallback {
            override fun onProvisioningError(p0: AntelopError, p1: Any?) {
                MaterialAlertDialogBuilder(this@ProvisioningActivity)
                    .setMessage(this@ProvisioningActivity.getString(R.string.provisioning_error))
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            override fun onProvisioningSuccess(p0: Any?) {
                finish()
            }

            override fun onProvisioningPending(p0: Any?) {
            }

            override fun onInitializationError(p0: AntelopError, p1: Any?) {
            }

            override fun onInitializationSuccess(p0: Any?) {
                walletProvisioning.checkEligibility(true)
            }

            override fun onDeviceEligible(p0: Boolean, p1: MutableList<Product>, p2: Any?) {
                walletProvisioning.launch(null, null, "basic", null);
            }

            override fun onDeviceNotEligible(p0: EligibilityDenialReason, p1: Any?, p2: String?) {
                Toast.makeText(applicationContext, this@ProvisioningActivity.getString(R.string.provisioning_device_error), Toast.LENGTH_LONG).show()

            }

            override fun onCheckEligibilityError(p0: AntelopError, p1: Any?) {
                Toast.makeText(applicationContext, this@ProvisioningActivity.getString(R.string.provisioning_device_error), Toast.LENGTH_LONG).show()
            }


        }
        walletProvisioning = WalletProvisioning(this, walletProvisioningCallback, null)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        walletProvisioning.processPermissionsResult(requestCode, permissions, grantResults)
        walletProvisioning.launch(null, null, "basic", null)
    }

}