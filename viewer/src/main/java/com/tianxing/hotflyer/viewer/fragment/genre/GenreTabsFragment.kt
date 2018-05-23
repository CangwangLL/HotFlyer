package com.tianxing.hotflyer.viewer.fragment.genre


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import java.util.LinkedHashMap

import butterknife.BindView
import butterknife.ButterKnife

import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.ViewPagerAdapter
import com.tianxing.hotflyer.viewer.adapter.item.Genre
import com.tianxing.hotflyer.viewer.fragment.ExtendedAppBarFragment
import com.tianxing.hotflyer.viewer.network.provider.AVMOProvider

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenreTabsFragment : ExtendedAppBarFragment() {

    lateinit var mTabLayout: TabLayout

    lateinit var mViewPager: ViewPager

    lateinit var mProgressBar: ProgressBar

    lateinit var mAdapter: ViewPagerAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter = ViewPagerAdapter(activity.supportFragmentManager)
        mViewPager.adapter = mAdapter
        mTabLayout.setupWithViewPager(mViewPager)

        val call = JAViewer.SERVICE.genre
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                mProgressBar.visibility = View.GONE
                try {
                    val genres = AVMOProvider.parseGenres(response.body()!!.string())

                    var fragment: GenreFragment
                    for (title in genres.keys) {
                        fragment = GenreFragment()
                        fragment.genres.addAll(genres[title]!!.toMutableList())
                        mAdapter.addFragment(fragment, title)
                    }

                    mAdapter.notifyDataSetChanged()

                    mTabLayout.visibility = View.VISIBLE
                } catch (e: Throwable) {
                    onFailure(call, e)
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_genre, container, false)
        mTabLayout = view.findViewById(R.id.genre_tabs)
        mViewPager = view.findViewById(R.id.genre_view_pager)
        mProgressBar = view.findViewById(R.id.genre_progress_bar)
        return view
    }
}// Required empty public constructor
