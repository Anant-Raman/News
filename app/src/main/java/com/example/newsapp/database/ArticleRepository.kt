package com.example.newsapp.database

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.news.data.Article
import com.example.newsapp.database.dao.ArticleDao

class ArticleRepository(private val articleDao: ArticleDao) {

    val articleList : LiveData<List<Article>> = articleDao.getArticles()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insert(article : Article) {
        Log.i("Anant", article.title)
        articleDao.insert(article)
    }
}