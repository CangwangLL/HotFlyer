package com.tianxing.hotflyer.viewer.activity

import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem


import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.ViewPagerAdapter
import com.tianxing.hotflyer.viewer.fragment.DownloadFragment

class DownloadActivity : AppCompatActivity() {

    lateinit var mToolbar: Toolbar

    lateinit var mTabLayout: TabLayout

    lateinit var mViewPager: ViewPager

    var keyword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        mToolbar = findViewById(R.id.download_toolbar)
        mTabLayout = findViewById(R.id.download_tabs)
        mViewPager = findViewById(R.id.download_view_pager)

        var bundle = this.intent.extras
        this.keyword = this.intent.extras!!.getString("keyword")

        setSupportActionBar(mToolbar)
        supportActionBar!!.setTitle(this.keyword)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        var fragment: Fragment

        fragment = DownloadFragment()
        bundle = bundle!!.clone() as Bundle
        bundle.putString("provider", "btso")
        fragment.setArguments(bundle)
        adapter.addFragment(fragment, "BTSO")

        fragment = DownloadFragment()
        bundle = bundle.clone() as Bundle
        bundle.putString("provider", "torrentkitty")
        fragment.setArguments(bundle)
        adapter.addFragment(fragment, "Torrent Kitty")

        mViewPager.adapter = adapter

        mTabLayout.setupWithViewPager(mViewPager)

        var downloadCounter = JAViewer.CONFIGURATIONS!!.downloadCounter
        if (downloadCounter == -1L) {
            return
        }
        downloadCounter++
        JAViewer.CONFIGURATIONS!!.downloadCounter = downloadCounter
        if (downloadCounter % 20 == 0L) {
            AlertDialog.Builder(this)
                    .setTitle("用得不错？")
                    .setMessage("您的支持是我动力来源！\n请考虑为我买杯咖啡醒醒脑，甚至其他…… ;)")
                    .setPositiveButton("为我买杯咖啡") { dialog, which ->
                        JAViewer.a(this@DownloadActivity)
                        AlertDialog.Builder(this@DownloadActivity)
                                .setMessage("感谢您的支持！;)\n新功能持续开发中！")
                                .setPositiveButton("确认", null)
                                .show()
                    }
                    .setNeutralButton("不再显示") { dialog, which -> JAViewer.CONFIGURATIONS!!.downloadCounter = -1 }
                    .setNegativeButton("取消", null)
                    .show()
        }
        JAViewer.CONFIGURATIONS!!.save()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
