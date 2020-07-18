package com.example.newsapp.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.Article
import com.example.news.data.NewsData
import com.globallogic.sampleapp.framework.network.IViewApiListener
import com.globallogic.sampleapp.framework.network.RestApiService

class SearchViewModel : ViewModel(),IViewApiListener {

    var searchResult: MutableLiveData<NewsData> = MutableLiveData()
    var state: MutableLiveData<String> = MutableLiveData()

    fun fetchSearchResult(searchKey : String, sortBy : String, language : String) {
        Log.i("Anant", searchKey)
        val service = RestApiService()
        service.getSearchResult(searchKey, sortBy,language,this, state)
    }

    fun saveNews(article: Article){


    }

    override fun notifyViewOnSuccess(`object`: Any?, type: Int) {
        when (type) {
            0 -> {
                val response: NewsData = `object` as NewsData
                searchResult.postValue(response)
            }
        }
    }

    override fun notifyViewOnFailure(`object`: Any?, type: Int) {
        when (type) {
            0 -> {
                searchResult.postValue(null)
            }
        }
    }
}