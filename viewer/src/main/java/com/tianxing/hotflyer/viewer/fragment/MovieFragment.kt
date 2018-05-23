package com.tianxing.hotflyer.viewer.fragment


import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.tianxing.hotflyer.viewer.adapter.MovieAdapter
import com.tianxing.hotflyer.viewer.adapter.item.Movie
import com.tianxing.hotflyer.viewer.network.provider.AVMOProvider
import com.tianxing.hotflyer.viewer.view.decoration.MovieItemDecoration
import com.tianxing.hotflyer.viewer.view.listener.EndlessOnScrollListener

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import okhttp3.ResponseBody
import retrofit2.Call

abstract class MovieFragment : RecyclerFragment<Movie, LinearLayoutManager>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.layoutManager = LinearLayoutManager(this.context)
        mRecyclerView!!.addItemDecoration(MovieItemDecoration())
        this.adapter = SlideInBottomAnimationAdapter(MovieAdapter(items, this.activity))
        val animator = SlideInUpAnimator()
        animator.addDuration = 300
        mRecyclerView!!.itemAnimator = animator

        this.onRefreshListener = SwipeRefreshLayout.OnRefreshListener { onScrollListener!!.refresh() }

        this.addOnScrollListener(object : EndlessOnScrollListener<Movie>() {

            override val layoutManager: RecyclerView.LayoutManager
                get() = this@MovieFragment.layoutManager

            override val refreshLayout: SwipeRefreshLayout
                get() = this@MovieFragment.mRefreshLayout

            override val items: MutableList<Movie>
                get() = this@MovieFragment.items

            override val adapter: RecyclerView.Adapter<*>?
                get() = this@MovieFragment.adapter

            override fun newCall(page: Int): Call<ResponseBody>? {
                return this@MovieFragment.newCall(page)
            }

            @Throws(Exception::class)
            override fun onResult(response: ResponseBody?) {
                super.onResult(response)
                val wrappers = AVMOProvider.parseMovies(response!!.string())

                val pos = items.size

                items.addAll(wrappers)
                adapter!!.notifyItemRangeInserted(pos, wrappers.size)
            }
        })

        mRefreshLayout.post {
            mRefreshLayout.isRefreshing = true
            onRefreshListener?.onRefresh()
        }

        super.onActivityCreated(savedInstanceState)
    }

    abstract fun newCall(page: Int): Call<ResponseBody>
}// Required empty public constructor
