package com.tianxing.hotflyer.viewer.view.listener

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

import com.tianxing.hotflyer.viewer.activity.MovieListActivity
import com.tianxing.hotflyer.viewer.adapter.item.Actress

/**
 * Project: JAViewer
 */

class ActressClickListener(private val actress: Actress, private val mActivity: Activity) : View.OnClickListener {

    override fun onClick(v: View) {
        if (actress.link != null) {
            val intent = Intent(mActivity, MovieListActivity::class.java)
            val bundle = Bundle()
            bundle.putString("title", actress.name + " 的作品")
            bundle.putString("link", actress.link)

            intent.putExtras(bundle)

            mActivity.startActivity(intent)
        }
    }
}
