package com.example.newsapp.ui.source.bycategory

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
import com.example.newsapp.databinding.FragmentSourcesByCategoryBinding
import com.example.newsapp.ui.webview.WebViewActivity
import com.example.newsapp.utility.SnackBarUtils

class SourcesByCategoryFragment : Fragment() {

    private lateinit var sourcesByCategoryViewModel: SourcesByCategoryViewModel
    private lateinit var sourcesByCategoryBinding: FragmentSourcesByCategoryBinding
    private lateinit var categoryList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sourcesByCategoryBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_sources_by_category,
                container,
                false
            )
        sourcesByCategoryViewModel = ViewModelProvider(this).get(
            SourcesByCategoryViewModel::
            class.java
        )
        initViews()
        observeLiveData()
        initSpinner()
        return sourcesByCategoryBinding.root
    }

    private fun initViews() {
        sourcesByCategoryBinding.rvSource.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    fun initSpinner() {
        categoryList = sourcesByCategoryViewModel.initCategoryList()

        val adapter = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            categoryList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sourcesByCategoryBinding.spinnerSourceCategory.adapter = adapter

        sourcesByCategoryBinding.spinnerSourceCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (internetCheck() == true) {
                        sourcesByCategoryViewModel.fetchSourcesByCountry(categoryList[position])
                    }
                }
            }
    }

    private fun observeLiveData() {
        sourcesByCategoryViewModel.sources.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val sourceDataList: List<SourceData.SourcesList> = it.sourcesList
                val sourceAdapter = SourceAdapter(sourceDataList, object :
                    SourceCallback {
                    override fun launchWebView(position: Int) {
                        launchWebView(sourceDataList.get(position))
                    }
                }
                )
                sourcesByCategoryBinding.rvSource.adapter = sourceAdapter
            }
        })
        sourcesByCategoryViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                Constants.STATUS_START -> {
                    sourcesByCategoryBinding.categorySourceProgbar.visibility = View.VISIBLE
                }
                Constants.STATUS_LOADED -> {
                    sourcesByCategoryBinding.categorySourceProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_FAILED -> {
                    sourcesByCategoryBinding.categorySourceProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_NODATA -> {
                    sourcesByCategoryBinding.categorySourceProgbar.visibility = View.INVISIBLE
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
                sourcesByCategoryBinding.rootSourcesByCategory,
                requireContext()
            )
        }
        return isConnected
    }

}