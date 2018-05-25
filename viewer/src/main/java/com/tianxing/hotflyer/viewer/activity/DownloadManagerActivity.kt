package com.tianxing.hotflyer.viewer.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.ViewPagerAdapter
import com.tianxing.hotflyer.viewer.fragment.DownLoadControlFragment
import kotlinx.android.synthetic.main.activity_download_manager.*

/**
 * auther by cwll on 2018/5/24 23:19.
 *
 */
class DownloadManagerActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_manager)

        setSupportActionBar(download_manager_toolbar)
        supportActionBar!!.setTitle("DownLoad Manager")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val adapter = ViewPagerAdapter(supportFragmentManager)

        var dlcFragment = DownLoadControlFragment()
        adapter.addFragment(dlcFragment,"downloading")

        dlcFragment = DownLoadControlFragment()
        adapter.addFragment(dlcFragment,"finished")
        download_manager_view_pager.adapter = adapter

        download_manager_tabs.setupWithViewPager(download_manager_view_pager)

    }
}