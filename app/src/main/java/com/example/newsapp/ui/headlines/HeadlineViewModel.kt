package com.example.newsapp.ui.headlines

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.Article
import com.example.news.data.NewsData
import com.example.newsapp.core.MainApplication
import com.example.newsapp.database.ArticleRepository
import com.example.newsapp.database.ArticleRoomDatabase
import com.example.newsapp.network.IViewApiListener
import com.example.newsapp.network.RestApiService

class HeadlineViewModel() : ViewModel(), IViewApiListener, Parcelable {

    var headline: MutableLiveData<NewsData> = MutableLiveData()
    var state: MutableLiveData<String> = MutableLiveData()
    private val repository: ArticleRepository

    constructor(parcel: Parcel) : this()

    init {
        val articleDao =
            ArticleRoomDatabase.getDatabase(MainApplication.applicationContext()).articleDao()
        repository = ArticleRepository(articleDao)
    }

    fun fetchHeadline(country: String, page: Int) {
        val service = RestApiService()
        service.getHeadlines(this, page, country, state)
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HeadlineViewModel> {
        override fun createFromParcel(parcel: Parcel): HeadlineViewModel {
            return HeadlineViewModel(parcel)
        }

        override fun newArray(size: Int): Array<HeadlineViewModel?> {
            return arrayOfNulls(size)
        }
    }
}