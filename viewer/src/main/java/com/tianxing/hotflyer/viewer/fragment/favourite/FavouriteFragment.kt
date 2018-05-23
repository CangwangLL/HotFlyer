package com.tianxing.hotflyer.viewer.fragment.favourite

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.tianxing.hotflyer.viewer.adapter.ItemAdapter
import com.tianxing.hotflyer.viewer.adapter.item.Movie
import com.tianxing.hotflyer.viewer.fragment.RecyclerFragment

/**
 * Project: JAViewer
 */

abstract class FavouriteFragment : RecyclerFragment<Movie, LinearLayoutManager>() {

    fun update() {
        val adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.layoutManager = LinearLayoutManager(this.context)
        this.adapter = adapter()
        //this.setAdapter(adapter =
        mRefreshLayout.isEnabled = false

        if (decoration() != null) {
            mRecyclerView!!.addItemDecoration(decoration())
        }

        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun adapter(): ItemAdapter<*, *>

    open fun decoration(): RecyclerView.ItemDecoration? {
        return null
    }
}
