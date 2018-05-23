package com.tianxing.hotflyer.viewer

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.support.multidex.MultiDex
import android.support.v4.app.Fragment

import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.stream.JsonReader
import com.tianxing.hotflyer.viewer.adapter.item.DataSource
import com.tianxing.hotflyer.viewer.fragment.ActressesFragment
import com.tianxing.hotflyer.viewer.fragment.HomeFragment
import com.tianxing.hotflyer.viewer.fragment.PopularFragment
import com.tianxing.hotflyer.viewer.fragment.ReleasedFragment
import com.tianxing.hotflyer.viewer.fragment.favourite.FavouriteTabsFragment
import com.tianxing.hotflyer.viewer.fragment.genre.GenreTabsFragment
import com.tianxing.hotflyer.viewer.network.BasicService

import java.io.File
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import io.fabric.sdk.android.Fabric
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit

/**
 * Project: JAViewer
 */

open class JAViewer : Application() {

    override fun onCreate() {
        super.onCreate()
        CustomActivityOnCrash.install(this)
        //        Fabric.with(this, new Crashlytics());
    }

    companion object {

        val USER_AGENT = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"

        var CONFIGURATIONS: Configurations? = null

        val DATA_SOURCES: MutableList<DataSource> = ArrayList()

        val FRAGMENTS: Map<Int, Class<out Fragment>> = object : HashMap<Int, Class<out Fragment>>() {
            init {
                put(R.id.nav_home, HomeFragment::class.java)
                put(R.id.nav_popular, PopularFragment::class.java)
                put(R.id.nav_released, ReleasedFragment::class.java)
                put(R.id.nav_actresses, ActressesFragment::class.java)
                put(R.id.nav_genre, GenreTabsFragment::class.java)
                put(R.id.nav_favourite, FavouriteTabsFragment::class.java)
            }
        }

        val dataSource: DataSource?
            get() = JAViewer.CONFIGURATIONS!!.dataSource

        lateinit var SERVICE: BasicService

        fun recreateService() {
            SERVICE = Retrofit.Builder()
                    .baseUrl(JAViewer.dataSource?.link!!)
                    .client(JAViewer.HTTP_CLIENT)
                    .build()
                    .create(BasicService::class.java)
        }

        val storageDir: File
            get() {
                val dir = File(Environment.getExternalStorageDirectory(), "JAViewer/")
                dir.mkdirs()
                return dir
            }

        fun replaceUrl(url: HttpUrl): HttpUrl {
            val builder = url.newBuilder()
            val host = url.url().host
            if (hostReplacements.containsKey(host)) {
                builder.host(hostReplacements[host])
                return builder.build()
            }

            return url
        }

        var hostReplacements: MutableMap<String, String> = HashMap()

        /*static {
        String host;
        try {
            host = new URI(DataSource.AVMO.getLink()).getHost();
            hostReplacements.put("avmo.club", host);
            hostReplacements.put("avmo.pw", host);
            hostReplacements.put("avio.pw", host);

            host = new URI(DataSource.AVSO.getLink()).getHost();
            hostReplacements.put("avso.pw", host);

            host = new URI(DataSource.AVXO.getLink()).getHost();
            hostReplacements.put("avxo.pw", host);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }*/


        val HTTP_CLIENT = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                    .url(replaceUrl(original.url()))
                    .header("User-Agent", USER_AGENT)
                    .build()

            chain.proceed(request)
        }
                .cookieJar(object : CookieJar {
                    private val cookieStore = HashMap<HttpUrl, List<Cookie>>()

                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        cookieStore.put(url, cookies)
                    }

                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        val cookies = cookieStore[url]
                        return cookies ?: ArrayList()
                    }
                })
                .build()

        @Throws(JsonParseException::class)
        fun <T> parseJson(beanClass: Class<T>, reader: JsonReader): T {
            val builder = GsonBuilder()
            val gson = builder.create()
            return gson.fromJson(reader, beanClass)
        }

        @Throws(JsonParseException::class)
        fun <T> parseJson(beanClass: Class<T>, json: String): T {
            val builder = GsonBuilder()
            val gson = builder.create()
            return gson.fromJson(json, beanClass)
        }

        fun Objects_equals(a: Any?, b: Any): Boolean {
            return a === b || a != null && a == b
        }

        fun a(context: Context) {
            val url = "https://qr.alipay.com/a6x05027ymf6n8kl0qkoa54"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}
