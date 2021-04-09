package com.example.daytoday

import android.app.Application
import android.util.Log
import com.example.daytoday.model.Wiki
import retrofit2.Call
import retrofit2.Response
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Retrofit
import java.io.IOException

object DefaultDataSource : DataSource {

    override fun getList(
        title: String,
        application: Application,
        responseHandler: ResponseHandler<Result<Wiki>>
    ) {
        responseHandler.response(Result.Loading)
        val service = MainServiceCreater.createService(application)
        val call = service.getDataList(
            "query",
            "json",
            "pageimages|pageterms|info",
            title,
            "2",
            1,
            "thumbnail",
            50,
            "10",
            "description","prefixsearch",title,"url"
        )

        call.enqueue(object : CustomerRetrofitCallBack<Wiki>() {
            override fun onFailure(call: Call<Wiki>, t: Throwable) {
                super.onFailure(call, t)
                t.printStackTrace()
                responseHandler.response(Result.Error(Exception(exceptionErrors(t))))
                Log.d("Fail:getLeadDetails ", t.message.toString());
            }

            override fun onResponse(call: Call<Wiki>, response: Response<Wiki>) {
                super.onResponse(call, response)
                Log.d("khushi", response.body().toString())
                if (response.isSuccessful) {
                    responseHandler.response(Result.Success(response.body()!!))
                } else {
                    responseHandler.response(Result.Error(Exception("Data Not Found")))
                }
            }
        })

    }

    fun exceptionErrors(throwable: Throwable): String {
        if (throwable is IOException) {
            // A network or conversion error happened
            return "Network Error"
        }
        if (throwable is IllegalStateException) {
            // data parsing error
            return "Data Parsing Error"
        }
        // any other network call exception
        return "Please try again Later"
    }
}