package com.example.newsapp.ui.saved

import android.content.Intent
import android.os.Build
import androidx.biometric.BiometricManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
import com.example.newsapp.extention.showBiometric
import com.example.newsapp.extention.showDialog
import com.example.newsapp.ui.webview.WebViewActivity
import com.example.newsapp.utility.SnackBarUtils
import kotlinx.android.synthetic.main.fragment_headline.*

class SavedNewsFragment : Fragment() {

    companion object {
        fun newInstance() = SavedNewsFragment()
    }

    private lateinit var fragmentSavedNewsBinding : FragmentSavedNewsBinding
    private lateinit var savedViewModel: SavedNewsViewModel
    private lateinit var articleToDelete: Article

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
        setOnClick()
        return fragmentSavedNewsBinding.root
    }

    private fun initViews() {
        fragmentSavedNewsBinding.rvNews.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    private fun setOnClick(){
        fragmentSavedNewsBinding.deleteAllBtn.setOnClickListener {
            confirmDeleteAll()
        }

    }

    private fun observeData() {
        savedViewModel.articleList.observe(viewLifecycleOwner, Observer {
            val mArticleList: List<Article> = it
            val savedNewsArticleAdapter = SavedNewsArticleAdapter(mArticleList, object :
                NewsCallbacks {

                override fun launchNewsWebView(position: Int) {
                    launchWebView(mArticleList[position])
                }

                override fun saveArticle(position: Int) {

                }

                override fun deleteArticle(position: Int) {
                    confirmDelete(mArticleList[position])
                }
            }
            )
            rv_news.adapter = savedNewsArticleAdapter
        })

    }

    private fun confirmDelete(article: Article){
        activity?.let {
            articleToDelete = article
            it.showDialog("Delete article?","Are you sure you want to delete this article?","Yes","No",onCancelled,onContinue)
        }
    }

    private fun confirmDeleteAll(){
        activity?.let {
            it.showDialog("Delete all articles?","Are you sure you want to delete all the articles? (Enable your fingerprint scanner for further security)","Yes","No",
                onCancelled,onContinueDeleteAll as () -> Unit)
        }
    }

    private var onContinue= {
        deleteNews(articleToDelete)
    }

    private var onContinueDeleteAll ={
        val biometricManager = BiometricManager.from(requireContext())
        if (savedViewModel.canAuthenticate(biometricManager)) {
            activity?.let {
                it.showBiometric(it,requireContext(),onAuthenticated,onCancelled)
            }
        }
        else{
            deleteAll()
        }
    }


    private var onCancelled = {

    }

    private var onAuthenticated = {
        deleteAll()
    }

    private fun deleteNews(article: Article){
        savedViewModel.deleteNews(article)
        SnackBarUtils.showSnackBar("Deleted",fragmentSavedNewsBinding.savedRoot,requireContext())
    }

    private fun deleteAll(){
        savedViewModel.deleteAll()
        SnackBarUtils.showSnackBar("All articles deleted",fragmentSavedNewsBinding.savedRoot,requireContext())
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


