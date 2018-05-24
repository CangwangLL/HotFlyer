package com.tianxing.hotflyer.viewer.adapter

import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.item.DownloadLink
import com.tianxing.hotflyer.viewer.network.provider.DownloadLinkProvider

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Project: JAViewer
 */
class DownloadLinkAdapter(links: MutableList<DownloadLink>, private val mParentActivity: Activity, private val provider: DownloadLinkProvider) : ItemAdapter<DownloadLink, DownloadLinkAdapter.ViewHolder>(links) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_download, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val link = getItems()[position]

        holder.parse(link)

        holder.mView.setOnClickListener {
            if (!link.hasMagnetLink()) {
                val mDialog: ProgressDialog
                mDialog = ProgressDialog(mParentActivity)
                mDialog.setTitle("请稍后")
                mDialog.setMessage("正在获取磁力链接")
                mDialog.isIndeterminate = false
                mDialog.setCancelable(false)
                mDialog.show()

                val call = provider.get(link.link!!)
                call?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        try {
                            val magnetLink = provider.parseMagnetLink(response.body()!!.string())
                            onMagnetGet(magnetLink!!.magnetLink)
                        } catch (e: Throwable) {
                            onFailure(call, e)
                        }

                        mDialog.dismiss()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            } else {
                onMagnetGet(link.magnetLink.toString())
            }
        }
    }

    fun onMagnetGet(magnetLink: String) {
        if (!magnetLink.isEmpty()) {
            val clip = mParentActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clip.primaryClip = ClipData.newPlainText("magnet-link", magnetLink)
            Toast.makeText(mParentActivity, "磁力链接：$magnetLink 已复制到剪贴板", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(mParentActivity, "磁力链接获取失败", Toast.LENGTH_SHORT).show()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mTextTitle: TextView

        var mTextSize: TextView

        var mTextDate: TextView

        var mView: View

        fun parse(link: DownloadLink) {
            mTextSize.text = link.size
            mTextTitle.text = link.title
            mTextDate.text = link.date
        }

        init {
            mTextTitle = view.findViewById(R.id.download_title)
            mTextSize = view.findViewById(R.id.download_size)
            mTextDate = view.findViewById(R.id.download_date)
            mView = view.findViewById(R.id.layout_download)
        }
    }
}
