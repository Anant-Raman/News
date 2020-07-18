package com.example.news.data

import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "saved_article_table")

data class Article(
    @Embedded(prefix = "source")
    val source: Source?,

    @SerializedName("author")
    val author: String?,

    @PrimaryKey
    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("description")
    @Expose
    val description : String?,

    @SerializedName("url")
    @Expose
    val url: String?,

    @SerializedName("urlToImage")
    @Expose
    val urlToImage: String?,

    @SerializedName("publishedAt")
    @Expose
    val publishedAt: String?,

    @SerializedName("content")
    @Expose
    val content: String?
)