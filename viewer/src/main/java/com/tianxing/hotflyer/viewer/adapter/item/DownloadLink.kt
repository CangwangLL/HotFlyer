package com.tianxing.hotflyer.viewer.adapter.item

/**
 * Project: JAViewer
 */
class DownloadLink : Linkable() {
    lateinit var title: String
        protected set
    lateinit var size: String
        protected set
    lateinit var date: String
        protected set
    lateinit var magnetLink: MagnetLink

    fun hasMagnetLink(): Boolean {
        return magnetLink.magnetLink != null
    }

    fun getMagnetLink(): String {
        return magnetLink.magnetLink
    }

    companion object {

        fun create(title: String, size: String, date: String, link: String, magnetLink: String): DownloadLink {
            val download = DownloadLink()
            download.title = title
            download.size = size
            download.date = date
            download.link = link
            download.magnetLink = MagnetLink.create(magnetLink)
            return download
        }
    }
}
