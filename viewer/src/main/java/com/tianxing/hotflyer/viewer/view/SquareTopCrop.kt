package com.tianxing.hotflyer.viewer.view

import android.content.Context
import android.graphics.Bitmap

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

/**
 * Project: JAViewer
 */

class SquareTopCrop(context: Context) : BitmapTransformation(context) {

    // Bitmap doesn't implement equals, so == and .equals are equivalent here.
    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.width, toTransform.width)
    }

    override fun getId(): String {
        return "SquareTopCrop"
    }
}
