package com.example.newsapp.ui.source.bycountry

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapter.SourceAdapter
import com.example.news.data.SourceData
import com.example.newsapp.R
import com.example.newsapp.callbacks.SourceCallback
import com.example.newsapp.core.Constants
import com.example.newsapp.databinding.FragmentSourcesByCountryBinding
import com.example.newsapp.ui.webview.WebViewActivity
import com.example.newsapp.utility.SnackBarUtils

class SourcesByCountryFragment : Fragment() {

    private lateinit var sourcesByCountryViewModel: SourcesByCountryViewModel
    private lateinit var sourcesByCountryBinding: FragmentSourcesByCountryBinding
    private lateinit var countryList: ArrayList<String>
    private lateinit var countryLabelList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sourcesByCountryBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_sources_by_country,
                container,
                false
            )
        sourcesByCountryViewModel = ViewModelProvider(this).get(
            SourcesByCountryViewModel::
            class.java
        )
        initViews()
        observeLiveData()
        initSpinner()
        return sourcesByCountryBinding.root
    }

    private fun initViews() {
        sourcesByCountryBinding.rvSource.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

    }

    fun initSpinner() {
        countryList = sourcesByCountryViewModel.initCountryList()
        countryLabelList = sourcesByCountryViewModel.initCountryLabelList()

        val adapter = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            countryLabelList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourcesByCountryBinding.spinnerSourceCountry.adapter = adapter

        sourcesByCountryBinding.spinnerSourceCountry.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        sourcesByCountryViewModel.fetchSourceByCategory(countryList[position])
                    }
                }
            }
    }

    private fun observeLiveData() {
        sourcesByCountryViewModel.sources.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.sourcesList.size < 1) {
                    sourcesByCountryBinding.tvNoSourceAvailable.visibility = View.VISIBLE
                } else {
                    sourcesByCountryBinding.tvNoSourceAvailable.visibility = View.INVISIBLE
                }
                val sourceDataList: List<SourceData.SourcesList> = it.sourcesList
                val sourceAdapter = SourceAdapter(sourceDataList, object :
                    SourceCallback {
                    override fun launchWebView(position: Int) {
                        launchWebView(sourceDataList.get(position))
                    }
                }
                )
                sourcesByCountryBinding.rvSource.adapter = sourceAdapter
            }
        })
        sourcesByCountryViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                Constants.STATUS_START -> {
                    sourcesByCountryBinding.countrySourceProgbar.visibility = View.VISIBLE
                }
                Constants.STATUS_LOADED -> {
                    sourcesByCountryBinding.countrySourceProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_FAILED -> {
                    sourcesByCountryBinding.countrySourceProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_NODATA -> {
                    sourcesByCountryBinding.countrySourceProgbar.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun launchWebView(source: SourceData.SourcesList) {
        if (internetCheck() == true) {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra(Constants.URL_LABEL, source.url)
            this.startActivity(intent)
        }
    }

    private fun internetCheck(): Boolean? {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        if (!isConnected) {
            SnackBarUtils.showSnackBar(
                Constants.INTERNET_UNAVAILABLE,
                sourcesByCountryBinding.rootSourcesByCountry,
                requireContext()
            )
        }
        return isConnected
    }
}