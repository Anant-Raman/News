package com.example.newsapp.network

import com.example.news.data.NewsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {


    @GET("top-headlines")
    fun getHeadlines(
        @Query("country") country : String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String )
            : Call<NewsData>

    
    @GET("top-headlines")
    fun getHeadlinesByCategory(
        @Query("country") country : String,
        @Query("page") page: Int,
        @Query("category") category : String,
        @Query("apiKey") apiKey: String )
            : Call<NewsData>

    @GET("everything")
    fun getHeadSearchResult(
        @Query("qInTitle") searchKey : String,
        @Query("page") page: Int,
        @Query("sortBy") sortBy : String,
        @Query("language") language : String,
        @Query("apiKey") apiKey: String )
            : Call<NewsData>
}