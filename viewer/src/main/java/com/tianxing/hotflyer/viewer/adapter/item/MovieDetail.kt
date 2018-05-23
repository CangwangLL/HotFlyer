package com.tianxing.hotflyer.viewer.adapter.item

import java.util.ArrayList

/**
 * Project: JAViewer
 */
class MovieDetail {

    var title: String? = null

    var coverUrl: String? = null

    val screenshots: MutableList<Screenshot> = ArrayList()

    var headers: MutableList<Header> = ArrayList()

    var genres: MutableList<Genre> = ArrayList()

    var actresses: MutableList<Actress> = ArrayList()

    class Header : Linkable() {
        lateinit var name: String
        lateinit var value: String

        override fun toString(): String {
            return "Header{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}'
        }

        companion object {

            fun create(name: String, value: String, link: String): Header {
                val header = Header()
                header.name = name
                header.value = value
                header.link = link
                return header
            }
        }
    }
}
