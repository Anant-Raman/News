package com.example.newsapp.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.Article
import com.example.news.data.NewsData
import com.globallogic.sampleapp.framework.network.IViewApiListener
import com.globallogic.sampleapp.framework.network.RestApiService

class CategoryViewModel : ViewModel(),IViewApiListener {

    var categoryHeadline: MutableLiveData<NewsData> = MutableLiveData()
    var state : MutableLiveData<String> = MutableLiveData()

    fun fetchCategoryHeadline(country: String, category: String) {
        val service = RestApiService()
        service.getHeadlinesByCategory(country ,category,this, state)
    }

    fun saveNews(article: Article){


    }

    override fun notifyViewOnSuccess(`object`: Any?, type: Int) {
        when (type) {
            0 -> {
                val response: NewsData = `object` as NewsData
                categoryHeadline.postValue(response)
            }
        }
    }

    override fun notifyViewOnFailure(`object`: Any?, type: Int) {
        when (type) {
            0 -> {
                categoryHeadline.postValue(null)
            }
        }
    }
}