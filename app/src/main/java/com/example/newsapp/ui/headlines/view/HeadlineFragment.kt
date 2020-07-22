package com.example.newsapp.ui.headlines.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapter.NewsArticleAdapter
import com.example.news.data.Article
import com.example.newsapp.R
import com.example.newsapp.callbacks.NewsCallbacks
import com.example.newsapp.core.Constants
import com.example.newsapp.databinding.FragmentHeadlineBinding
import com.example.newsapp.extention.showBottomSheet
import com.example.newsapp.ui.headlines.viewmodel.HeadlineViewModel
import com.example.newsapp.ui.webview.WebViewActivity
import com.example.newsapp.utility.SharedPreferences
import kotlinx.android.synthetic.main.fragment_headline.*

class HeadlineFragment : Fragment() {

    private lateinit var headlineViewModel: HeadlineViewModel
    private lateinit var fragmentHeadlineBinding: FragmentHeadlineBinding
    private var country = "in"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHeadlineBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_headline, container, false)
        headlineViewModel = ViewModelProvider(this).get(HeadlineViewModel::class.java)
        initViews()
        setUpActionBar()
        observeLiveData()
        return fragmentHeadlineBinding.root
    }


    private fun setUpActionBar() {
        fragmentHeadlineBinding.headlineToolbar.title = "Headlines"
        fragmentHeadlineBinding.menuBtn.setOnClickListener {
            openSettingBottomSheet()
        }
    }

    private fun openSettingBottomSheet() {
        activity?.let {
            it.showBottomSheet(parentFragmentManager)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu,inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initViews() {
        fragmentHeadlineBinding.rvNews.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        if(SharedPreferences.getStringSharedPref(Constants.COUNTRY,requireContext())!=null){
            country = SharedPreferences.getStringSharedPref(Constants.COUNTRY,requireContext()).toString()
        }

        fragmentHeadlineBinding.swipeRefreshLayout.setOnRefreshListener {
            refreshAction()                    // refresh your list contents somehow
            fragmentHeadlineBinding.swipeRefreshLayout.isRefreshing = false   // reset the SwipeRefreshLayout (stop the loading spinner)
        }

    }

    private fun refreshAction(){
        if(SharedPreferences.getStringSharedPref(Constants.COUNTRY,requireContext())!=null){
            country = SharedPreferences.getStringSharedPref(Constants.COUNTRY,requireContext()).toString()
        }
        Log.i("Anant","Refreshed")
        headlineViewModel.fetchHeadline(country)
    }

    private fun observeLiveData() {
        headlineViewModel.headline.observe(viewLifecycleOwner, Observer {
            val mArticleList: List<Article> = it.articles
            val newsArticleAdapter = NewsArticleAdapter(mArticleList, object :
                NewsCallbacks {

                override fun launchNewsWebView(position: Int) {
                    launchWebView(mArticleList.get(position))
                }

                override fun saveArticle(position: Int) {
                    saveNews(mArticleList.get(position))
                }

                override fun deleteArticle(position: Int) {
//                    TODO("Not yet implemented")
                }
            }
            )
            rv_news.adapter = newsArticleAdapter
        })
        headlineViewModel.fetchHeadline(country)

        headlineViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                Constants.STATUS_START -> {
                    fragmentHeadlineBinding.headlineProgbar.visibility = View.VISIBLE
                }
                Constants.STATUS_LOADED -> {
                    fragmentHeadlineBinding.headlineProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_FAILED -> {
                    fragmentHeadlineBinding.headlineProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_NODATA -> {
                    fragmentHeadlineBinding.headlineProgbar.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun saveNews(article: Article){
        headlineViewModel.saveNews(article)
    }

    private fun launchWebView(article: Article){
        val intent = Intent(requireContext(), WebViewActivity::class.java)
        intent.putExtra("url", article.url)
        this.startActivity(intent)
    }

}