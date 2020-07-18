package com.example.newsapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.news.data.Article

@Dao
interface ArticleDao {

    @androidx.room.Query("SELECT * from saved_article_table")
    fun getArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(article: Article?)

    @androidx.room.Query("DELETE FROM saved_article_table")
    suspend fun deleteAll()
}