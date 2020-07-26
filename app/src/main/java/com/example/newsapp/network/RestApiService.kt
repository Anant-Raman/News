package com.example.newsapp.network

import androidx.lifecycle.MutableLiveData
import com.example.news.data.NewsData
import com.example.news.data.SourceData
import com.example.newsapp.core.Constants
import com.example.newsapp.core.MainApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    private var endpoints = MainApplication.getApplication().restApiBuilder

    fun getHeadlines(
        listener: IViewApiListener,
        page: Int,
        country: String,
        state: MutableLiveData<String>
    ) {

        state.postValue(Constants.STATUS_START)

        val apiService = endpoints.getNetworkService(RestApi::class.java)

        val call: Call<NewsData> = apiService.getHeadlines(country, page, Constants.API_KEY)


        call.enqueue(object : Callback<NewsData> {
            override fun onFailure(call: Call<NewsData>?, t: Throwable?) {
                state.postValue(Constants.STATUS_FAILED)
                listener.notifyViewOnFailure(t, 0)
            }

            override fun onResponse(call: Call<NewsData>?, response: Response<NewsData>?) {

                if (response!!.isSuccessful) {
                    state.postValue(Constants.STATUS_LOADED)
                    listener.notifyViewOnSuccess(response.body(), 0)
                } else {
                    state.postValue(Constants.STATUS_NODATA)

                }
            }
        })
    }

    fun getHeadlinesByCategory(
        country: String,
        page: Int,
        category: String,
        listener: IViewApiListener,
        state: MutableLiveData<String>
    ) {

        state.postValue(Constants.STATUS_START)

        val apiService = endpoints.getNetworkService(RestApi::class.java)

        val call: Call<NewsData> =
            apiService.getHeadlinesByCategory(country, page, category, Constants.API_KEY)


        call.enqueue(object : Callback<NewsData> {
            override fun onFailure(call: Call<NewsData>?, t: Throwable?) {
                state.postValue(Constants.STATUS_FAILED)
                listener.notifyViewOnFailure(t, 0)
            }

            override fun onResponse(call: Call<NewsData>?, response: Response<NewsData>?) {


                if (response!!.isSuccessful) {
                    state.postValue(Constants.STATUS_LOADED)
                    listener.notifyViewOnSuccess(response.body(), 0)
                } else {
                    state.postValue(Constants.STATUS_NODATA)

                }
            }
        })
    }

    fun getSearchResult(
        searchKey: String,
        page: Int,
        sortBy: String,
        language: String,
        listener: IViewApiListener,
        state: MutableLiveData<String>
    ) {

        state.postValue(Constants.STATUS_START)

        val apiService = endpoints.getNetworkService(RestApi::class.java)

        val call: Call<NewsData> =
            apiService.getHeadSearchResult(searchKey, page, sortBy, language, Constants.API_KEY)


        call.enqueue(object : Callback<NewsData> {
            override fun onFailure(call: Call<NewsData>?, t: Throwable?) {
                state.postValue(Constants.STATUS_FAILED)
                listener.notifyViewOnFailure(t, 0)
            }

            override fun onResponse(call: Call<NewsData>?, response: Response<NewsData>?) {


                if (response!!.isSuccessful) {
                    state.postValue(Constants.STATUS_LOADED)
                    listener.notifyViewOnSuccess(response.body(), 0)
                } else {
                    state.postValue(Constants.STATUS_NODATA)

                }
            }
        })
    }


    fun getAllSource(
        listener: IViewApiListener,
        state: MutableLiveData<String>
    ) {
        state.postValue(Constants.STATUS_START)

        val apiService = endpoints.getNetworkService(RestApi::class.java)

        val call: Call<SourceData> =
            apiService.getAllSource(Constants.API_KEY)


        call.enqueue(object : Callback<SourceData> {
            override fun onFailure(call: Call<SourceData>?, t: Throwable?) {
                state.postValue(Constants.STATUS_FAILED)
                listener.notifyViewOnFailure(t, 0)
            }

            override fun onResponse(call: Call<SourceData>?, response: Response<SourceData>?) {
                if (response!!.isSuccessful) {
                    state.postValue(Constants.STATUS_LOADED)
                    listener.notifyViewOnSuccess(response.body(), 0)
                } else {
                    state.postValue(Constants.STATUS_NODATA)
                }
            }
        })
    }

    fun getSourceByCountry(
        country: String,
        listener: IViewApiListener,
        state: MutableLiveData<String>
    ) {
        state.postValue(Constants.STATUS_START)

        val apiService = endpoints.getNetworkService(RestApi::class.java)

        val call: Call<SourceData> =
            apiService.getSourceByCountry(country, Constants.API_KEY)


        call.enqueue(object : Callback<SourceData> {
            override fun onFailure(call: Call<SourceData>?, t: Throwable?) {
                state.postValue(Constants.STATUS_FAILED)
                listener.notifyViewOnFailure(t, 0)
            }

            override fun onResponse(call: Call<SourceData>?, response: Response<SourceData>?) {
                if (response!!.isSuccessful) {
                    state.postValue(Constants.STATUS_LOADED)
                    listener.notifyViewOnSuccess(response.body(), 0)
                } else {
                    state.postValue(Constants.STATUS_NODATA)
                }
            }
        })
    }

    fun getSourceByCategory(
        category: String,
        listener: IViewApiListener,
        state: MutableLiveData<String>
    ) {
        state.postValue(Constants.STATUS_START)

        val apiService = endpoints.getNetworkService(RestApi::class.java)

        val call: Call<SourceData> =
            apiService.getSourceByCategory(category, Constants.API_KEY)


        call.enqueue(object : Callback<SourceData> {
            override fun onFailure(call: Call<SourceData>?, t: Throwable?) {
                state.postValue(Constants.STATUS_FAILED)
                listener.notifyViewOnFailure(t, 0)
            }

            override fun onResponse(call: Call<SourceData>?, response: Response<SourceData>?) {
                if (response!!.isSuccessful) {
                    state.postValue(Constants.STATUS_LOADED)
                    listener.notifyViewOnSuccess(response.body(), 0)
                } else {
                    state.postValue(Constants.STATUS_NODATA)
                }
            }
        })
    }
}




