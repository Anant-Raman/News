package com.example.newsapp.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.Article
import com.example.news.data.NewsData
import com.example.newsapp.core.MainApplication
import com.example.newsapp.database.ArticleRepository
import com.example.newsapp.database.ArticleRoomDatabase
import com.example.newsapp.network.IViewApiListener
import com.example.newsapp.network.RestApiService

class CategoryViewModel : ViewModel(), IViewApiListener {

    var categoryHeadline: MutableLiveData<NewsData> = MutableLiveData()
    var state: MutableLiveData<String> = MutableLiveData()
    private val repository: ArticleRepository

    init {
        val articleDao =
            ArticleRoomDatabase.getDatabase(MainApplication.applicationContext()).articleDao()
        repository = ArticleRepository(articleDao)
    }

    fun fetchCategoryHeadline(country: String, page: Int, category: String) {
        val service = RestApiService()
        service.getHeadlinesByCategory(country, page, category, this, state)
    }

    fun saveNews(article: Article) {
        MainApplication.getExecutors()?.diskIO()
            ?.execute {
                repository.insert(article)
            }
    }

    fun initCategoryList(): ArrayList<String> {
        val categoryList = arrayListOf<String>()
        categoryList.add("Business")
        categoryList.add("Entertainment")
        categoryList.add("General")
        categoryList.add("Health")
        categoryList.add("Science")
        categoryList.add("Sports")
        categoryList.add("Technology")
        return categoryList
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