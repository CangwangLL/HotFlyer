package com.tianxing.hotflyer.viewer.fragment.favourite

import android.support.v7.widget.RecyclerView

import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.adapter.ItemAdapter
import com.tianxing.hotflyer.viewer.adapter.MovieAdapter
import com.tianxing.hotflyer.viewer.view.decoration.MovieItemDecoration

/**
 * Project: JAViewer
 */

class FavouriteMovieFragment : FavouriteFragment() {
    override fun adapter(): ItemAdapter<*, *> {
        return MovieAdapter(JAViewer.CONFIGURATIONS!!.starredMovies, this.activity)
    }

    override fun decoration(): RecyclerView.ItemDecoration? {
        return MovieItemDecoration()
    }
}
