package com.example.newsapp.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var activityWebViewBinding: ActivityWebViewBinding
    private lateinit var url: String
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWebViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        if (supportActionBar != null) supportActionBar!!.hide()

        webView = findViewById(R.id.web_view)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        initViews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {

        activityWebViewBinding.toolbarWebView.title = "News App"
        activityWebViewBinding.toolbarWebView.setOnClickListener {
            onBackPressed()
        }
        url = intent.getStringExtra("url")!!

        activityWebViewBinding.webView.settings.loadWithOverviewMode = true
        activityWebViewBinding.webView.settings.useWideViewPort = true
        activityWebViewBinding.webView.settings.javaScriptEnabled = true

        activityWebViewBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        activityWebViewBinding.webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (activityWebViewBinding.webView.canGoBack()) {
            activityWebViewBinding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}