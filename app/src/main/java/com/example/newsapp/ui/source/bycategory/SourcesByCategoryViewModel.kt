package com.example.newsapp.ui.source.bycategory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.SourceData
import com.example.newsapp.network.IViewApiListener
import com.example.newsapp.network.RestApiService

class SourcesByCategoryViewModel : ViewModel(), IViewApiListener {

    var sources: MutableLiveData<SourceData?> = MutableLiveData()
    var state: MutableLiveData<String> = MutableLiveData()

    fun fetchSourcesByCountry(category: String) {
        val service = RestApiService()
        service.getSourceByCategory(category, this, state)
    }

    override fun notifyViewOnSuccess(`object`: Any?, type: Int) {
        when (type) {
            0 -> {
                val response: SourceData = `object` as SourceData
                sources.postValue(response)
            }
        }
    }

    override fun notifyViewOnFailure(`object`: Any?, type: Int) {
        when (type) {
            0 -> {
                sources.postValue(null)
            }
        }
    }

    fun initCategoryList(): ArrayList<String> {
        val categoryList = arrayListOf<String>()
        categoryList.add("Business")
        categoryList.add("Entertainment")
        categoryList.add("General")
        categoryList.add("Health")
        categoryList.add("Science")
        categoryList.add("Sports")
        categoryList.add("Technology")
        return categoryList
    }
}
