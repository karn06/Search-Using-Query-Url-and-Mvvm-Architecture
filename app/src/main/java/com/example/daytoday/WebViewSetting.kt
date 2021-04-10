package com.example.daytoday

import android.webkit.WebSettings
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class WebViewSetting {
    private val WEBVIEW_LINK = "https://docs.google.com/gview?embedded=true&url="
    private val DRIVE_LINK = "http://drive.google.com/viewerng/viewer?embedded=true&url="

    //Encoder is used to remove all the special characters
    val ENCODER = "ISO-8859-1"

    fun setWebSettings(webSettings: WebSettings) {
        webSettings.javaScriptEnabled = true
        webSettings.builtInZoomControls = true
        webSettings.setSupportZoom(true)
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.setAppCacheEnabled(false)
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
    }

    fun getWebViewUrlEncoded(url: String): String? {
        return try {
            WEBVIEW_LINK + URLEncoder.encode(url, ENCODER)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            getWebViewUrl(url)
        }
    }

    fun getWebViewUrl(url: String): String? {
        return WEBVIEW_LINK + url
    }

    fun getWebviewDriveLink(url: String): String? {
        return DRIVE_LINK + url
    }
}