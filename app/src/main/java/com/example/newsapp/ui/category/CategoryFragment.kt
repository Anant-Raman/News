package com.example.newsapp.ui.category

import android.content.Intent
import android.graphics.Color
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
import kotlinx.android.synthetic.main.fragment_headline.*
import java.util.*


class CategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var fragmentCategoryBinding: FragmentCategoryBinding
    private var categoryList = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCategoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        initViews()
        observeLiveData()
        initCategoryList()
        initSpinner()
        return fragmentCategoryBinding.root
    }

    private fun initViews() {
        fragmentCategoryBinding.rvNews.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun observeLiveData() {
        categoryViewModel.categoryHeadline.observe(viewLifecycleOwner, Observer {
            val mArticleList: List<Article> = it.articles
            val newsArticleAdapter = NewsArticleAdapter(mArticleList, object :
                NewsCallbacks {

                override fun launchNewsWebView(position: Int) {
                    launchWebView(mArticleList.get(position))
                }

                override fun saveArticle(position: Int) {
                    saveNews(mArticleList.get(position))
                }
            })
            rv_news.adapter = newsArticleAdapter
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
        categoryList.add(0, "Select news category ")
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
                        categoryViewModel.fetchCategoryHeadline("in", categoryList.get(position))
                    }
                }
            }
    }

    private fun initCategoryList() {
        categoryList.add("Business")
        categoryList.add("Entertainment")
        categoryList.add("General")
        categoryList.add("Health")
        categoryList.add("Science")
        categoryList.add("Sports")
        categoryList.add("Technology")
    }

    private fun saveNews(article: Article){
        categoryViewModel.saveNews(article)
    }

    private fun launchWebView(article: Article){
        val intent = Intent(requireContext(), WebViewActivity::class.java)
        intent.putExtra("url", article.url)
        this.startActivity(intent)
    }

}