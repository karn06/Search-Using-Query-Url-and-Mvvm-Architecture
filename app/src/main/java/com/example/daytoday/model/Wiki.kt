package com.example.daytoday.model

import com.google.gson.annotations.SerializedName

data class Wiki(
    @SerializedName("batchcomplete") val batchComplete: Boolean,
    @SerializedName("query") val query: Query
)

data class Query(
    @SerializedName("pages") val pages: ArrayList<Pages>,
)

data class Pages(
    @SerializedName("title") val title: String,
    @SerializedName("thumbnail") val thumbnail: ThumbNails,
    @SerializedName("terms") val terms: Terms,
    @SerializedName("fullurl") val fullurl: String,
)

data class ThumbNails(
    @SerializedName("source") val source: String
)

data class Terms(
    @SerializedName("description") val description: ArrayList<String>
)