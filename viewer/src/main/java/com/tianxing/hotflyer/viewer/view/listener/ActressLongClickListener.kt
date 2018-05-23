package com.tianxing.hotflyer.viewer.view.listener

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast

import java.util.Collections

import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.activity.FavouriteActivity
import com.tianxing.hotflyer.viewer.adapter.item.Actress

/**
 * Project: JAViewer
 */

class ActressLongClickListener(private val actress: Actress, private val mActivity: Activity) : View.OnLongClickListener {

    override fun onLongClick(v: View): Boolean {
        val builder = AlertDialog.Builder(mActivity)
        val items: Array<String>
        val actresses = JAViewer.CONFIGURATIONS!!.starredActresses
        val contain = actresses.contains(actress)
        if (contain) {
            items = arrayOf("复制女优名字", "取消收藏")
        } else {
            items = arrayOf("复制女优名字", "收藏")
        }
        builder.setTitle(actress.name)
                .setItems(items) { dialog, which ->
                    when (which) {
                        0 -> {
                            val clip = mActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clip.primaryClip = ClipData.newPlainText("actress", actress.name)
                            Toast.makeText(mActivity, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                        }
                        1 -> {
                            if (contain) {
                                actresses.remove(actress)
                                Toast.makeText(mActivity, "已取消收藏", Toast.LENGTH_SHORT).show()
                            } else {
                                Collections.reverse(actresses)
                                actresses.add(actress)
                                Collections.reverse(actresses)
                                Toast.makeText(mActivity, "已收藏", Toast.LENGTH_SHORT).show()
                            }
                            JAViewer.CONFIGURATIONS!!.save()
                            FavouriteActivity.update()
                        }
                    }
                }
        builder.create().show()
        return true
    }
}
