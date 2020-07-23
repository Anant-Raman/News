package com.example.newsapp.ui.saved

import android.app.Activity
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.Article
import com.example.newsapp.core.Constants
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

    fun deleteNews(article: Article){
        MainApplication.getExecutors()?.diskIO()
            ?.execute {
                repository.delete(article)
            }
    }

    fun deleteAll(){
        MainApplication.getExecutors()?.diskIO()
            ?.execute {
                repository.deleteAll()
            }
    }

    fun canAuthenticate(biometricManager: BiometricManager): Boolean {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(Constants.TAG, Constants.CAN_AUTHENTICATE)
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e(Constants.TAG, Constants.NO_BIOMETRICS_ON_DEVICE)
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e(Constants.TAG, Constants.BIOMETRICS_UNAVAILABLE)
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e(
                    Constants.TAG, Constants.NOT_ASSOCIATED_BIOMETRICS
                )
        }
        return false
    }
}