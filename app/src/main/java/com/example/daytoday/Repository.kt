package com.example.daytoday

import android.app.Application
import com.example.daytoday.model.Wiki

class Repository(private val dataSource: DataSource) {

    fun getList(
        application: Application,
        query: String,
        responseHandler: ResponseHandler<Result<Wiki>>
    ) {
        dataSource.getList(query, application, responseHandler)
    }

}