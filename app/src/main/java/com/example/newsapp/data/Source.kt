package com.example.news.data

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

class Source (
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name : String?
)
