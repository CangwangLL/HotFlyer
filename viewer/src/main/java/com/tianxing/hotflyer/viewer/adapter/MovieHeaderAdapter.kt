package com.tianxing.hotflyer.viewer.adapter

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.activity.MovieListActivity
import com.tianxing.hotflyer.viewer.adapter.item.MovieDetail
import com.tianxing.hotflyer.viewer.view.ViewUtil

/**
 * Project: JAViewer
 */

class MovieHeaderAdapter(private val headers: List<MovieDetail.Header>?, private val mParentActivity: Activity, private val mIcon: ImageView) : RecyclerView.Adapter<MovieHeaderAdapter.ViewHolder>() {

    var first = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_header, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val header = headers!![position]

        if (header.name != null && header.value != null) {
            holder.itemView.setOnClickListener {
                val clip = mParentActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clip.primaryClip = ClipData.newPlainText(header.name, header.value)
                Toast.makeText(mParentActivity, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
            }

            holder.mHeaderName.text = header.name
            holder.mHeaderValue.text = header.value

            if (header.link != null) {
                holder.mHeaderValue.paintFlags = holder.mHeaderValue.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                holder.mHeaderValue.setTextColor(ResourcesCompat.getColor(this.mParentActivity.resources, R.color.colorAccent, null))
                holder.mHeaderValue.setOnClickListener {
                    val intent = Intent(mParentActivity, MovieListActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("title", header.name + " " + header.value)
                    bundle.putString("link", header.link)
                    intent.putExtras(bundle)
                    mParentActivity.startActivity(intent)
                }
            }

            if (first) {
                ViewUtil.alignIconToView(mIcon, holder.mHeaderName)
                first = false
            }
        }
    }

    override fun getItemCount(): Int {
        return headers?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mHeaderName: TextView

        var mHeaderValue: TextView

        init {
            mHeaderName = view.findViewById(R.id.header_name)
            mHeaderValue = view.findViewById(R.id.header_value)
        }
    }
}
