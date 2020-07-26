package com.example.newsapp.ui.source.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.newsapp.R
import com.example.newsapp.adapter.ViewPagerAdapter
import com.example.newsapp.core.Constants
import com.example.newsapp.databinding.ActivitySourceBinding
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.utility.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator

class SourceActivity : AppCompatActivity() {

    private lateinit var sourceBinding: ActivitySourceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sourceBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_source)

        initViews()
        setUpActionBar()
    }

    private fun initViews() {
        sourceBinding.pager.isUserInputEnabled = true
        sourceBinding.pager.setPageTransformer(ZoomOutPageTransformer())
        sourceBinding.pager.adapter =
            ViewPagerAdapter(
                supportFragmentManager,
                lifecycle
            )
        TabLayoutMediator(
            sourceBinding.tabLayout,
            sourceBinding.pager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = Constants.SOURCES
                1 -> tab.text = Constants.BY_COUNTRY
                2 -> tab.text = Constants.BY_CATEGORY
            }
        }.attach()
    }

    private fun setUpActionBar() {
        sourceBinding.sourceToolbar.title = Constants.SOURCES
        sourceBinding.backBtn.setOnClickListener {
            goBackToMain()
        }
    }

    private fun goBackToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

