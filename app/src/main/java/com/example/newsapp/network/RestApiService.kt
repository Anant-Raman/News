package com.globallogic.sampleapp.framework.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.news.data.NewsData
import com.example.newsapp.core.Constants
import com.example.newsapp.network.RestApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {

    var endpoints: RestApiBuilder = RestApiBuilder()

    fun getHeadlines(
        listener: IViewApiListener,
        state: MutableLiveData<String>
    ) {

        state.postValue(Constants.STATUS_START)

        val apiService = endpoints.getNetworkService(RestApi::class.java, 0)

        val call: Call<NewsData> = apiService.getHeadlines("in", "8d4a8a814d09473eb8d672ab8bcea034")


        call.enqueue(object : Callback<NewsData> {
            override fun onFailure(call: Call<NewsData>?, t: Throwable?) {
                state.postValue(Constants.STATUS_FAILED)
                Log.i("Anant", t!!.message.toString())
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
//    fun getSearchResult(
//        listener: IViewApiListener,
//        serachStr : String
//    ){
//
//        val apiService = endpoints.getNetworkService(RestApi::class.java,1)
//
//        val call : Call<SearchModel> = apiService.getSearch("query","json","search",serachStr)
//
//        call.enqueue(object : Callback<SearchModel>{
//            override fun onFailure(call: Call<SearchModel>?, t: Throwable?) {
//                listener.notifyViewOnFailure(t,1)
//            }
//            override fun onResponse(call: Call<SearchModel>?, response: Response<SearchModel>?) {
//
//                if (response!!.isSuccessful) {
//                    listener.notifyViewOnSuccess(response.body(),1)
//                }
//            }
//        })
//    }
}




