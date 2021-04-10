package com.example.daytoday

import android.app.Application
import com.example.daytoday.model.Wiki

interface DataSource {

    fun getList(
        jsonObject: String,
        application: Application,
        responseHandler: ResponseHandler<Result<Wiki>>
    )

}