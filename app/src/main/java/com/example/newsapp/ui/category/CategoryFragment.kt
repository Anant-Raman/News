package com.example.newsapp.ui.category

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
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
import com.example.newsapp.databinding.FragmentCategoryBinding
import com.example.newsapp.ui.webview.WebViewActivity
import com.example.newsapp.utility.SnackBarUtils
import kotlinx.android.synthetic.main.fragment_headline.*
import java.util.*
import kotlin.math.ceil


class CategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var fragmentCategoryBinding: FragmentCategoryBinding
    private var categoryList = arrayListOf<String>()
    private lateinit var categoty: String
    private var page = 1
    private val pageSize = 20
    private var totalData: Int? = null
    private val countryCat = Constants.INDIA

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCategoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        initViews()
        categoryList = categoryViewModel.initCategoryList()
        initSpinner()
        return fragmentCategoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun initViews() {
        fragmentCategoryBinding.rvNews.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun observeLiveData() {
        categoryViewModel.categoryHeadline.observe(viewLifecycleOwner, Observer {
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
                    SnackBarUtils.showSnackBar(
                        Constants.NEWS_SAVED,
                        fragmentCategoryBinding.rootCategory,
                        requireContext()
                    )
                }

                override fun deleteArticle(position: Int) {
//                    TODO("Not yet implemented")
                }
            })
            rv_news.adapter = newsArticleAdapter
            setPaging()
            fragmentCategoryBinding.categoryPagingLayout.visibility = View.VISIBLE
        })

        categoryViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                Constants.STATUS_START -> {
                    fragmentCategoryBinding.categoryProgbar.visibility = View.VISIBLE
                }
                Constants.STATUS_LOADED -> {
                    fragmentCategoryBinding.categoryProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_FAILED -> {
                    fragmentCategoryBinding.categoryProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_NODATA -> {
                    fragmentCategoryBinding.categoryProgbar.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun initSpinner() {
        Collections.sort(categoryList)
        categoryList.add(0, Constants.SELECT_NEWS_CATEGORY)
        val adapter = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            categoryList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fragmentCategoryBinding.spinnerCategory.adapter = adapter

        fragmentCategoryBinding.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    (view as TextView).setTextColor(Color.WHITE)
                    (view).setTextSize(18f)
                    if (position > 0) {
                        page = 1
                        categoty = categoryList[position]
                        if (internetCheck() == true) {
                            categoryViewModel.fetchCategoryHeadline(
                                countryCat,
                                page,
                                categoryList.get(position)
                            )
                        }
                    }
                }
            }
    }

    private fun saveNews(article: Article) {
        categoryViewModel.saveNews(article)
    }

    private fun launchWebView(article: Article) {
        if (internetCheck() == true) {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra(Constants.URL_LABEL, article.url)
            this.startActivity(intent)
        }
    }

    private fun setPaging() {
        fragmentCategoryBinding.btnNext.setOnClickListener {
            if (internetCheck() == true) {
                if (totalData != null && page * 20 < totalData!!) {
                    categoryViewModel.fetchCategoryHeadline(countryCat, ++page, categoty)
                }
            }
        }

        fragmentCategoryBinding.btnPrev.setOnClickListener {
            if (page > 1) {
                if (internetCheck() == true) {
                    categoryViewModel.fetchCategoryHeadline(countryCat, --page, categoty)
                }
            }
        }

        fragmentCategoryBinding.btnFirst.setOnClickListener {
            if (page > 1) {
                if (internetCheck() == true) {
                    page = 1
                    categoryViewModel.fetchCategoryHeadline(countryCat, page, categoty)
                }
            }
        }
        fragmentCategoryBinding.btnLast.setOnClickListener {
            if (totalData != null) {
                val pageLast = ceil((totalData!!.toDouble() / pageSize)).toInt()
                if (page < pageLast) {
                    if (internetCheck() == true) {
                        page = pageLast
                        categoryViewModel.fetchCategoryHeadline(countryCat, page, categoty)
                    }
                }
            }
        }

        if (page == 1) {
            fragmentCategoryBinding.btnFirst.alpha = .5f
            fragmentCategoryBinding.btnPrev.alpha = .5f
        } else {
            fragmentCategoryBinding.btnFirst.alpha = 1f
            fragmentCategoryBinding.btnPrev.alpha = 1f
        }
        if (totalData != null) {
            val pageLast = ceil((totalData!!.toDouble() / pageSize)).toInt()
            fragmentCategoryBinding.pageNumber = page.toString().plus(Constants.OF).plus(pageLast)
            if (page == pageLast) {
                fragmentCategoryBinding.btnLast.alpha = .5f
                fragmentCategoryBinding.btnNext.alpha = .5f
            } else {
                fragmentCategoryBinding.btnLast.alpha = 1f
                fragmentCategoryBinding.btnNext.alpha = 1f
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
                fragmentCategoryBinding.rootCategory,
                requireContext()
            )
        }
        return isConnected
    }
}