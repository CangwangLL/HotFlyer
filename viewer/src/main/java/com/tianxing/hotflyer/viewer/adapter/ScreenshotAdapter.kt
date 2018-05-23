package com.tianxing.hotflyer.viewer.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide

import butterknife.BindView
import butterknife.ButterKnife

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.activity.GalleryActivity
import com.tianxing.hotflyer.viewer.adapter.item.Movie
import com.tianxing.hotflyer.viewer.adapter.item.Screenshot
import com.tianxing.hotflyer.viewer.view.ViewUtil

/**
 * Project: JAViewer
 */

class ScreenshotAdapter(private val screenshots: List<Screenshot>?, private val mParentActivity: Activity, private val mIcon: ImageView, private val movie: Movie) : RecyclerView.Adapter<ScreenshotAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_screenshot, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val screenshot = screenshots!![position]

        holder.mImage.setImageDrawable(null)
        Glide.with(holder.mImage.context.applicationContext)
                .load(screenshot.thumbnailUrl)
                .into(holder.mImage)

        holder.mImage.setOnClickListener {
            val i = Intent(mParentActivity, GalleryActivity::class.java)
            val bundle = Bundle()

            val urls = arrayOfNulls<String>(screenshots.size)
            for (k in screenshots.indices) {
                urls[k] = screenshots[k].imageUrl
            }
            bundle.putStringArray("urls", urls)
            bundle.putSerializable("movie", movie)
            bundle.putInt("position", holder.adapterPosition)
            i.putExtras(bundle)
            mParentActivity.startActivity(i)
        }

        if (position == 0) {
            ViewUtil.alignIconToView(mIcon, holder.mImage)
        }
    }

    override fun getItemCount(): Int {
        return screenshots?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mImage: ImageView

        init {
            mImage = view.findViewById(R.id.screenshot_image_view)
        }
    }
}
