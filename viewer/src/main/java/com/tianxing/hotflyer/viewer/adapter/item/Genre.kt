package com.tianxing.hotflyer.viewer.adapter.item

/**
 * Project: JAViewer
 */
class Genre : Linkable() {
    lateinit var name: String

    override fun toString(): String {
        return name + ":" + link
    }

    companion object {

        fun create(name: String, link: String): Genre {
            val genre = Genre()
            genre.name = name
            genre.link = link
            return genre
        }
    }
}
