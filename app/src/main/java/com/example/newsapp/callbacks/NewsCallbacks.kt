package com.example.newsapp.callbacks

interface NewsCallbacks {

    fun launchNewsWebView(position: Int)

    fun saveArticle(position: Int)
}