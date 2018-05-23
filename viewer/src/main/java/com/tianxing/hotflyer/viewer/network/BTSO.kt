package com.tianxing.hotflyer.viewer.network

import com.tianxing.hotflyer.viewer.JAViewer

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * Project: JAViewer
 */
interface BTSO {

    @GET("/search/{keyword}/page/{page}")
    @Headers("Accept-Language: zh-CN,zh;q=0.8,en;q=0.6")
    fun search(@Path(value = "keyword") keyword: String, @Path("page") page: Int): Call<ResponseBody>

    @GET
    @Headers("Accept-Language: zh-CN,zh;q=0.8,en;q=0.6")
    operator fun get(@Url url: String): Call<ResponseBody>

    companion object {

        val INSTANCE = Retrofit.Builder()
                .baseUrl(BTSO.BASE_URL)
                .client(JAViewer.HTTP_CLIENT)
                .build()
                .create(BTSO::class.java)

        val BASE_URL = "https://btso.pw"
    }
}
