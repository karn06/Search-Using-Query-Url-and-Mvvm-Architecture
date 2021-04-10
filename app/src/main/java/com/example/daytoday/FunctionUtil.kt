package com.example.daytoday

import android.app.Activity
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity


object FunctionUtil {

    fun isInternetOn(activity: Activity): Boolean {
        val connec = activity.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        var b = false
        val activeNetwork = connec.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                b = true
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                b = true
        } else
            b = false
        return b
    }
}
