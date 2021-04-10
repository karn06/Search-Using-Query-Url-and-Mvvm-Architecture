package com.example.daytoday

import android.app.Application
import com.example.daytoday.model.Wiki
import org.json.JSONObject

class Repository(private val dataSource: DataSource) {

    fun getList(application: Application, jsonObject: String, responseHandler: ResponseHandler<Result<Wiki>>) {
        dataSource.getList(jsonObject, application, responseHandler)
    }

}