package com.tianxing.hotflyer.viewer.network.provider

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import java.util.ArrayList

import com.tianxing.hotflyer.viewer.adapter.item.DownloadLink
import com.tianxing.hotflyer.viewer.adapter.item.MagnetLink
import com.tianxing.hotflyer.viewer.network.TorrentKitty

import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Project: JAViewer
 */
class TorrentKittyLinkProvider : DownloadLinkProvider() {


    override fun search(keyword: String, page: Int): Call<ResponseBody>? {
        return if (page == 1) {
            TorrentKitty.INSTANCE.search(keyword)
        } else {
            null
        }
    }

    override fun parseDownloadLinks(htmlContent: String): List<DownloadLink> {
        val links = ArrayList<DownloadLink>()
        val table = Jsoup.parse(htmlContent).getElementById("archiveResult")
        for (tr in table.getElementsByTag("tr")) {
            try {
                links.add(DownloadLink.create(
                        tr.getElementsByClass("name").first().text(),
                        "",
                        tr.getElementsByClass("date").first().text(), "",
                        tr.getElementsByAttributeValue("rel", "magnet").first().attr("href")
                ))
            } catch (ignored: Exception) {

            }

        }

        return links
    }

    override fun get(url: String): Call<ResponseBody>? {
        return null
        //ABANDONED
    }

    override fun parseMagnetLink(htmlContent: String): MagnetLink? {
        return null
        //ABANDONED
    }
}
