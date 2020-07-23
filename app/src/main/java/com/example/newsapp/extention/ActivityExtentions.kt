package com.example.newsapp.extention

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.newsapp.R
import com.example.newsapp.core.Constants
import com.example.newsapp.ui.bottomsheet.SettingBottomSheet

fun Activity.showBottomSheet(
    fragmentManager: FragmentManager
) {
    val bottomSheet = SettingBottomSheet()
    bottomSheet.show(fragmentManager, Constants.BOTTOMSHEET_EX)
}

fun Activity.showDialog(
    dialogTitle: String, dialogMessage: String,
    positiveButtonText: String, NegativeButtonText: String,
    onCancelButtonClick: () -> Unit, onContinueClick: () -> Unit
) {
    val builder = AlertDialog.Builder(this)
    //set title for alert dialog
    builder.setTitle(dialogTitle)
    //set message for alert dialog
    builder.setMessage(dialogMessage)
    //performing positive action
    builder.setPositiveButton(positiveButtonText) { dialogInterface, _ ->
        onContinueClick()
        dialogInterface.dismiss()
    }
    //performing cancel action
    builder.setNegativeButton(NegativeButtonText) { dialogInterface, _ ->
        onCancelButtonClick()
        dialogInterface.cancel()
    }
    // Create the AlertDialog
    val alertDialog: AlertDialog = builder.create()
    // Set other dialog properties
    alertDialog.setCancelable(false)
    alertDialog.show()
}

fun Activity.showBiometric(
    fragmentActivity: FragmentActivity,
    context: Context,
    onAuthenticated: () -> Unit,
    onAuthenticationCancelled: () -> Unit
) {

    val executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = BiometricPrompt(
        fragmentActivity, executor,
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                onAuthenticationCancelled()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                onAuthenticated()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(getString(R.string.confirm_fingerprint))
        .setNegativeButtonText(getString(R.string.cancel))
        .build()

    biometricPrompt.authenticate(promptInfo)
}
