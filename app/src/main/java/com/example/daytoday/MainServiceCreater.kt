package com.example.daytoday

import android.app.Application
import android.media.tv.TvContract.Channels.CONTENT_TYPE
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.HTTP
import java.util.concurrent.TimeUnit


object MainServiceCreater {

    private fun <S> createService(serviceClass: Class<S>, application: Application): S {
        /*  val httpClientBuilder = OkHttpClient.Builder()
          val httpLoggingInterceptor = HttpLoggingInterceptor()
          httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
          val okHttpClient = httpClientBuilder
              .connectTimeout(30, TimeUnit.SECONDS)
              .readTimeout(30, TimeUnit.SECONDS)
              .writeTimeout(30, TimeUnit.SECONDS)
          val retrofitBuilder = Retrofit.Builder()
              .baseUrl(ApiConstants.API)
              .addConverterFactory(GsonConverterFactory.create())
          val retrofit = retrofitBuilder.client(okHttpClient.build()).build()*/
        val httpClientBuilder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(httpLoggingInterceptor).addInterceptor(MainInterceptor())

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.API)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(serviceClass)
    }

    fun createService(application: Application): Api {
        return createService<Api>(Api::class.java, application)
    }
}