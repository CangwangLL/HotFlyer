package com.tianxing.hotflyer.viewer.network.provider

import com.tianxing.hotflyer.viewer.adapter.item.DownloadLink
import com.tianxing.hotflyer.viewer.adapter.item.MagnetLink

import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Project: JAViewer
 */
abstract class DownloadLinkProvider {
    abstract fun search(keyword: String, page: Int): Call<ResponseBody>?

    abstract fun parseDownloadLinks(htmlContent: String): List<DownloadLink>

    abstract operator fun get(url: String): Call<ResponseBody>?

    abstract fun parseMagnetLink(htmlContent: String): MagnetLink?

    companion object {

        fun getProvider(name: String): DownloadLinkProvider? {
            when (name.toLowerCase().trim { it <= ' ' }) {
                "btso" -> return BTSOLinkProvider()
                "torrentkitty" -> return TorrentKittyLinkProvider()
                else -> return null
            }
        }
    }
}
