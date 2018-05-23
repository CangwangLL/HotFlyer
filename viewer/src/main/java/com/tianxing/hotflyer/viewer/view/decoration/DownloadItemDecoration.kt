package com.tianxing.hotflyer.viewer.view.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

import com.tianxing.hotflyer.viewer.view.ViewUtil

/**
 * Project: JAViewer
 */

class DownloadItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val rect = Rect()
        if (parent.getChildAdapterPosition(view) == 0) {
            rect.top = ViewUtil.dpToPx(8)
        }
        outRect.set(rect)
    }
}
