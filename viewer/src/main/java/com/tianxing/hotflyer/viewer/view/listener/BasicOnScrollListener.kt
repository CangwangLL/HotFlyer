package com.tianxing.hotflyer.viewer.view.listener

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Project: JAViewer
 */

abstract class BasicOnScrollListener<I> : RecyclerView.OnScrollListener() {

    var isLoading = false

    private var loadThreshold = 5
    private var currentPage = 0

    private var token: Long = 0
    var isEnd = false

    abstract val layoutManager: RecyclerView.LayoutManager

    abstract val refreshLayout: SwipeRefreshLayout

    abstract val items: MutableList<I>

    abstract val adapter: RecyclerView.Adapter<*>?


    fun reset() {
        isLoading = false
        loadThreshold = 5
        currentPage = 0
        val oldSize = items.size
        if (oldSize > 0) {
            items.clear()
            adapter?.notifyItemRangeRemoved(0, oldSize)
        }
    }

    fun saveState(): Bundle {
        val bundle = Bundle()
        bundle.putInt("CurrentPage", currentPage)
        return bundle
    }

    fun restoreState(bundle: Bundle) {
        currentPage = bundle.getInt("CurrentPage")
    }

    abstract fun newCall(page: Int): Call<ResponseBody>?

    fun refresh() {
        isLoading = true
        reset()
        onLoad(token = System.currentTimeMillis())
    }

    private fun onLoad(token: Long) {
        val page = currentPage
        val call = newCall(page + 1)

        if (call == null) {
            isLoading = false
            refreshLayout.isRefreshing = false
            return
        }
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (token == this@BasicOnScrollListener.token && page == currentPage) {
                    try {
                        onResult(response.body())
                        currentPage++
                    } catch (e: Throwable) {
                        onFailure(call, e)
                    }

                }

                isLoading = false
                refreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                isLoading = false
                refreshLayout.isRefreshing = false
                onExceptionCaught(t)
            }
        })
    }

    fun onExceptionCaught(t: Throwable) {

    }

    @Throws(Exception::class)
    open fun onResult(response: ResponseBody?) {

    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (!isLoading && canLoadMore(recyclerView)) {
            onLoad(token = System.currentTimeMillis())
            isLoading = true
        }
    }

    fun canLoadMore(recyclerView: RecyclerView?): Boolean {
        val mLayoutManager = layoutManager
        val visibleItemCount = recyclerView!!.childCount
        val totalItemCount = mLayoutManager.itemCount
        var firstVisibleItem = 0
        if (mLayoutManager is StaggeredGridLayoutManager) {
            firstVisibleItem = mLayoutManager.findFirstVisibleItemPositions(null)[0]
        } else if (mLayoutManager is GridLayoutManager) {
            firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
        } else if (mLayoutManager is LinearLayoutManager) {
            firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
        }

        return totalItemCount - visibleItemCount <= firstVisibleItem + this.loadThreshold
    }
}
