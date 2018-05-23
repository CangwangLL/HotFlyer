package com.tianxing.hotflyer.viewer

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.tianxing.hotflyer.viewer.adapter.item.Actress
import com.tianxing.hotflyer.viewer.adapter.item.DataSource
import com.tianxing.hotflyer.viewer.adapter.item.Movie

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.ArrayList

/**
 * Project: JAViewer
 */

class Configurations {

    private var starred_movies: ArrayList<Movie>? = null

    private var starred_actresses: ArrayList<Actress>? = null

    var dataSource: DataSource? = null
        get() {
            if (field == null && JAViewer.DATA_SOURCES.size > 0) {
                dataSource = JAViewer.DATA_SOURCES[0]
            }
            return field
        }

    private var show_ads: Boolean = false

    var downloadCounter: Long = 0

    val starredMovies: ArrayList<Movie>
        get() {
            if (starred_movies == null) {
                starred_movies = ArrayList()
            }
            return starred_movies!!
        }

    val starredActresses: ArrayList<Actress>
        get() {
            if (starred_actresses == null) {
                starred_actresses = ArrayList()
            }
            return starred_actresses!!
        }

    fun save() {
        try {
            val writer = FileWriter(file!!)
            Gson().toJson(this, writer)
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun setShowAds(show_ads: Boolean) {
        this.show_ads = show_ads
    }

    fun showAds(): Boolean {
        return show_ads
    }

    companion object {

        private var file: File? = null

        fun load(file: File): Configurations {
            Configurations.file = file
            var config: Configurations? = null
            try {
                config = JAViewer.parseJson(Configurations::class.java, JsonReader(FileReader(file)))
            } catch (ignored: Exception) {
            }

            if (config == null) {
                config = Configurations()
            }

            return config
        }
    }
}
