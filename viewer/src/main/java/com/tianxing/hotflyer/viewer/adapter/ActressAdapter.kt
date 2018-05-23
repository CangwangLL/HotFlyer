package com.tianxing.hotflyer.viewer.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import butterknife.ButterKnife

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.item.Actress
import com.tianxing.hotflyer.viewer.view.SquareTopCrop
import com.tianxing.hotflyer.viewer.view.listener.ActressClickListener
import com.tianxing.hotflyer.viewer.view.listener.ActressLongClickListener

import com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE

/**
 * Project: JAViewer
 */
class ActressAdapter(actresses: MutableList<Actress>, private val mParentActivity: Activity) : ItemAdapter<Actress, ActressAdapter.ViewHolder>(actresses) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_actress, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val actress = getItems()[position]

        holder.parse(actress)

        holder.mLayout.setOnClickListener(ActressClickListener(actress, mParentActivity))
        holder.mLayout.setOnLongClickListener(ActressLongClickListener(actress, mParentActivity))

        holder.mImage.setImageDrawable(null)
        Glide.with(holder.mImage.context.applicationContext)
                .load(actress.imageUrl)
                .placeholder(R.drawable.ic_movie_actresses)
                .diskCacheStrategy(SOURCE) // override default RESULT cache and apply transform always
                .skipMemoryCache(true) // do not reuse the transformed result while running
                .transform(SquareTopCrop(holder.mImage.context))
                .dontAnimate()
                .into(holder.mImage)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mTextName: TextView

        var mImage: ImageView

        var mLayout: View

        fun parse(actress: Actress) {
            mTextName.text = actress.name
            mTextName.isSelected = true
        }

        init {
            mTextName = view.findViewById(R.id.actress_name)
            mImage = view.findViewById(R.id.actress_img)
            mLayout = view.findViewById(R.id.layout_actress)
            ButterKnife.bind(this, view)
        }
    }
}
