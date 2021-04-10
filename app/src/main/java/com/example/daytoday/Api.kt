package com.example.daytoday

import com.example.daytoday.model.Wiki
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface Api {

    @POST(ApiConstants.API_END_POINT)
    fun getDataList(
        @Query("action") action: String,
        @Query("format") format: String,
        @Query("prop") prop: String,
        @Query("titles") titles: String,
        @Query("formatversion") formatversion: String,
        @Query("redirects") redirects: Int,
        @Query("piprop") piprop: String,
        @Query("pithumbsize") pithumbsize: Int,
        @Query("pilimit") pilimit: Int,
        @Query("wbptterms") wbptterms: String,
        @Query("generator") generator: String,
        @Query("gpssearch") gpssearch: String,
        @Query("inprop") inprop: String
    ): Call<Wiki>
}