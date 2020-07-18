package com.example.newsapp.ui.saved

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.Article
import com.example.newsapp.core.MainApplication
import com.example.newsapp.database.ArticleRepository
import com.example.newsapp.database.ArticleRoomDatabase

class SavedNewsViewModel : ViewModel() {

    private val repository: ArticleRepository

    val articleList: LiveData<List<Article>>

    init {
        val articleDao =
            ArticleRoomDatabase.getDatabase(MainApplication.applicationContext()).articleDao()
        repository = ArticleRepository(articleDao)
        articleList = repository.articleList
    }


}