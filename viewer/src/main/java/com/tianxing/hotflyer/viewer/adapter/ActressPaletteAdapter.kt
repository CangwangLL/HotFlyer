package com.tianxing.hotflyer.viewer.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.item.Actress
import com.tianxing.hotflyer.viewer.view.SquareTopCrop
import com.tianxing.hotflyer.viewer.view.ViewUtil
import com.tianxing.hotflyer.viewer.view.listener.ActressClickListener
import com.tianxing.hotflyer.viewer.view.listener.ActressLongClickListener

import com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE

/**
 * Project: JAViewer
 */

class ActressPaletteAdapter(private val actresses: List<Actress>?, private val mParentActivity: Activity, private val mIcon: ImageView) : RecyclerView.Adapter<ActressPaletteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_actress_palette, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actress = actresses!![position]

        holder.mCard.setOnClickListener(ActressClickListener(actress, mParentActivity))
        holder.mCard.setOnLongClickListener(ActressLongClickListener(actress, mParentActivity))

        holder.mName.text = actress.name

        if (position == 0) {
            ViewUtil.alignIconToView(mIcon, holder.mImage)
        }

        holder.mImage.setImageResource(R.drawable.ic_movie_actresses)

        if (actress.imageUrl.trim { it <= ' ' }.isEmpty()) {
            return
        }

        Glide.with(holder.mImage.context.applicationContext)
                .load(actress.imageUrl)
                .asBitmap()
                .placeholder(R.drawable.ic_movie_actresses)
                .diskCacheStrategy(SOURCE) // override default RESULT cache and apply transform always
                .skipMemoryCache(true) // do not reuse the transformed result while running
                .transform(SquareTopCrop(holder.mImage.context))
                //.transform(new PositionedCropTransformation(holder.mImage.getContext(), 0, 0))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                        //resource = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getWidth());
                        holder.mImage.setImageBitmap(resource)

                        try {
                            Palette.from(resource).generate(Palette.PaletteAsyncListener { palette ->
                                val swatch = palette.lightVibrantSwatch ?: return@PaletteAsyncListener
                                holder.mCard.setCardBackgroundColor(swatch.rgb)
                                holder.mName.setTextColor(swatch.bodyTextColor)
                            })
                        } catch (ignored: Exception) {
                        }

                    }
                })

    }

    override fun getItemCount(): Int {
        return actresses?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mImage: ImageView

        var mName: TextView

        var mCard: CardView

        init {
            mImage = view.findViewById(R.id.actress_palette_img)
            mName = view.findViewById(R.id.actress_palette_name)
            mCard = view.findViewById(R.id.card_actress_palette)
        }
    }
}
