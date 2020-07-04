package com.example.news.data

import com.google.gson.annotations.SerializedName

data class NewsData (
    @SerializedName("status")
    val status: String ,
    @SerializedName("totalResults")
    val totalResult: Int,
    @SerializedName("articles")
    var  articles :List<Article>
)

