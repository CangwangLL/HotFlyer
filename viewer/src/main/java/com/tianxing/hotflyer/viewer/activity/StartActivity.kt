package com.tianxing.hotflyer.viewer.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException

import com.tianxing.hotflyer.viewer.Configurations
import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.Properties
import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.item.DataSource

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response

class StartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        checkPermissions() //检查权限，创建配置
    }

    fun readProperties() {
        val request = Request.Builder()
                .url("https://raw.githubusercontent.com/SplashCodes/JAViewer/master/properties.json")
                .build()
        JAViewer.HTTP_CLIENT.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val properties = JAViewer.parseJson(Properties::class.java, response.body()!!.string())
                if (properties != null) {
                    Handler(Looper.getMainLooper()).post {
                        try {
                            handleProperties(properties)
                        } catch (e: URISyntaxException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
    }

    @Throws(URISyntaxException::class)
    fun handleProperties(properties: Properties?) {
        JAViewer.DATA_SOURCES.clear()
        if (properties?.data_sources != null) {
            JAViewer.DATA_SOURCES.addAll(properties.data_sources)
        }

        JAViewer.hostReplacements.clear()
        for (source in JAViewer.DATA_SOURCES) {
            val host = URI(source.link!!).host
            for (h in source.legacies!!) {
                JAViewer.hostReplacements.put(h, host)
            }
        }

        val currentVersion: Int
        try {
            currentVersion = this.packageManager.getPackageInfo(this.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Hacked???")
        }

        if (properties?.latest_version_code!! > 0 && currentVersion < properties.latest_version_code) {

            var message = "新版本：" + properties.latest_version!!
            if (properties.changelog != null) {
                message += "\n\n更新日志：\n\n" + properties.changelog + "\n"
            }

            val update = booleanArrayOf(false)
            val dialog = AlertDialog.Builder(this)
                    .setTitle("发现更新")
                    .setMessage(message)
                    .setNegativeButton("忽略更新", null)
                    .setPositiveButton("更新") { dialog, which -> update[0] = true }
                    .create()
            dialog.show()

            dialog.setOnDismissListener {
                start()
                if (update[0]) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SplashCodes/JAViewer/releases")))
                }
            }
        } else {
            start()
        }

    }

    fun start() {
        startActivity(Intent(this@StartActivity, MainActivity::class.java))
        finish()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                            checkPermissions()
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) {
                            AlertDialog.Builder(this@StartActivity)
                                    .setTitle("权限申请")
                                    .setCancelable(false)
                                    .setMessage("JAViewer 需要储存空间权限，储存用户配置。请您允许。")
                                    .setPositiveButton(android.R.string.ok) { dialogInterface, i -> checkPermissions() }
                                    .show()
                        }

                        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                            token.continuePermissionRequest()
                        }
                    })
                    .onSameThread()
                    .check()
            return
        }

        val oldConfig = File(this@StartActivity.getExternalFilesDir(null), "configurations.json")
        val config = File(JAViewer.storageDir, "configurations.json")
        if (oldConfig.exists()) {
            oldConfig.renameTo(config)
        }

        val noMedia = File(JAViewer.storageDir, ".nomedia")
        try {
            noMedia.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        JAViewer.CONFIGURATIONS = Configurations.load(config)

        readProperties()
    }

}
