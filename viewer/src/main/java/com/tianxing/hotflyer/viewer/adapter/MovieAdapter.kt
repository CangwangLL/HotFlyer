package com.tianxing.hotflyer.viewer.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.activity.MovieActivity
import com.tianxing.hotflyer.viewer.adapter.item.Movie

/**
 * Project: JAViewer
 */

class MovieAdapter(movies: MutableList<Movie>, private val mParentActivity: Activity) : ItemAdapter<Movie, MovieAdapter.ViewHolder>(movies) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_movie, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItems()[position]

        holder.parse(movie)

        holder.mCard.setOnClickListener {
            val intent = Intent(mParentActivity, MovieActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("movie", movie)
            intent.putExtras(bundle)

            mParentActivity.startActivity(intent)
        }

        holder.mImageCover.setImageDrawable(null)
        Glide.with(holder.mImageCover.context.applicationContext)
                .load(movie.coverUrl)
                .into(holder.mImageCover)

        holder.mImageHot.visibility = if (movie.isHot) View.VISIBLE else View.GONE
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mTextTitle: TextView

        var mTextCode: TextView

        var mTextDate: TextView

        var mImageCover: ImageView

        var mImageHot: ImageView

        var mCard: CardView

        fun parse(movie: Movie) {
            mTextCode.text = movie.code
            mTextTitle.text = movie.title
            mTextDate.text = movie.date
        }

        init {
            mTextTitle = view.findViewById(R.id.movie_title)
            mTextCode = view.findViewById(R.id.movie_size)
            mTextDate = view.findViewById(R.id.movie_date)
            mImageCover = view.findViewById(R.id.movie_cover)
            mImageHot = view.findViewById(R.id.movie_hot)
            mCard = view.findViewById(R.id.card_movie)
        }
    }
}
