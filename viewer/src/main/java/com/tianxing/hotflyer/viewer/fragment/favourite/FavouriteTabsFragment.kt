package com.tianxing.hotflyer.viewer.fragment.favourite


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.ViewPagerAdapter
import com.tianxing.hotflyer.viewer.fragment.ExtendedAppBarFragment

class FavouriteTabsFragment : ExtendedAppBarFragment() {

    lateinit var mTabLayout: TabLayout

    lateinit var mViewPager: ViewPager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter = ViewPagerAdapter(activity.supportFragmentManager)
        mViewPager.adapter = mAdapter
        mTabLayout.setupWithViewPager(mViewPager)

        var fragment: FavouriteFragment = FavouriteMovieFragment()
        mAdapter!!.addFragment(fragment, "作品")
        fragment = FavouriteActressFragment()
        mAdapter!!.addFragment(fragment, "女优")

        mAdapter!!.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_favourite, container, false)
        mTabLayout = view.findViewById(R.id.favourite_tabs)
        mViewPager = view.findViewById(R.id.favourite_view_pager)
        return view
    }

    companion object {

        var mAdapter: ViewPagerAdapter? = null

        fun update() {
            if (mAdapter != null) {
                for (i in 0 until mAdapter!!.count) {
                    (mAdapter!!.getItem(i) as FavouriteFragment).update()
                }
            }
        }
    }
}// Required empty public constructor
