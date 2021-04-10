package com.example.daytoday

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.daytoday.databinding.WebViewBinding
import kotlinx.android.synthetic.main.web_view.*


class WebViewDialog(private val url: String) : DialogFragment() {

    lateinit var bindingImpl: WebViewBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        bindingImpl = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.web_view,
            null,
            false
        )
        builder.setView(bindingImpl.getRoot())
        bindingImpl.lifecycleOwner = this
        initViews()
        return builder.create()
    }

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    private fun initViews() {
        bindingImpl.cancel.setOnClickListener { v ->
            dismiss()
        }


        val webSettings = bindingImpl.webView.settings
        webSettings.javaScriptEnabled = true
        bindingImpl.webView.setInitialScale(200)
        bindingImpl.progressBar2.visibility = View.VISIBLE
        bindingImpl.webView.settings.setSupportZoom(true)
        bindingImpl.webView.settings.loadWithOverviewMode = true
        bindingImpl.webView.settings.builtInZoomControls = true
        bindingImpl.webView.addJavascriptInterface(Any(), "myJavaScript")
        bindingImpl.webView.settings.loadsImagesAutomatically = true;
        bindingImpl.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        bindingImpl.webView.loadUrl(url)
        bindingImpl.webView.clearCache(true)

        bindingImpl.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false
            }
        }

        bindingImpl.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress == 100) {
                    bindingImpl.progressBar2.visibility = View.GONE
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
}