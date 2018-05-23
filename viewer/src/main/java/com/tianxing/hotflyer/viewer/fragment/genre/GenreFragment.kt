package com.tianxing.hotflyer.viewer.fragment.genre


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife

import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.GenreAdapter
import com.tianxing.hotflyer.viewer.adapter.item.Genre
import com.tianxing.hotflyer.viewer.view.ViewUtil

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import okhttp3.ResponseBody
import retrofit2.Call

class GenreFragment : Fragment() {

    var genres: MutableList<Genre> = ArrayList()
        protected set

    lateinit var mRecyclerView: RecyclerView

    var adapter: RecyclerView.Adapter<*>? = null
        private set
    private val mLayoutManager: StaggeredGridLayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_genre_list, container, false)
        mRecyclerView = view.findViewById(R.id.genre_recycler_view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.mRecyclerView.setPadding(
                ViewUtil.dpToPx(1),
                ViewUtil.dpToPx(1),
                ViewUtil.dpToPx(1),
                ViewUtil.dpToPx(1)
        )

        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = GenreAdapter(genres, this.activity)
        mRecyclerView.adapter = adapter

        val animator = SlideInUpAnimator()
        animator.addDuration = 300
        mRecyclerView.itemAnimator = animator

        this.adapter!!.notifyItemRangeInserted(0, this.adapter!!.itemCount)
    }

    fun getCall(page: Int): Call<ResponseBody> {
        return JAViewer.SERVICE.getActresses(page)
    }
}// Required empty public constructor
