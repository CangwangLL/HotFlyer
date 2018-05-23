package com.tianxing.hotflyer.viewer.fragment.favourite

import android.support.v7.widget.RecyclerView

import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.adapter.ActressAdapter
import com.tianxing.hotflyer.viewer.adapter.ItemAdapter
import com.tianxing.hotflyer.viewer.view.decoration.ActressItemDecoration

/**
 * Project: JAViewer
 */

class FavouriteActressFragment : FavouriteFragment() {
    override fun adapter(): ItemAdapter<*, *> {
        return ActressAdapter(JAViewer.CONFIGURATIONS!!.starredActresses, this.activity)
    }

    override fun decoration(): RecyclerView.ItemDecoration? {
        return ActressItemDecoration()
    }
}
