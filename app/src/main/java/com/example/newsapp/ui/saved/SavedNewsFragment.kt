package com.example.newsapp.ui.saved

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapter.NewsArticleAdapter
import com.example.news.data.Article
import com.example.newsapp.R
import com.example.newsapp.adapter.SavedNewsArticleAdapter
import com.example.newsapp.callbacks.NewsCallbacks
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.ui.webview.WebViewActivity
import kotlinx.android.synthetic.main.fragment_headline.*

class SavedNewsFragment : Fragment() {

    companion object {
        fun newInstance() = SavedNewsFragment()
    }

    private lateinit var fragmentSavedNewsBinding : FragmentSavedNewsBinding
    private lateinit var savedViewModel: SavedNewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSavedNewsBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_saved_news, container, false)
        savedViewModel = ViewModelProvider(this).get(SavedNewsViewModel::class.java)
        setUpActionBar()
        initViews()
        observeData()
        return fragmentSavedNewsBinding.root
    }

    private fun initViews() {
        fragmentSavedNewsBinding.rvNews.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }
    private fun observeData() {
        savedViewModel.articleList.observe(viewLifecycleOwner, Observer {
            val mArticleList: List<Article> = it
            val savedNewsArticleAdapter = SavedNewsArticleAdapter(mArticleList, object :
                NewsCallbacks {

                override fun launchNewsWebView(position: Int) {
                    launchWebView(mArticleList.get(position))
                }

                override fun saveArticle(position: Int) {
                    saveNews(mArticleList.get(position))
                }
            }
            )
            rv_news.adapter = savedNewsArticleAdapter
        })

    }

    private fun saveNews(article: Article){
       // savedViewModel.saveNews(article)
    }

    private fun launchWebView(article: Article){
        val intent = Intent(requireContext(), WebViewActivity::class.java)
        intent.putExtra("url", article.url)
        this.startActivity(intent)
    }

    private fun setUpActionBar() {
        fragmentSavedNewsBinding.savedToolbar.title = "Saved News"
    }
}


