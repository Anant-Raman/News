package com.example.newsapp.ui.source.allsources

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.newsapp.databinding.FragmentAllSourcesBinding
import com.example.newsapp.ui.webview.WebViewActivity
import com.example.newsapp.utility.SnackBarUtils

class AllSourcesFragment : Fragment() {

    private lateinit var allSourcesViewModel: AllSourcesViewModel
    private lateinit var allSourcesBinding: FragmentAllSourcesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allSourcesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_all_sources, container, false)
        allSourcesViewModel = ViewModelProvider(this).get(AllSourcesViewModel::class.java)
        initViews()
        observeLiveData()
        return allSourcesBinding.root
    }

    private fun initViews() {
        allSourcesBinding.rvSource.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun observeLiveData() {
        allSourcesViewModel.sources.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val sourceDataList: List<SourceData.SourcesList> = it.sourcesList
                val sourceAdapter = SourceAdapter(sourceDataList, object :
                    SourceCallback {
                    override fun launchWebView(position: Int) {
                        launchWebView(sourceDataList.get(position))
                    }
                }
                )
                allSourcesBinding.rvSource.adapter = sourceAdapter
            }
        })
        if (internetCheck() == true) {
            allSourcesViewModel.fetchAllSource()
        }
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
                allSourcesBinding.rootAllSources,
                requireContext()
            )
        }
        return isConnected
    }
}