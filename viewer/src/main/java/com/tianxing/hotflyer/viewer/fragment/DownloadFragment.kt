package com.tianxing.hotflyer.viewer.fragment


import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.tianxing.hotflyer.viewer.adapter.DownloadLinkAdapter
import com.tianxing.hotflyer.viewer.adapter.item.DownloadLink
import com.tianxing.hotflyer.viewer.network.provider.DownloadLinkProvider
import com.tianxing.hotflyer.viewer.view.decoration.DownloadItemDecoration
import com.tianxing.hotflyer.viewer.view.listener.BasicOnScrollListener

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import okhttp3.ResponseBody
import retrofit2.Call

class DownloadFragment : RecyclerFragment<DownloadLink, LinearLayoutManager>() {

    var provider: DownloadLinkProvider? = null

    var keyword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        this.provider = DownloadLinkProvider.getProvider(bundle.getString("provider"))
        this.keyword = bundle.getString("keyword")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        /*if (JAViewer.CONFIGURATIONS.showAds()) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("52546C5153814CA9A9714647F5960AFE")
                    .build();
            mAdView.loadAd(adRequest);
        }*/

        this.layoutManager = LinearLayoutManager(this.context)
        this.adapter = ScaleInAnimationAdapter(DownloadLinkAdapter(this.items, this.activity, provider!!))
        mRecyclerView!!.addItemDecoration(DownloadItemDecoration())

        val animator = SlideInUpAnimator()
        animator.addDuration = 300
        mRecyclerView!!.itemAnimator = animator

//        this.onRefreshListener = SwipeRefreshLayout.OnRefreshListener { onScrollListener!!.refresh() }

        setRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            onScrollListener!!.refresh()
        })

        this.addOnScrollListener(object : BasicOnScrollListener<DownloadLink>() {

            override val layoutManager: RecyclerView.LayoutManager
                get() = this@DownloadFragment.layoutManager

            override val refreshLayout: SwipeRefreshLayout
                get() = this@DownloadFragment.mRefreshLayout

            override val adapter: RecyclerView.Adapter<*>?
                get() = this@DownloadFragment.adapter

            override val items: MutableList<DownloadLink>
                get() = this@DownloadFragment.items

            override fun newCall(page: Int): Call<ResponseBody>? {
                return this@DownloadFragment.newCall(page)
            }

            @Throws(Exception::class)
            override fun onResult(response: ResponseBody?) {
                super.onResult(response)
                val downloads = provider!!.parseDownloadLinks(response!!.string())

                val pos = items.size

                if (downloads.isEmpty()) {
                    isEnd = true
                } else {
                    items.addAll(downloads)
                    adapter!!.notifyItemRangeInserted(pos, downloads.size)
                }
            }
        })

        mRefreshLayout.post {
            mRefreshLayout.isRefreshing = true
            onRefreshListener!!.onRefresh()
        }

        super.onActivityCreated(savedInstanceState)
    }

    fun newCall(page: Int): Call<ResponseBody>? {
        return this.provider!!.search(keyword!!, page)
    }
}// Required empty public constructor
