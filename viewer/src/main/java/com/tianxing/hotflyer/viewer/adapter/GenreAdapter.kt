package com.tianxing.hotflyer.viewer.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.activity.MovieListActivity
import com.tianxing.hotflyer.viewer.adapter.item.Genre

/**
 * Project: JAViewer
 */
class GenreAdapter(private val genres: List<Genre>?, private val mParentActivity: Activity) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_genre, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val genre = genres!![position]
        holder.parse(genre)

        holder.mCard.setOnClickListener {
            if (genre.link != null) {
                val intent = Intent(mParentActivity, MovieListActivity::class.java)
                val bundle = Bundle()
                bundle.putString("title", genre.name)
                bundle.putString("link", genre.link)

                intent.putExtras(bundle)

                mParentActivity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return genres?.size ?: 0
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mTextName: TextView

        var mCard: CardView

        fun parse(genre: Genre) {
            mTextName.text = genre.name
        }

        init {
            mTextName = view.findViewById(R.id.genre_name)
            mCard = view.findViewById(R.id.card_genre)
        }
    }
}
