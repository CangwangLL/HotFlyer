package com.tianxing.hotflyer.viewer.activity

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

import java.io.UnsupportedEncodingException
import java.net.URI
import java.net.URISyntaxException
import java.net.URLEncoder

import com.tianxing.hotflyer.viewer.JAViewer
import com.tianxing.hotflyer.viewer.Properties
import com.tianxing.hotflyer.viewer.R
import com.tianxing.hotflyer.viewer.adapter.NavigationSpinnerAdapter
import com.tianxing.hotflyer.viewer.adapter.item.DataSource
import com.tianxing.hotflyer.viewer.fragment.ExtendedAppBarFragment
import com.tianxing.hotflyer.viewer.network.BasicService
import com.tianxing.hotflyer.viewer.view.SimpleSearchView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var fragmentManager: FragmentManager? = null

    var currentFragment: Fragment? = null

    lateinit var mNavigationView: NavigationView

    lateinit var mAppBarLayout: AppBarLayout

    lateinit var mSearchView: SimpleSearchView

    lateinit var mDrawerLayout: DrawerLayout

    internal var positionOfSpinner = 0
    internal var idOfMenuItem = R.id.nav_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mNavigationView = findViewById(R.id.nav_view)
        mAppBarLayout = findViewById(R.id.app_bar)
        mSearchView = findViewById(R.id.search_view)
        mDrawerLayout = findViewById(R.id.drawer_layout)

        JAViewer.recreateService()

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        this.fragmentManager = supportFragmentManager
        initFragments(savedInstanceState)

        if (savedInstanceState != null) {
            idOfMenuItem = savedInstanceState.getInt("MenuSelectedItemId", R.id.nav_home)
        }
        mNavigationView.setNavigationItemSelectedListener(this)
        val selectedItem = mNavigationView.menu.findItem(idOfMenuItem)
        mNavigationView.setCheckedItem(selectedItem.itemId)
        onNavigationItemSelected(selectedItem)

        val spinner = mNavigationView.getHeaderView(0).findViewById<View>(R.id.nav_header_spinner) as Spinner
        val adapter = NavigationSpinnerAdapter(this, R.layout.nav_spinner_item, JAViewer.DATA_SOURCES)
        adapter.setDropDownViewResource(R.layout.view_drop_down)
        spinner.adapter = adapter
        spinner.setSelection(JAViewer.DATA_SOURCES.indexOf(JAViewer.dataSource))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val newSource = JAViewer.DATA_SOURCES[position]
                if (newSource == JAViewer.dataSource) {
                    return
                }

                AlertDialog.Builder(this@MainActivity)
                        .setMessage("是否切换到" + newSource.name + "数据源？")
                        .setOnCancelListener { spinner.setSelection(positionOfSpinner) }
                        .setPositiveButton("确认") { dialog, which ->
                            positionOfSpinner = position
                            JAViewer.CONFIGURATIONS!!.dataSource = newSource
                            JAViewer.CONFIGURATIONS!!.save()
                            restart()
                        }
                        .setNegativeButton("取消") { dialog, which -> spinner.setSelection(positionOfSpinner) }
                        .show()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


    }

    fun initFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val tag = savedInstanceState.getString("CurrentFragment")
            this.currentFragment = fragmentManager!!.findFragmentByTag(tag)
            return
        }

        val transaction = this.fragmentManager!!.beginTransaction()
        for (id in JAViewer.FRAGMENTS.keys) {
            val fragmentClass = JAViewer.FRAGMENTS[id]
            try {
                val fragment = fragmentClass!!.getConstructor(*arrayOfNulls(0)).newInstance()
                transaction.add(R.id.content, fragment, fragmentClass!!.simpleName).hide(fragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        transaction.commit()
        this.fragmentManager!!.executePendingTransactions()
    }

    private fun setFragment(fragment: Fragment, title: CharSequence) {
        supportActionBar!!.title = title

        val old = this.currentFragment

        if (old === fragment) {
            return
        }

        val transaction = fragmentManager!!.beginTransaction()
        if (old != null) {
            transaction.hide(old)
        }
        transaction.show(fragment)
        transaction.commit()

        this.currentFragment = fragment

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (fragment is ExtendedAppBarFragment) {
                mAppBarLayout.elevation = 0f
            } else {
                mAppBarLayout.elevation = 4 * resources.displayMetrics.density
            }
        }
    }

    private fun setFragment(id: Int, title: CharSequence) {
        this.setFragment(fragmentManager!!.findFragmentByTag(JAViewer.FRAGMENTS[id]!!.simpleName), title)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString("CurrentFragment", this.currentFragment!!.javaClass.simpleName)
        outState.putInt("MenuSelectedItemId", this.idOfMenuItem)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
            return
        }

        if (mSearchView.isSearchOpen) {
            mSearchView.closeSearch()
            return
        }

        moveTaskToBack(false)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val item = menu.findItem(R.id.action_search)
        mSearchView.setMenuItem(item)
        mSearchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                try {
                    startActivity(MovieListActivity.newIntent(this@MainActivity, query + " 的搜索结果", JAViewer.dataSource?.link + BasicService.LANGUAGE_NODE + "/search/" + URLEncoder.encode(query, "UTF-8")))
                } catch (e: UnsupportedEncodingException) {
                    return false
                }

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        idOfMenuItem = id

/*        if (id == R.id.nav_github) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SplashCodes/JAViewer/releases"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        } else*/

        if (id ==R.id.nav_download_manager){
            startActivity(Intent(this@MainActivity,DownloadManagerActivity::class.java))
        }else if (id == R.id.nav_favourite) {
            val intent = Intent(this@MainActivity, FavouriteActivity::class.java)
            startActivity(intent)

        } else {
            setFragment(id, item.title)
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun restart() {
        val intent = intent
        finish()
        startActivity(intent)
    }

}
