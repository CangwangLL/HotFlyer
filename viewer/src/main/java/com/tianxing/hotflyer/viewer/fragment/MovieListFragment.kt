package com.tianxing.hotflyer.viewer.fragment

import android.os.Bundle

import com.tianxing.hotflyer.viewer.JAViewer

import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Project: JAViewer
 */
class MovieListFragment : MovieFragment() {

    var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        this.link = bundle.getString("link")
    }

    override fun newCall(page: Int): Call<ResponseBody> {
        return JAViewer.SERVICE.get(this.link + "/page/" + page)
    }
}
