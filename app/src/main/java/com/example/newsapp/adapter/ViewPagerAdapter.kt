package com.example.newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.ui.source.allsources.AllSourcesFragment
import com.example.newsapp.ui.source.bycategory.SourcesByCategoryFragment
import com.example.newsapp.ui.source.bycountry.SourcesByCountryFragment


class ViewPagerAdapter(fm: FragmentManager?, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm!!, lifecycle) {

    private val int_items = 3

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = AllSourcesFragment()
            1 -> fragment = SourcesByCountryFragment()
            2 -> fragment = SourcesByCategoryFragment()
        }
        return fragment!!
    }

    override fun getItemCount(): Int {
        return int_items
    }
}