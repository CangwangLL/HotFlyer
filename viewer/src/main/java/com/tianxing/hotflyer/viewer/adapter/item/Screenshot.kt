package com.tianxing.hotflyer.viewer.adapter.item

/**
 * Project: JAViewer
 */
class Screenshot : Linkable() {

    lateinit var thumbnailUrl: String
        protected set

    val imageUrl: String?
        get() = link

    companion object {

        fun create(thumbnailUrl: String, imageUrl: String): Screenshot {
            val screenshot = Screenshot()
            screenshot.thumbnailUrl = thumbnailUrl
            screenshot.link = imageUrl
            return screenshot
        }
    }
}
