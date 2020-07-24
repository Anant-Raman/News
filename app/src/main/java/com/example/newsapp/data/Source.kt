package com.example.news.data

import com.google.gson.annotations.SerializedName

class Source(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)
