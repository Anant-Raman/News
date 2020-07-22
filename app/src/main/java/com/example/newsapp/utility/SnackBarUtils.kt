package com.example.newsapp.utility

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.example.newsapp.R
import com.google.android.material.snackbar.Snackbar

object SnackBarUtils {

    fun showSnackBar(
        msg: String?,
        binding: ViewDataBinding?,
        context: Context?
    ) {
        if (binding == null || context == null) return
        val snackbar = Snackbar
            .make(binding.root, msg!!, Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(context.resources.getColor(R.color.colorWhite))
        snackbar.show()
    }

    fun showSnackBar(
        msg: String?,
        view: View?,
        context: Context?
    ) {
        if (view == null || context == null) return
        val snackbar = Snackbar
            .make(view, msg!!, Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(context.resources.getColor(R.color.colorWhite))
        snackbar.show()
    }
}