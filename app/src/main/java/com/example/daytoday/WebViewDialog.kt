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
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.daytoday.databinding.WebViewBinding
import kotlinx.android.synthetic.main.activity_main.*
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

    /*  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
      ): View? {
          bindingImpl = DataBindingUtil.inflate(
              LayoutInflater.from(requireContext()),
              R.layout.web_view,
              null,
              false
          )

          return bindingImpl
      }*/

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        bindingImpl.cancel.setOnClickListener {
            dismiss()
        }
    }

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    private fun initViews() {
        bindingImpl.cancel.setOnClickListener { v ->
            dismiss()
        }

//        WebSettings webSettings = binding.webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setBuiltInZoomControls(true);

//        WebSettings webSettings = binding.webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setBuiltInZoomControls(true);
        val webSettings = bindingImpl.webView.settings
        webSettings.javaScriptEnabled = true
        bindingImpl.webView.setInitialScale(200)
        bindingImpl.progressBar2.visibility = View.VISIBLE
        bindingImpl.webView.webViewClient = Browser(bindingImpl.progressBar2)
        bindingImpl.webView.settings.setSupportZoom(true)
        bindingImpl.webView.settings.loadWithOverviewMode = true
        bindingImpl.webView.settings.builtInZoomControls = true
        bindingImpl.webView.addJavascriptInterface(Any(), "myJavaScript")
        bindingImpl.webView.settings.loadsImagesAutomatically = true;
        bindingImpl.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        bindingImpl.webView.loadUrl(url)
        bindingImpl.webView.clearCache(true)

        bindingImpl.webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false
            }
        })

        bindingImpl.webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress == 100) {
                    bindingImpl.progressBar2.setVisibility(View.GONE)
                }
            }
        })
    }

    //class for opening webview
    class Browser(pd: ProgressBar) : WebViewClient() {
        var pd: ProgressBar = pd
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            view.loadUrl(url!!)
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            pd.visibility = View.GONE
        }

        override fun onReceivedError(
            view: WebView?,
            errorCode: Int, description: String, failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
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