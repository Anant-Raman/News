package com.example.newsapp.core

import android.app.Application
import android.content.Context
import com.example.newsapp.di.ApplicationComponent
import com.example.newsapp.di.DaggerApplicationComponent
import com.example.newsapp.network.RestApiBuilder
import javax.inject.Inject

class MainApplication : Application() {

    private var applicationComponent: ApplicationComponent? = null

    @Inject
    lateinit var restApiBuilder: RestApiBuilder

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...

        MainApplication.application = this
        applicationComponent = DaggerApplicationComponent.create()
        applicationComponent?.injectApplication(this)

        //val context: Context = MainApplication.applicationContext()
    }

    init {
        instance = this
        mAppExecutors = AppExecutors()
    }

    companion object {
        private var instance: MainApplication? = null
        private var mAppExecutors: AppExecutors? = null

        private lateinit var application: MainApplication
        fun getApplication(): MainApplication {
            return application
        }

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun getExecutors(): AppExecutors? {
            return mAppExecutors
        }
    }
}