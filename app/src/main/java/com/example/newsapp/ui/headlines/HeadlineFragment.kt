package com.example.newsapp.ui.headlines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapter.NewsArticleAdapter
import com.example.news.data.Article
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHeadlineBinding
import kotlinx.android.synthetic.main.fragment_headline.*

class HeadlineFragment : Fragment() {

    private lateinit var headlineViewModel: HeadlineViewModel
    private lateinit var fragmentHeadlineBinding: FragmentHeadlineBinding
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentHeadlineBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_headline,container,false)
        headlineViewModel =    ViewModelProvider(this).get(HeadlineViewModel::class.java)
        initViews()
        observeLiveData()
        return fragmentHeadlineBinding.root
    }

    private fun initViews(){
        fragmentHeadlineBinding.rvNews.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun observeLiveData(){
        headlineViewModel.headline.observe(viewLifecycleOwner, Observer {
            val mArticleList: List<Article> = it.articles
            val newsArticleAdapter = NewsArticleAdapter(mArticleList)
            rv_news.adapter = newsArticleAdapter
        })
        headlineViewModel.fetchHeadline()
    }
}