package com.example.newsapp.core

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = MainApplication.applicationContext()
    }

    init {
        instance = this
        mAppExecutors = AppExecutors()
    }

    companion object {
        private var instance: MainApplication? = null
        private var mAppExecutors: AppExecutors? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
        fun getExecutors(): AppExecutors? {
            return mAppExecutors
        }
    }
}