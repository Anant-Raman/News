package com.example.newsapp.ui.source.bycountry

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.data.SourceData
import com.example.newsapp.network.IViewApiListener
import com.example.newsapp.network.RestApiService

class SourcesByCountryViewModel : ViewModel(), IViewApiListener {

    var sources: MutableLiveData<SourceData?> = MutableLiveData()
    var state: MutableLiveData<String> = MutableLiveData()

    fun fetchSourceByCategory(country: String) {
        val service = RestApiService()
        service.getSourceByCountry(country, this, state)
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

    fun initCountryList(): ArrayList<String> {

        val conList = arrayListOf<String>()
        conList.add("")
        conList.add("in")
        conList.add("ar")
        conList.add("at")
        conList.add("ae")
        conList.add("au")
        conList.add("be")
        conList.add("bg")
        conList.add("br")
        conList.add("ca")
        conList.add("ch")
        conList.add("cn")
        conList.add("co")
        conList.add("cu")
        conList.add("cz")
        conList.add("de")
        conList.add("eg")
        conList.add("fr")
        conList.add("gb")
        conList.add("gr")
        conList.add("hk")
        conList.add("hu")
        conList.add("id")
        conList.add("ie")
        conList.add("il")
        conList.add("it")
        conList.add("jp")
        conList.add("kr")
        conList.add("lt")
        conList.add("lv")
        conList.add("ma")
        conList.add("mx")
        conList.add("my")
        conList.add("ng")
        conList.add("nl")
        conList.add("no")
        conList.add("nz")
        conList.add("ph")
        conList.add("pl")
        conList.add("pt")
        conList.add("ro")
        conList.add("rs")
        conList.add("ru")
        conList.add("sa")
        conList.add("se")
        conList.add("sg")
        conList.add("si")
        conList.add("sk")
        conList.add("th")
        conList.add("tr")
        conList.add("tw")
        conList.add("ua")
        conList.add("us")
        conList.add("ve")
        conList.add("za")

        return conList
    }

    fun initCountryLabelList(): ArrayList<String> {

        val countryLabelList = arrayListOf<String>()
        countryLabelList.add(("Select your country"))
        countryLabelList.add("India")
        countryLabelList.add("Argentina")
        countryLabelList.add("Austria")
        countryLabelList.add("United Arab Emirates")
        countryLabelList.add("Australia")
        countryLabelList.add("Belgium")
        countryLabelList.add("Bulgaria")
        countryLabelList.add("Brazil")
        countryLabelList.add("Canada")
        countryLabelList.add("Switzerland")
        countryLabelList.add("China")
        countryLabelList.add("Columbia")
        countryLabelList.add("Cuba")
        countryLabelList.add("Czechia")
        countryLabelList.add("Gremany")
        countryLabelList.add("Egypt")
        countryLabelList.add("France")
        countryLabelList.add("Great Britain")
        countryLabelList.add("Greece")
        countryLabelList.add("Hong Kong")
        countryLabelList.add("Hungary")
        countryLabelList.add("Indonesia")
        countryLabelList.add("Ireland")
        countryLabelList.add("Israel")
        countryLabelList.add("Italy")
        countryLabelList.add("Japan")
        countryLabelList.add("Korea")
        countryLabelList.add("Lithuania")
        countryLabelList.add("Latvia")
        countryLabelList.add("Morocco")
        countryLabelList.add("Mexico")
        countryLabelList.add("Malaysia")
        countryLabelList.add("Nigeria")
        countryLabelList.add("Netherlands")
        countryLabelList.add("Norway")
        countryLabelList.add("New Zealand")
        countryLabelList.add("Philippines")
        countryLabelList.add("Poland")
        countryLabelList.add("Portugal")
        countryLabelList.add("Romania")
        countryLabelList.add("Serbia")
        countryLabelList.add("Russia")
        countryLabelList.add("Saudi Arabia")
        countryLabelList.add("Sweden")
        countryLabelList.add("Singapore")
        countryLabelList.add("Slovenia")
        countryLabelList.add("Slovakia")
        countryLabelList.add("Thailand")
        countryLabelList.add("Turkey")
        countryLabelList.add("Taiwan")
        countryLabelList.add("Ukraine")
        countryLabelList.add("United States")
        countryLabelList.add("Venezuela")
        countryLabelList.add("South Africa")

        return countryLabelList
    }
}