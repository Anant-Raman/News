package com.example.news.data

import com.google.gson.annotations.SerializedName

data class SourceData(
    @SerializedName("sources")
    var sourcesList: List<SourcesList>,
    @SerializedName("status")
    var status: String?
) {
    data class SourcesList(
        @SerializedName("category")
        var category: String?,
        @SerializedName("country")
        var country: String?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("language")
        var language: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("url")
        var url: String?
    )
}