package com.tianxing.hotflyer.viewer.adapter.item

import com.tianxing.hotflyer.viewer.JAViewer.Companion.Objects_equals
import java.io.Serializable

/**
 * Project: JAViewer
 */
open class Linkable : Serializable {

    var link: String? = null

    override fun equals(linkable: Any?): Boolean {
        return if (linkable !is Linkable) {
            false
        } else Objects_equals(link, linkable.link!!)

    }

    override fun toString(): String {
        return "Linkable{" +
                "link='" + link + '\'' +
                '}'
    }
}
