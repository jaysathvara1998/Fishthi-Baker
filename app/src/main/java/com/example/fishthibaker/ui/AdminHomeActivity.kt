package com.example.fishthibaker.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.fishthibaker.AdminHomeFragment
import com.example.fishthibaker.R
import com.google.android.material.navigation.NavigationView

class AdminHomeActivity : AppCompatActivity() {
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    // index to identify current nav menu item
    var navItemIndex = 0

    // tags used to attach the fragments
    private val TAG_HOME = "home"
    private val TAG_PHOTOS = "photos"
    private val TAG_MOVIES = "movies"
    private val TAG_NOTIFICATIONS = "notifications"
    private val TAG_SETTINGS = "settings"
    var CURRENT_TAG = TAG_HOME

    private var mHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        Log.e("UserHomePage", "Test")
        mHandler = Handler()

        drawer = findViewById(R.id.my_drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navigationView = findViewById(R.id.navView)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close)

        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private fun loadHomeFragment() {
        selectNavMenu()

        if (supportFragmentManager.findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers()
            return
        }

        val mPendingRunnable = Runnable { // update the main content by replacing fragments
            val fragment: Fragment = getHomeFragment()
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_fade_out
            )
            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG)
            fragmentTransaction.commitAllowingStateLoss()
        }

        mHandler!!.post(mPendingRunnable)

        drawer.closeDrawers()

        invalidateOptionsMenu()
    }

    private fun getHomeFragment(): Fragment {
        return when (navItemIndex) {
            0 -> {
                AdminHomeFragment(this)
            }
            1 -> {
                AdminHomeFragment(this)
            }
            else -> AdminHomeFragment(this)
        }
    }

    private fun selectNavMenu() {
        for (item in navigationView.menu.children) {
            item.isChecked = false
        }
        navigationView.menu.getItem(navItemIndex).isChecked = true
    }

    private fun setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navHome -> {
                    navItemIndex = 0
                    CURRENT_TAG = TAG_HOME
                }
                R.id.navLogout -> {
//                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                else -> navItemIndex = 0
            }
            menuItem.isChecked = !menuItem.isChecked
            menuItem.isChecked = true
            loadHomeFragment()
            true
        }
        val actionBarDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.openDrawer,
            R.string.closeDrawer
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle)

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}