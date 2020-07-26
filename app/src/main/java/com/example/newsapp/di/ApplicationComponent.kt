package com.example.newsapp.di

import com.example.newsapp.core.MainApplication
import com.example.newsapp.network.RestApiBuilder
import dagger.Component

@Component(modules = [RestApiBuilder::class])
interface ApplicationComponent {

    fun injectApplication(myApplication: MainApplication)

}