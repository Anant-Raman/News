package com.example.newsapp.ui.search

import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.fragment_headline.*

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchBinding: FragmentSearchBinding
    private lateinit var sortBy : String
    private lateinit var language : String

    private var sortByList = arrayListOf<String>()
    private var sortByListLabel = arrayListOf<String>()
    private var languageList = arrayListOf<String>()
    private var languageLabelList = arrayListOf<String>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        searchBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_search,container,false)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        sortBy = "publishedAt"
        language = "en"
        initViews()
        initSpinner()
        initSearchView()
        observeResult()
        return searchBinding.root
    }

    private fun initViews(){
        searchBinding.rvNews.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
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

    fun initSearchView(){
        searchBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {result->
                    searchViewModel.fetchSearchResult(result,sortBy,language)
                    val imm =
                        activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
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

    fun observeResult(){
        searchViewModel.searchResult.observe(viewLifecycleOwner, Observer {result->
            val mArticleList: List<Article> = result.articles
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
            })
            rv_news.adapter = newsArticleAdapter
        })
        searchViewModel.state.observe(viewLifecycleOwner, Observer { state->
            when(state){
                Constants.STATUS_START->{
                    searchBinding.searchProgbar.visibility = View.VISIBLE
                }
                Constants.STATUS_LOADED->{
                    searchBinding.searchProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_FAILED->{
                    searchBinding.searchProgbar.visibility = View.INVISIBLE
                }
                Constants.STATUS_NODATA->{
                    searchBinding.searchProgbar.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun saveNews(article: Article){
        searchViewModel.saveNews(article)
    }

    private fun launchWebView(article: Article){
        val intent = Intent(requireContext(), WebViewActivity::class.java)
        intent.putExtra("url", article.url)
        this.startActivity(intent)
    }

}
