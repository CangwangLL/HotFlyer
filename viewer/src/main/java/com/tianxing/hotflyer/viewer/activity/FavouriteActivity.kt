package com.tianxing.hotflyer.viewer.activity

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem

import com.roughike.bottombar.BottomBar
import com.roughike.bottombar.OnTabSelectListener

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.ViewPagerAdapter
import com.tianxing.hotflyer.viewer.fragment.favourite.FavouriteActressFragment
import com.tianxing.hotflyer.viewer.fragment.favourite.FavouriteFragment
import com.tianxing.hotflyer.viewer.fragment.favourite.FavouriteMovieFragment

class FavouriteActivity : AppCompatActivity() {

    lateinit var mViewPager: ViewPager

    lateinit internal var mBottomBar: BottomBar

    internal var menuItem: MenuItem? = null

    private val mOnTabSelectedListener = OnTabSelectListener { tabId ->
        Log.i("tabid", tabId.toString())
        if (tabId == R.id.tab_fav_actress)
            mViewPager.currentItem = 1
        else if (tabId == R.id.tab_fav_movie)
            mViewPager.currentItem = 0
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == R.id.nav_fav_actresses) {
            mViewPager.currentItem = 1
            return@OnNavigationItemSelectedListener true
        } else if (item.itemId == R.id.nav_fav_movie) {
            mViewPager.currentItem = 0
        }
        false
    }

    private val mOnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            mBottomBar.selectTabAtPosition(position, true)
            mBottomBar.shySettings.showBar()
            /*if (menuItem != null) {
                menuItem.setChecked(false);
            } else {
                mNav.getMenu().getItem(0).setChecked(false);
            }
            mNav.getMenu().getItem(position).setChecked(true);
            menuItem = mNav.getMenu().getItem(position);*/
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        mViewPager = findViewById(R.id.favourite_view_pager)
        mBottomBar = findViewById(R.id.favourite_bottom_bar)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mAdapter = ViewPagerAdapter(supportFragmentManager)
        mViewPager.adapter = mAdapter
        mViewPager.addOnPageChangeListener(mOnPageChangeListener)

        var fragment: FavouriteFragment = FavouriteMovieFragment()
        mAdapter!!.addFragment(fragment, "作品")
        fragment = FavouriteActressFragment()
        mAdapter!!.addFragment(fragment, "女优")
        mAdapter!!.notifyDataSetChanged()

        //mNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomBar.setOnTabSelectListener(mOnTabSelectedListener, true)

        /*NestedScrollView mScrollView = (NestedScrollView) findViewById(R.id.favourite_scroll_view);
        mScrollView.setFillViewport(true);*/
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
}
