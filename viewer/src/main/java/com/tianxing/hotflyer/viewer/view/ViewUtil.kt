package com.tianxing.hotflyer.viewer.view

import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Build
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.widget.ImageView

/**
 * Project: JAViewer
 */

object ViewUtil {
    fun alignIconToView(icon: View, view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                val viewMargin = view.layoutParams as ViewGroup.MarginLayoutParams
                val iconMargin = icon.layoutParams as ViewGroup.MarginLayoutParams
                var topMargin = viewMargin.topMargin
                topMargin += (view.measuredHeight - icon.measuredHeight) / 2

                iconMargin.topMargin = topMargin
                icon.layoutParams = iconMargin

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    view.viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
            }
        })
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun imageTopCrop(view: ImageView) {
        view.scaleType = ImageView.ScaleType.MATRIX
        val matrix = view.imageMatrix

        val scale: Float
        val viewWidth = view.width - view.paddingLeft - view.paddingRight
        val viewHeight = view.height - view.paddingTop - view.paddingBottom
        val drawableWidth = view.drawable.intrinsicWidth
        val drawableHeight = view.drawable.intrinsicHeight

        if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            scale = viewHeight.toFloat() / drawableHeight.toFloat()
        } else {
            scale = viewWidth.toFloat() / drawableWidth.toFloat()
        }

        matrix.setScale(scale, scale)
        view.imageMatrix = matrix
    }

    fun getBitmapByView(scrollView: NestedScrollView): Bitmap {
        var h = 0
        var bitmap: Bitmap? = null
        // 获取scrollview实际高度
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"))
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.width, h,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap!!)
        scrollView.draw(canvas)
        return bitmap
    }

    fun getStatusBarHeight(activity: Activity): Int {
        val rectangle = Rect()
        val window = activity.window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        return rectangle.top
    }
}
