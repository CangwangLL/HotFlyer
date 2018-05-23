package com.tianxing.hotflyer.viewer.fragment

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.view.ViewUtil
import com.tianxing.hotflyer.viewer.view.listener.BasicOnScrollListener

/**
 * Project: JAViewer
 */
abstract class RecyclerFragment<I, LM : RecyclerView.LayoutManager> : Fragment() {

    protected var mRecyclerView: RecyclerView? = null

    lateinit var mRefreshLayout: SwipeRefreshLayout

    /*@BindView(R.id.adView)
    protected AdView mAdView;*/

    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null
//        set(listener) {
//            mRefreshLayout.setOnRefreshListener(listener)
//        }

    fun setRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener?){
        onRefreshListener = listener
        mRefreshLayout.setOnRefreshListener(listener)
    }

    var onScrollListener: BasicOnScrollListener<*>? = null
        private set

    var items = ArrayList<I>()
        set(items) {
            val size = items.size
            if (size > 0) {
                items.clear()
                adapter!!.notifyDataSetChanged()
            }

            items.addAll(items)
            adapter!!.notifyDataSetChanged()
        }

    var layoutManager: LM
        get() = this.mRecyclerView!!.layoutManager as LM
        set(mLayoutManager) {
            this.mRecyclerView!!.layoutManager = mLayoutManager
        }

    var adapter: RecyclerView.Adapter<*>?
        get() = if (this.mRecyclerView == null) {
            null
        } else this.mRecyclerView!!.adapter
        set(mAdapter) {
            this.mRecyclerView!!.adapter = mAdapter
        }

    protected fun setRecyclerViewPadding(dp: Int) {
        this.mRecyclerView!!.setPadding(
                ViewUtil.dpToPx(dp),
                ViewUtil.dpToPx(dp),
                ViewUtil.dpToPx(dp),
                ViewUtil.dpToPx(dp)
        )
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_recycler, container, false)
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mRefreshLayout = view.findViewById(R.id.refresh_layout)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this.context, R.color.googleBlue),
                ContextCompat.getColor(this.context, R.color.googleGreen),
                ContextCompat.getColor(this.context, R.color.googleRed),
                ContextCompat.getColor(this.context, R.color.googleYellow)
        )

        if (savedInstanceState != null) {
            this.layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable<Parcelable>("LayoutManagerState"))
            this.items = savedInstanceState.getSerializable("Items") as ArrayList<I>
            if (this.onScrollListener != null) {
                this.onScrollListener!!.restoreState(savedInstanceState.getBundle("ScrollListenerState"))
            }
        }
    }

    fun addOnScrollListener(listener: BasicOnScrollListener<*>) {
        this.onScrollListener = listener
        mRecyclerView!!.addOnScrollListener(listener)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putSerializable("Items", this.items)
        outState.putParcelable("LayoutManagerState", layoutManager.onSaveInstanceState())

        if (this.onScrollListener != null) {
            outState.putBundle("ScrollListenerState", onScrollListener!!.saveState())
        }

        super.onSaveInstanceState(outState)
    }
}// Required empty public constructor
