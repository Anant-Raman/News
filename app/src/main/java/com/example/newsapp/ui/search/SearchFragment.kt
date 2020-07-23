package com.example.newsapp.ui.search

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
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
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.ui.webview.WebViewActivity
import com.example.newsapp.utility.SnackBarUtils
import kotlinx.android.synthetic.main.fragment_headline.*
import kotlin.math.ceil

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchBinding: FragmentSearchBinding
    private lateinit var sortBy: String
    private lateinit var language: String
    private var page = 1
    private val pageSize = 20
    private var totalData: Int? = null
    private lateinit var queryKey: String

    private var sortByList = arrayListOf<String>()
    private var sortByListLabel = arrayListOf<String>()
    private var languageList = arrayListOf<String>()
    private var languageLabelList = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        sortBy = "publishedAt"
        language = "en"
        initViews()
        initSpinner()
        initSearchView()
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResult()
    }

    private fun initViews() {
        searchBinding.rvNews.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun initSpinner() {
        sortByList = searchViewModel.initSortList()
        sortByListLabel = searchViewModel.initSortListLabel()
        val adapter = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            sortByListLabel
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchBinding.spinnerSort.adapter = adapter

        searchBinding.spinnerSort.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sortBy = sortByList.get(position)
                }
            }

        languageList = searchViewModel.initLanguageList()
        languageLabelList = searchViewModel.initLanguageLabelList()

        val langAdapter = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            languageLabelList
        )
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchBinding.setLanguageSpinner.adapter = langAdapter

        searchBinding.setLanguageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    language = languageList.get(position)
                }
            }
    }

    fun initSearchView() {
        searchBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { result ->
                    queryKey = result
                    if (internetCheck() == true) {
                        searchViewModel.fetchSearchResult(result, page, sortBy, language)
                        val imm =
                            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Handler().postDelayed({
                    newText?.let { result ->
                        //Log.i("Anant", newText)
                        // searchViewModel.fetchSearchResult(result, sortBy, language)
                    }
                }, 1000)
                return false
            }
        })
    }

    fun observeResult() {
        searchViewModel.searchResult.observe(viewLifecycleOwner, Observer { result ->
            val mArticleList: List<Article> = result.articles
            totalData = result.totalResult
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
                    SnackBarUtils.showSnackBar(Constants.NEWS_SAVED,searchBinding.rootSearch,requireContext())
                }

                override fun deleteArticle(position: Int) {
//                    TODO("Not yet implemented")
                }
            })
            rv_news.adapter = newsArticleAdapter
            setPaging()
            searchBinding.searchPagingLayout.visibility = View.VISIBLE
        })
        searchViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                Constants.STATUS_START -> {
                    searchBinding.searchProgbar.visibility = View.VISIBLE
                }
                Constants.STATUS_LOADED -> {
                    searchBinding.searchProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_FAILED -> {
                    searchBinding.searchProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_NODATA -> {
                    searchBinding.searchProgbar.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun saveNews(article: Article) {
        searchViewModel.saveNews(article)
    }

    private fun launchWebView(article: Article) {
        if (internetCheck() == true) {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra(Constants.URL_LABEL, article.url)
            this.startActivity(intent)
        }
    }

    private fun setPaging() {
        searchBinding.btnNext.setOnClickListener {
            if (totalData != null && page * 20 < totalData!!) {
                if (internetCheck() == true) {
                    searchViewModel.fetchSearchResult(queryKey, ++page, sortBy, language)
                }
            }
        }

        searchBinding.btnPrev.setOnClickListener {
            if (page > 1) {
                if (internetCheck() == true) {
                    searchViewModel.fetchSearchResult(queryKey, --page, sortBy, language)
                }
            }
        }

        searchBinding.btnFirst.setOnClickListener {
            if (page > 1) {
                if (internetCheck() == true) {
                    page = 1
                    searchViewModel.fetchSearchResult(queryKey, page, sortBy, language)
                }
            }
        }
        searchBinding.btnLast.setOnClickListener {
            if (totalData != null) {
                val pageLast = ceil((totalData!!.toDouble() / pageSize)).toInt()
                if (internetCheck() == true) {
                    if (page < pageLast) {
                        page = pageLast
                        searchViewModel.fetchSearchResult(queryKey, page, sortBy, language)
                    }
                }
            }
        }

        if (page == 1) {
            searchBinding.btnFirst.alpha = .5f
            searchBinding.btnPrev.alpha = .5f
        } else {
            searchBinding.btnFirst.alpha = 1f
            searchBinding.btnPrev.alpha = 1f
        }
        if (totalData != null) {
            val pageLast = ceil((totalData!!.toDouble() / pageSize)).toInt()
            searchBinding.pageNumber = page.toString().plus(" of ").plus(pageLast)
            if (page == pageLast) {
                searchBinding.btnLast.alpha = .5f
                searchBinding.btnNext.alpha = .5f
            } else {
                searchBinding.btnLast.alpha = 1f
                searchBinding.btnNext.alpha = 1f
            }
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
                searchBinding.rootSearch,
                requireContext()
            )
        }
        return isConnected
    }
}
