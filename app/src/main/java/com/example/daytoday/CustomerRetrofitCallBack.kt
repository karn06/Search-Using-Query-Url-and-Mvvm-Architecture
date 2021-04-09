package com.example.daytoday

import android.widget.Toast
import com.example.daytoday.model.Wiki
import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

open class CustomerRetrofitCallBack<T> : Callback<Wiki> {

    private val retrofit: Retrofit? = null


    private fun bodyToString(request: RequestBody?): String? {
        return try {
            val buffer = Buffer()
            if (request != null) request.writeTo(buffer) else return ""
            buffer.readUtf8()
        } catch (e: IOException) {
            ""
        }
    }

    protected fun handleError(response: Response<Wiki>, request: Request?) {
        try {
            if (response.errorBody() != null) {
                val errorBody = response.errorBody()!!.string()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onResponse(call: Call<Wiki>, response: Response<Wiki>) {
        if (!response.isSuccessful) {
            handleError(response, call.request())
        }
    }

    override fun onFailure(call: Call<Wiki>, t: Throwable) {
    }
}