package com.example.newsapp.ui.headlines.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.Article
import com.example.news.data.NewsData
import com.example.newsapp.core.MainApplication
import com.example.newsapp.database.ArticleRepository
import com.example.newsapp.database.ArticleRoomDatabase
import com.globallogic.sampleapp.framework.network.IViewApiListener
import com.globallogic.sampleapp.framework.network.RestApiService

class HeadlineViewModel : ViewModel(),IViewApiListener {

    var headline: MutableLiveData<NewsData> = MutableLiveData()
    var state : MutableLiveData<String> = MutableLiveData()
    private val repository: ArticleRepository

    init {
        val articleDao =
            ArticleRoomDatabase.getDatabase(MainApplication.applicationContext()).articleDao()
        repository = ArticleRepository(articleDao)
    }

    fun fetchHeadline(country : String) {
        val service = RestApiService()
        service.getHeadlines(this,country, state)
    }

    fun saveNews(article: Article) {
        MainApplication.getExecutors()?.diskIO()
            ?.execute {
                repository.insert(article)
            }
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