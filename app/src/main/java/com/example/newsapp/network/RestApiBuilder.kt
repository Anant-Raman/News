package com.example.newsapp.network

import com.example.newsapp.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class RestApiBuilder {
    private  val CACHE_CONTROL = "Cache-Control"
    private val LOGIN = 0
    private  val HOME = 1
    private val REQUEST_TIMEOUT = 30

    val BASE_API = "https://newsapi.org/v2/"

    private val mBaseApiRetrofit: Retrofit = provideRetrofit(BASE_API)

    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideRestApiBuilder(): RestApiBuilder {
        return this
    }

    private fun provideOkHttpClient(): OkHttpClient? {
        val builder = OkHttpClient.Builder()
        builder
            .addInterceptor(provideCacheInterceptor())
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(provideHttpLoggingInterceptor())
        return builder.build()
    }

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        return httpLoggingInterceptor
    }

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val response = chain.proceed(chain.request())
            // re-write response header to force use of cache
            val cacheControl =
                CacheControl.Builder().maxAge(200, TimeUnit.DAYS)
                    .build()
            response.newBuilder().header(CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    fun <T> getNetworkService(service: Class<T>?): T {
        return mBaseApiRetrofit.create(service!!)
    }

//    fun <T> getNetworkService(service: Class<T>?, type: Int): T {
//        return when (type) {
//            LOGIN -> mBaseApiRetrofit.create(service!!)
//            else -> mBaseApiRetrofit.create(service!!)
//        }
//    }

}