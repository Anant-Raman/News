package com.example.newsapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.Article
import com.example.news.data.NewsData
import com.example.newsapp.core.MainApplication
import com.example.newsapp.database.ArticleRepository
import com.example.newsapp.database.ArticleRoomDatabase
import com.example.newsapp.network.IViewApiListener
import com.example.newsapp.network.RestApiService

class SearchViewModel : ViewModel(), IViewApiListener {

    var searchResult: MutableLiveData<NewsData> = MutableLiveData()
    var state: MutableLiveData<String> = MutableLiveData()
    private val repository: ArticleRepository

    init {
        val articleDao =
            ArticleRoomDatabase.getDatabase(MainApplication.applicationContext()).articleDao()
        repository = ArticleRepository(articleDao)
    }

    fun fetchSearchResult(searchKey: String, page: Int, sortBy: String, language: String) {
        val service = RestApiService()
        service.getSearchResult(searchKey, page, sortBy, language, this, state)
    }

    fun saveNews(article: Article) {
        MainApplication.getExecutors()?.diskIO()
            ?.execute {
                repository.insert(article)
            }
    }

    fun initSortList(): ArrayList<String> {
        val sortByList = arrayListOf<String>()
        sortByList.add("publishedAt")
        sortByList.add("relevancy")
        sortByList.add("popularity")
        return sortByList
    }

    fun initSortListLabel(): ArrayList<String> {

        val sortByListLabel = arrayListOf<String>()
        sortByListLabel.add("Publication Date")
        sortByListLabel.add("Relevance")
        sortByListLabel.add("Popularity")
        return sortByListLabel
    }

    fun initLanguageList(): ArrayList<String> {

        val languageList = arrayListOf<String>()
        languageList.add("en")
        languageList.add("en")
        languageList.add("ar")
        languageList.add("de")
        languageList.add("es")
        languageList.add("fr")
        languageList.add("it")
        languageList.add("nl")
        languageList.add("no")
        languageList.add("pt")
        languageList.add("ru")
        languageList.add("zh")

        return languageList
    }

    fun initLanguageLabelList(): ArrayList<String> {

        val languageLabelList = arrayListOf<String>()
        languageLabelList.add("All")
        languageLabelList.add("English")
        languageLabelList.add("Arabic")
        languageLabelList.add("German")
        languageLabelList.add("Spanish")
        languageLabelList.add("French")
        languageLabelList.add("Italian")
        languageLabelList.add("Dutch")
        languageLabelList.add("Norwegian")
        languageLabelList.add("Portuguese")
        languageLabelList.add("Russian")
        languageLabelList.add("Chinese")

        return languageLabelList
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