package com.tianxing.hotflyer.viewer.network.provider

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.util.ArrayList

import com.tianxing.hotflyer.viewer.adapter.item.DownloadLink
import com.tianxing.hotflyer.viewer.adapter.item.MagnetLink
import com.tianxing.hotflyer.viewer.network.BTSO

import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Project: JAViewer
 */
class BTSOLinkProvider : DownloadLinkProvider() {


    override fun search(keyword: String, page: Int): Call<ResponseBody> {
        return BTSO.INSTANCE.search(keyword, page)
    }

    override fun parseDownloadLinks(htmlContent: String): List<DownloadLink> {
        val links = ArrayList<DownloadLink>()
        val rows = Jsoup.parse(htmlContent).getElementsByClass("row")
        for (row in rows) {
            try {
                val a = row.getElementsByTag("a").first()
                links.add(
                        DownloadLink.create(
                                row.getElementsByClass("file").first().text(),
                                row.getElementsByClass("size").first().text(),
                                row.getElementsByClass("date").first().text(),
                                a.attr("href"),
                                "")
                )
            } catch (ignored: Exception) {

            }

        }
        return links
    }

    override fun get(url: String): Call<ResponseBody> {
        return BTSO.INSTANCE.get(url)
    }

    override fun parseMagnetLink(htmlContent: String): MagnetLink {
        return MagnetLink.create(Jsoup.parse(htmlContent).getElementsByClass("magnet-link").first().text())
    }
}
