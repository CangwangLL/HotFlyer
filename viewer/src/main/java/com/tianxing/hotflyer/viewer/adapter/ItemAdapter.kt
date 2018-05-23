package com.tianxing.hotflyer.viewer.adapter

import android.support.v7.widget.RecyclerView

/**
 * Project: JAViewer
 */

abstract class ItemAdapter<I, VH : RecyclerView.ViewHolder>(private val items: MutableList<I>) : RecyclerView.Adapter<VH>() {

    fun getItems(): MutableList<I> {
        return items
    }

    fun setItems(items: List<I>) {
        val size = this.getItems().size
        if (size > 0) {
            this.getItems().clear()
            notifyItemRangeRemoved(0, size)
        }
        this.getItems().addAll(items)
        notifyItemRangeInserted(0, items.size)
    }

    override fun getItemCount(): Int {
        return getItems().size
    }

    override fun onViewDetachedFromWindow(holder: VH?) {
        holder!!.itemView.clearAnimation()
        super.onViewDetachedFromWindow(holder)
    }
}
