package com.example.daytoday

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.web_view.*


class WebViewDialog(private val url: String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.web_view, container, false)
    }

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        cancel.setOnClickListener {
            dismiss()
        }

        webView.setInitialScale(200)
        webView.settings.setSupportZoom(true)
        webView.settings.loadWithOverviewMode = true
        webView.settings.builtInZoomControls = true
        webView.addJavascriptInterface(Any(), "myJavaScript")
        webView.loadUrl(url)
        webView.webViewClient = WebViewClient()
    }
}