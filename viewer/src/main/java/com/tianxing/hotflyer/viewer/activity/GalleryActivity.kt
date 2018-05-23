package com.tianxing.hotflyer.viewer.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.PhotoView
import com.github.chrisbanes.photoview.PhotoViewAttacher

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

import butterknife.BindView
import butterknife.ButterKnife

import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.item.Movie

class GalleryActivity : AppCompatActivity() {

    internal var fadeIn: Animation = AlphaAnimation(0f, 1f)

    internal var fadeOut: Animation = AlphaAnimation(1f, 0f)

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        mPager.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        val actionBar = supportActionBar
        actionBar?.show()
        mToolbar.startAnimation(fadeIn)

        //mControlsView.setVisibility(View.VISIBLE);
    }

    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    lateinit var detector: GestureDetector

    lateinit var mPager: ViewPager

    lateinit var mToolbar: Toolbar

    private var imageUrls: Array<String>? = null
    private var movie: Movie? = null

    init {
        fadeIn.interpolator = DecelerateInterpolator() //add this
        fadeIn.duration = 150
    }

    init {
        fadeOut.interpolator = AccelerateInterpolator() //and this
        fadeOut.duration = 150
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_gallery)

        mPager = findViewById(R.id.gallery_pager)
        mToolbar = findViewById(R.id.toolbar_gallery)

        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        detector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                toggle()
                return true
            }
        })
        mPager.setOnTouchListener { v, event ->
            detector.onTouchEvent(event)
            false
        }

        run {
            val actionBar = supportActionBar
            actionBar?.hide()
            mToolbar.startAnimation(fadeOut)
            //mControlsView.setVisibility(View.GONE);
            mVisible = false
            mHidePart2Runnable.run()
        }

        val bundle = this.intent.extras

        mPager.adapter = ImageAdapter(this, imageUrls = bundle!!.getStringArray("urls"), mActivity = this)
        mPager.currentItem = bundle.getInt("position")
        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                updateIndicator()
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}
        })
        updateIndicator()

        movie = bundle.getSerializable("movie") as Movie
    }

    private fun updateIndicator() {
        delayedHide(AUTO_HIDE_DELAY_MILLIS)
        supportActionBar!!.title = (mPager.currentItem + 1).toString() + " / " + imageUrls!!.size
        //mTextIndicator.setText((mPager.getCurrentItem() + 1) + " / " + (imageUrls.length));
    }

    fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS)
            }
        }
    }

    private fun hide(delay: Int = UI_ANIMATION_DELAY) {
        // Hide UI first
        val actionBar = supportActionBar
        actionBar?.hide()
        mToolbar.startAnimation(fadeOut)
        //mControlsView.setVisibility(View.GONE);
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, delay.toLong())
    }

    @SuppressLint("InlinedApi")
    private fun show() {
        // Show the system bar
        mPager.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.gallery, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_save) {
            val dir = File(
                    JAViewer.storageDir,
                    String.format("/movies/[%s] %s", movie!!.code, movie!!.title).replace("^(?!(COM[0-9]|LPT[0-9]|CON|PRN|AUX|CLOCK\\$|NUL)$)[^./\\\\:*?\u200C\u200B\"<>|]+$".toRegex(), "-")
            )
            dir.mkdirs()
            val index = mPager.currentItem
            Glide
                    .with(this)
                    .load(imageUrls!![index])
                    .asBitmap()
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                            try {
                                val os = BufferedOutputStream(FileOutputStream(File(dir, (index + 1).toString() + ".jpeg")))
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, os)
                                os.flush()
                                os.close()
                                Toast.makeText(this@GalleryActivity, "成功保存到 " + dir, Toast.LENGTH_SHORT).show()
                            } catch (e: IOException) {
                                onLoadFailed(e, null)
                            }

                        }
                    })
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private class ImageAdapter
    //private DisplayImageOptions options;

    internal constructor(context: Context, private val imageUrls: Array<String>, private val mActivity: GalleryActivity) : PagerAdapter() {
        private val inflater: LayoutInflater

        init {
            inflater = LayoutInflater.from(context)
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun getCount(): Int {
            return imageUrls.size
        }

        override fun instantiateItem(view: ViewGroup, position: Int): Any {
            val imageLayout = inflater.inflate(R.layout.content_gallery, view, false)
            val imageView = imageLayout.findViewById<PhotoView>(R.id.image)
            val progressBar = imageLayout.findViewById<ProgressBar>(R.id.progress_bar)
            val textView = imageLayout.findViewById<TextView>(R.id.gallery_text_error)

            val attacher = PhotoViewAttacher(imageView)
            /*attacher.setOnViewTapListener(new OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    mActivity.toggle();
                }
            });
*/
            attacher.setOnPhotoTapListener { view, x, y -> mActivity.toggle() }
            Glide.with(imageView.context.applicationContext)
                    .load(imageUrls[position])
                    .into(object : SimpleTarget<GlideDrawable>() {
                        override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
                            progressBar.visibility = View.GONE
                            imageView.setImageDrawable(resource)
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            textView.text = "图片加载失败 :(\n" + e!!.message
                        }
                    })

            view.addView(imageLayout, 0)
            return imageLayout
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

        override fun saveState(): Parcelable? {
            return null
        }
    }

    companion object {

        private val AUTO_HIDE = true
        private val AUTO_HIDE_DELAY_MILLIS = 3000
        private val UI_ANIMATION_DELAY = 300
    }
}
