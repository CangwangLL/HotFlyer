package com.tianxing.hotflyer.viewer.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.robertlevonyan.views.chip.Chip
import com.wefika.flowlayout.FlowLayout

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Collections

import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.ActressPaletteAdapter
import com.tianxing.hotflyer.viewer.adapter.MovieHeaderAdapter
import com.tianxing.hotflyer.viewer.adapter.ScreenshotAdapter
import com.tianxing.hotflyer.viewer.adapter.item.Genre
import com.tianxing.hotflyer.viewer.adapter.item.Movie
import com.tianxing.hotflyer.viewer.adapter.item.MovieDetail
import com.tianxing.hotflyer.viewer.network.provider.AVMOProvider
import com.tianxing.hotflyer.viewer.view.ViewUtil

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieActivity : AppCompatActivity() {

    lateinit var movie: Movie

    lateinit var mToolbarLayoutBackground: ImageView

    lateinit var mContent: NestedScrollView

    lateinit var mProgressBar: ProgressBar

    lateinit var mFab: FloatingActionButton

    lateinit var mFlowLayout: FlowLayout


    lateinit var mStarButton: MenuItem

    //Image
    //ScrollView
    val screenBitmap: Bitmap
        get() {
            val imageHeight = mToolbarLayoutBackground.getHeight()
            var scrollViewHeight = 0
            for (i in 0 until mContent.getChildCount()) {
                scrollViewHeight += mContent.getChildAt(i).getHeight()
            }
            val result = Bitmap.createBitmap(mContent.getWidth(), imageHeight + scrollViewHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(result)
            canvas.drawColor(Color.parseColor("#FAFAFA"))
            run({
                val bitmap = Bitmap.createBitmap(mToolbarLayoutBackground.getWidth(), imageHeight, Bitmap.Config.ARGB_8888)
                val c = Canvas(bitmap)
                mToolbarLayoutBackground.draw(c)
                canvas.drawBitmap(bitmap, 0f, 0f, null)
            })
            run({
                val bitmap = Bitmap.createBitmap(mContent.getWidth(), scrollViewHeight, Bitmap.Config.ARGB_8888)
                val c = Canvas(bitmap)
                mContent.draw(c)
                canvas.drawBitmap(bitmap, 0f, imageHeight.toFloat(), null)
            })

            return result
        }

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        mToolbarLayoutBackground = findViewById<ImageView>(R.id.toolbar_layout_background)
        mContent = findViewById<NestedScrollView>(R.id.movie_content)
        mProgressBar = findViewById<ProgressBar>(R.id.movie_progress_bar)
        mFab = findViewById<FloatingActionButton>(R.id.fab)
        mFlowLayout = findViewById<FlowLayout>(R.id.genre_flow_layout)


        val bundle = this.getIntent().getExtras()
        movie = bundle!!.getSerializable("movie") as Movie

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setTitle(movie.title)

        mFab.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                val intent = Intent(this@MovieActivity, DownloadActivity::class.java)
                val arguments = Bundle()
                arguments.putString("keyword", movie.code)
                intent.putExtras(arguments)
                startActivity(intent)
            }
        })
        mFab.bringToFront()

        val call = JAViewer.SERVICE.get(this.movie.link!!)
        call.enqueue(object : Callback<ResponseBody> {
            public override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful()) {
                    return
                }

                val detail: MovieDetail
                try {
                    detail = AVMOProvider.parseMoviesDetail(response.body()!!.string())
                    detail.headers.add(0, MovieDetail.Header.create("影片名", movie.title, ""))
                    displayInfo(detail)

                    Glide.with(mToolbarLayoutBackground.getContext().getApplicationContext())
                            .load(detail.coverUrl)
                            .into(mToolbarLayoutBackground)
                } catch (e: IOException) {
                    onFailure(call, e)
                }

            }

            public override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun displayInfo(detail: MovieDetail) {
        //Info
        run({
            val mRecyclerView = findViewById<View>(R.id.headers_recycler_view) as RecyclerView
            val mIcon = findViewById<View>(R.id.movie_icon_header) as ImageView

            if (detail.headers.isEmpty()) {
                val mText = findViewById<View>(R.id.header_empty_text) as TextView
                mRecyclerView.setVisibility(View.GONE)
                mText.setVisibility(View.VISIBLE)
                ViewUtil.alignIconToView(mIcon, mText)
            } else {
                mRecyclerView.setAdapter(MovieHeaderAdapter(detail.headers, this, mIcon))
                mRecyclerView.setLayoutManager(LinearLayoutManager(this))
                mRecyclerView.setNestedScrollingEnabled(false)
            }
        })

        //Screenshots
        run({
            val mRecyclerView = findViewById<View>(R.id.screenshots_recycler_view) as RecyclerView
            val mIcon = findViewById<View>(R.id.movie_icon_screenshots) as ImageView

            if (detail.screenshots.isEmpty()) {
                val mText = findViewById<View>(R.id.screenshots_empty_text) as TextView
                mRecyclerView.setVisibility(View.GONE)
                mText.setVisibility(View.VISIBLE)
                ViewUtil.alignIconToView(mIcon, mText)
            } else {
                mRecyclerView.setAdapter(ScreenshotAdapter(detail.screenshots, this, mIcon, movie))
                mRecyclerView.setLayoutManager(StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL))
                mRecyclerView.setNestedScrollingEnabled(false)
            }
        })

        //Actress
        run({
            val mRecyclerView = findViewById<View>(R.id.actresses_recycler_view) as RecyclerView
            val mIcon = findViewById<View>(R.id.movie_icon_actresses) as ImageView

            if (detail.actresses.isEmpty()) {
                val mText = findViewById<View>(R.id.actresses_empty_text) as TextView
                mRecyclerView.setVisibility(View.GONE)
                mText.setVisibility(View.VISIBLE)
                ViewUtil.alignIconToView(mIcon, mText)
            } else {
                mRecyclerView.setAdapter(ActressPaletteAdapter(detail.actresses, this, mIcon))
                mRecyclerView.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))
                mRecyclerView.setNestedScrollingEnabled(false)
            }
        })

        //Genre
        run({
            val mIcon = findViewById<View>(R.id.movie_icon_genre) as ImageView

            if (detail.genres.isEmpty()) {
                mFlowLayout.setVisibility(View.GONE)
                val mText = findViewById<View>(R.id.genre_empty_text) as TextView
                mText.setVisibility(View.VISIBLE)
                ViewUtil.alignIconToView(mIcon, mText)
            } else {
                for (i in detail.genres.indices) {
                    val genre = detail.genres.get(i)
                    val view = getLayoutInflater().inflate(R.layout.chip_genre, mFlowLayout, false)
                    val chip = view.findViewById<View>(R.id.chip_genre) as Chip
                    chip.setOnClickListener(object : View.OnClickListener {
                        public override fun onClick(v: View) {
                            if (genre.link != null) {
                                startActivity(MovieListActivity.newIntent(this@MovieActivity, genre.name, genre.link!!))
                            }
                        }
                    })
                    chip.setChipText(genre.name)
                    mFlowLayout.addView(view)

                    if (i == 0) {
                        ViewUtil.alignIconToView(mIcon, view)
                    }
                }
            }
        })

        //Changing visibility
        mProgressBar.animate().setDuration(200).alpha(0f).setListener(object : AnimatorListenerAdapter() {
            public override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mProgressBar.setVisibility(View.GONE)
            }
        }).start()

        //Slide Up Animation
        mContent.setVisibility(View.VISIBLE)
        mContent.setY(mContent.getY() + 120)
        mContent.setAlpha(0f)
        mContent.animate().translationY(0f).alpha(1f).setDuration(500).start()
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    public override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.movie, menu)

        mStarButton = menu.findItem(R.id.action_star)
        run({
            if (JAViewer.CONFIGURATIONS!!.starredMovies.contains(movie)) {
                mStarButton.setIcon(R.drawable.ic_menu_star)
                mStarButton.setTitle("取消收藏")
            }
        })
        mStarButton.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            public override fun onMenuItemClick(item: MenuItem): Boolean {
                if (JAViewer.CONFIGURATIONS!!.starredMovies.contains(movie)) {
                    JAViewer.CONFIGURATIONS!!.starredMovies.remove(movie)
                    mStarButton.setIcon(R.drawable.ic_menu_star_border)
                    Snackbar.make(mContent, "已取消收藏", Snackbar.LENGTH_LONG).show()
                    mStarButton.setTitle("收藏")
                } else {
                    val movies = JAViewer.CONFIGURATIONS!!.starredMovies
                    Collections.reverse(movies)
                    movies.add(movie)
                    Collections.reverse(movies)
                    mStarButton.setIcon(R.drawable.ic_menu_star)
                    Snackbar.make(mContent, "已收藏", Snackbar.LENGTH_LONG).show()
                    mStarButton.setTitle("取消收藏")
                }
                JAViewer.CONFIGURATIONS!!.save()
                FavouriteActivity.update()
                return true
            }
        })

        val mShareButton = menu.findItem(R.id.action_share)
        mShareButton.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            public override fun onMenuItemClick(item: MenuItem): Boolean {
                try {
                    val cache = File(getExternalFilesDir("cache"), "screenshot")
                    val os = FileOutputStream(cache)
                    val screenshot = screenBitmap
                    //Bitmap screenshot = ViewUtil.getBitmapByView(mContent);
                    screenshot.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    os.flush()
                    os.close()

                    val uri = Uri.fromFile(cache)
                    val intent = Intent(Intent.ACTION_SEND)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setType("image/jpeg")
                            .putExtra(Intent.EXTRA_STREAM, uri)
                    startActivity(Intent.createChooser(intent, "分享此影片"))

                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MovieActivity, "无法分享：" + e.message, Toast.LENGTH_SHORT).show()
                }

                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
}
