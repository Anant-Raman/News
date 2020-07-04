package com.example.newsapp.ui.headlines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.NewsData
import com.example.newsapp.core.Constants
import com.globallogic.sampleapp.framework.network.IViewApiListener
import com.globallogic.sampleapp.framework.network.RestApiService

class HeadlineViewModel : ViewModel(),IViewApiListener {

    var headline: MutableLiveData<NewsData> = MutableLiveData()
    var state : MutableLiveData<String> = MutableLiveData()

    fun fetchHeadline() {
        val service = RestApiService()
        service.getHeadlines(this, state)
    }

    override fun notifyViewOnSuccess(`object`: Any?, type: Int) {
        when (type) {
            0 -> {
                val response: NewsData = `object` as NewsData
                headline.postValue(response)
            }
        }
    }

    override fun notifyViewOnFailure(`object`: Any?, type: Int) {
        when (type) {
            0 -> {
                headline.postValue(null)
            }
        }
    }
}