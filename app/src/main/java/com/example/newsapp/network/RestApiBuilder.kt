package com.example.newsapp.network

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RestApiBuilder {

    val BASE_API = "https://newsapi.org/v2/"

    private val mBaseApiRetrofit: Retrofit = provideRetrofit(BASE_API)

    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideRestApiBuilder(): RestApiBuilder {
        return this
    }

    fun <T> getNetworkService(service: Class<T>?): T {
        return mBaseApiRetrofit.create(service!!)
    }
}