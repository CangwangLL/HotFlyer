package com.tianxing.hotflyer.viewer.fragment


import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.adapter.ActressAdapter
import com.tianxing.hotflyer.viewer.adapter.item.Actress
import com.tianxing.hotflyer.viewer.network.provider.AVMOProvider
import com.tianxing.hotflyer.viewer.view.decoration.ActressItemDecoration
import com.tianxing.hotflyer.viewer.view.listener.EndlessOnScrollListener

import okhttp3.ResponseBody
import retrofit2.Call

class ActressesFragment : RecyclerFragment<Actress, LinearLayoutManager>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        this.layoutManager = LinearLayoutManager(this.context)
        //this.setAdapter(new SlideInBottomAnimationAdapter(new ActressAdapter(getItems(), this.getActivity())));
        this.adapter = ActressAdapter(items, this.activity)

        mRecyclerView!!.addItemDecoration(ActressItemDecoration())

        /*RecyclerView.ItemAnimator animator = new SlideInUpAnimator();
        animator.setAddDuration(300);
        mRecyclerView.setItemAnimator(animator);*/

//        this.onRefreshListener = SwipeRefreshLayout.OnRefreshListener { onScrollListener!!.refresh() }

        setRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            onScrollListener!!.refresh()
        })

        this.addOnScrollListener(object : EndlessOnScrollListener<Actress>() {

            override val layoutManager: RecyclerView.LayoutManager
                get() = this@ActressesFragment.layoutManager

            override val refreshLayout: SwipeRefreshLayout
                get() = this@ActressesFragment.mRefreshLayout

            override val adapter: RecyclerView.Adapter<*>?
                get() = this@ActressesFragment.adapter

            override val items: MutableList<Actress>
                get() = this@ActressesFragment.items

            override fun newCall(page: Int): Call<ResponseBody>? {
                return this@ActressesFragment.newCall(page)
            }

            @Throws(Exception::class)
            override fun onResult(response: ResponseBody?) {
                super.onResult(response)
                val wrappers = AVMOProvider.parseActresses(response!!.string())

                val pos = items.size

                items.addAll(wrappers)
                adapter?.notifyItemRangeInserted(pos, wrappers.size)
            }
        })

        mRefreshLayout.post {
            mRefreshLayout.isRefreshing = true
            onRefreshListener?.onRefresh()
        }

        super.onActivityCreated(savedInstanceState)
    }

    fun newCall(page: Int): Call<ResponseBody> {
        return JAViewer.SERVICE.getActresses(page)
    }
}// Required empty public constructor
