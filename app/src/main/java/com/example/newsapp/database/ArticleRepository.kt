package com.example.newsapp.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.news.data.Article
import com.example.newsapp.database.dao.ArticleDao

class ArticleRepository(private val articleDao: ArticleDao) {

    val articleList: LiveData<List<Article>> = articleDao.getArticles()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insert(article: Article) {
        articleDao.insert(article)
    }

    fun delete(article: Article) {
        articleDao.delete(article)
    }

    fun deleteAll() {
        articleDao.deleteAll()
    }
}