package com.tianxing.hotflyer.viewer.adapter.item

import java.io.Serializable

/**
 * Project: JAViewer
 */
class MagnetLink : Serializable {

    lateinit var magnetLink: String
        protected set

    companion object {

        fun create(magnetLink: String?): MagnetLink {
            val magnet = MagnetLink()
            if (magnetLink != null) {
                magnet.magnetLink = magnetLink.substring(0, magnetLink.indexOf("&"))
            }
            return magnet
        }
    }
}
