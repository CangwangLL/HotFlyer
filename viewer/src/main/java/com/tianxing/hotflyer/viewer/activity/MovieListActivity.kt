package com.tianxing.hotflyer.viewer.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.fragment.MovieListFragment
import com.tianxing.hotflyer.viewer.view.ViewUtil

class MovieListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        val bundle = this.intent.extras

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setTitle(bundle!!.getString("title"))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.elevation = ViewUtil.dpToPx(4).toFloat()

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()

            val fragment = MovieListFragment()
            fragment.arguments = bundle
            transaction.replace(R.id.content_query, fragment)
            transaction.commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {

        fun newIntent(context: Context, title: String, link: String): Intent {
            val intent = Intent(context, MovieListActivity::class.java)
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("link", link)
            intent.putExtras(bundle)
            return intent
        }
    }
}
