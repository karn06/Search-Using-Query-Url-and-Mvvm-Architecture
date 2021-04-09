package com.example.daytoday

import android.app.Application
import okhttp3.Interceptor
import okhttp3.Response

class MainInterceptor() : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()

        val response = chain.proceed(request)

        return response
    }

}
