package com.example.newsapp.network

import com.example.news.data.NewsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {


    @GET("top-headlines")
    fun getHeadlines(
        @Query("country") country : String,
        @Query("apiKey") apiKey: String )
            : Call<NewsData>

//    @GET("/w/api.php")
//    fun getSearch(
//        @Query("action") action: String,
//        @Query("format") format: String?,
//        @Query("list") list: String?,
//        @Query("srsearch") search: String?
//    ): Call<SearchModel>

     // https://reqres.in/api/users?page=2

    //https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=bhavesh,

}