package com.example.newsapp.ui.source.allsources

//import SourceData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.SourceData
import com.example.newsapp.network.IViewApiListener
import com.example.newsapp.network.RestApiService

class AllSourcesViewModel : ViewModel(), IViewApiListener {

    var sources: MutableLiveData<SourceData?> = MutableLiveData()
    var state: MutableLiveData<String> = MutableLiveData()

    fun fetchAllSource() {
        val service = RestApiService()
        service.getAllSource(this, state)
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
}