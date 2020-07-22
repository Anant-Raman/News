package com.example.newsapp.ui.headlines.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.example.newsapp.utility.SnackBarUtils
import kotlinx.android.synthetic.main.fragment_headline.*
import kotlin.math.ceil

class HeadlineFragment : Fragment() {

    private lateinit var headlineViewModel: HeadlineViewModel
    private lateinit var fragmentHeadlineBinding: FragmentHeadlineBinding
    private var country = "in"
    private var page = 1
    private val pageSize = 20
    private var totalData: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHeadlineBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_headline, container, false)
        headlineViewModel = ViewModelProvider(this).get(HeadlineViewModel::class.java)
        page = 1
        initViews()
        setUpActionBar()
        return fragmentHeadlineBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initViews() {
        fragmentHeadlineBinding.rvNews.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        if (SharedPreferences.getStringSharedPref(Constants.COUNTRY, requireContext()) != null) {
            country = SharedPreferences.getStringSharedPref(Constants.COUNTRY, requireContext())
                .toString()
        }

        fragmentHeadlineBinding.swipeRefreshLayout.setOnRefreshListener {
            if (InternetCheck() == true) {
                page = 1
                refreshAction()                    // refresh your list contents somehow
                fragmentHeadlineBinding.swipeRefreshLayout.isRefreshing =
                    false   // reset the SwipeRefreshLayout (stop the loading spinner)
            }
        }
    }

    private fun setPaging() {

        fragmentHeadlineBinding.btnNext.setOnClickListener {
            if (totalData != null && page * 20 < totalData!!) {
                if (InternetCheck() == true) {
                    headlineViewModel.fetchHeadline(country, ++page)
                }
            }
        }

        fragmentHeadlineBinding.btnPrev.setOnClickListener {
            if (page > 1) {
                if (InternetCheck() == true) {
                    headlineViewModel.fetchHeadline(country, --page)
                }
            }
        }

        fragmentHeadlineBinding.btnFirst.setOnClickListener {
            if (page > 1) {
                if (InternetCheck() == true) {
                    page = 1
                    headlineViewModel.fetchHeadline(country, page)
                }
            }
        }
        fragmentHeadlineBinding.btnLast.setOnClickListener {
            if (totalData != null) {
                val pageLast = ceil((totalData!!.toDouble() / pageSize)).toInt()
                if (page < pageLast) {
                    if (InternetCheck() == true) {
                        page = pageLast
                        headlineViewModel.fetchHeadline(country, page)
                    }
                }
            }
        }

        if (page == 1) {
            fragmentHeadlineBinding.btnFirst.alpha = .5f
            fragmentHeadlineBinding.btnPrev.alpha = .5f
        } else {
            fragmentHeadlineBinding.btnFirst.alpha = 1f
            fragmentHeadlineBinding.btnPrev.alpha = 1f
        }
        if (totalData != null) {
            val pageLast = ceil((totalData!!.toDouble() / pageSize)).toInt()
            fragmentHeadlineBinding.pageNumber = page.toString().plus(" of ").plus(pageLast)
            if (page == pageLast) {
                fragmentHeadlineBinding.btnLast.alpha = .5f
                fragmentHeadlineBinding.btnNext.alpha = .5f
            } else {
                fragmentHeadlineBinding.btnLast.alpha = 1f
                fragmentHeadlineBinding.btnNext.alpha = 1f
            }
        }
    }

    private fun refreshAction() {
        if (SharedPreferences.getStringSharedPref(Constants.COUNTRY, requireContext()) != null) {
            country = SharedPreferences.getStringSharedPref(Constants.COUNTRY, requireContext())
                .toString()
        }
        headlineViewModel.fetchHeadline(country, 1)
    }

    private fun observeLiveData() {
        headlineViewModel.headline.observe(viewLifecycleOwner, Observer {
            val mArticleList: List<Article> = it.articles
            totalData = it.totalResult
            if (totalData != null && totalData!! > 100) {
                totalData = 100
            }
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
            setPaging()
        })
        if (InternetCheck() == true) {
            headlineViewModel.fetchHeadline(country, 1)
        }
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

    private fun saveNews(article: Article) {
        headlineViewModel.saveNews(article)
        SnackBarUtils.showSnackBar("News saved",fragmentHeadlineBinding.swipeRefreshLayout,requireContext())
    }

    private fun launchWebView(article: Article) {
        if (InternetCheck() == true) {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra("url", article.url)
            this.startActivity(intent)
        }
    }

    private fun InternetCheck(): Boolean? {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        if(!isConnected){
            SnackBarUtils.showSnackBar("Internet Connection not available",fragmentHeadlineBinding.swipeRefreshLayout,requireContext())
        }
        return isConnected
    }
}