package com.tianxing.hotflyer.viewer.adapter.item

/**
 * Project: JAViewer
 */
class Movie : Linkable() {

    lateinit var title: String
    lateinit var code: String
    lateinit var coverUrl: String
    lateinit var date: String
    var isHot: Boolean = false

    override fun toString(): String {
        return "Movie{" +
                "title='" + title + '\'' +
                ", code='" + code + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", date='" + date + '\'' +
                ", hot=" + isHot +
                '}'
    }

    override fun equals(obj: Any?): Boolean {
        if (super.equals(obj)) {
            return true
        }

        return if (obj is Movie) {
            this.code == obj.code
        } else false

    }

    companion object {

        fun create(title: String, code: String, date: String, coverUrl: String, detailUrl: String, hot: Boolean): Movie {
            val movie = Movie()
            movie.title = title
            movie.date = date
            movie.code = code
            movie.coverUrl = coverUrl
            movie.isHot = hot
            movie.link = detailUrl
            return movie
        }
    }
}
