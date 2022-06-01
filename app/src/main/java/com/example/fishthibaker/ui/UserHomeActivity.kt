package com.example.fishthibaker.ui

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.fishthibaker.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class UserHomeActivity : AppCompatActivity() {
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    // index to identify current nav menu item
    var navItemIndex = 0

    // tags used to attach the fragments
    private val TAG_HOME = "home"
    private val TAG_PROFILE = "profile"
    private val TAG_ORDER = "order"
    private val TAG_CART = "cart"
    private val TAG_OFFER = "offer"
    private val TAG_SETTINGS = "settings"
    private val TAG_ABOUT = "about"
    private val TAG_VISIT = "visit"
    private val TAG_RATE = "rate"
    private val TAG_CALL = "call"
    private val TAG_TC = "tc"
    var CURRENT_TAG = TAG_HOME

    private var mHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

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
                UserHomeFragment(this)
            }
            1 -> {
                ProfileFragment(this)
            }
            2 -> {
                OrderFragment(this, false)
            }
            3 -> {
                CartFragment(this)
            }
            4 -> {
                TCFragment()
            }
            5 -> {
                AboutFragment()
            }
            6 -> {
                VisitFragment(this)
            }
            8 -> {
                RateFragment(this)
            }
            else -> HomeFragment("Other")
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
                R.id.navProfile -> {
                    navItemIndex = 1
                    CURRENT_TAG = TAG_PROFILE
                }
                R.id.navOrder -> {
                    navItemIndex = 2
                    CURRENT_TAG = TAG_ORDER
                }
                R.id.navCart -> {
                    navItemIndex = 3
                    CURRENT_TAG = TAG_CART
                }
                R.id.navTC -> {
                    navItemIndex = 4
                    CURRENT_TAG = TAG_TC
                }
                R.id.navAbout -> {
                    navItemIndex = 5
                    CURRENT_TAG = TAG_ABOUT
                }
                R.id.navVisit -> {
                    navItemIndex = 6
                    CURRENT_TAG = TAG_VISIT
                }
                R.id.navCall -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CALL_PHONE),
                            100
                        )
                    } else {
                        val intent = Intent(Intent.ACTION_CALL)

                        intent.data = Uri.parse("tel:1234567890")
                        startActivity(intent)
                    }
                }
                R.id.navRate -> {
                    navItemIndex = 8
                    CURRENT_TAG = TAG_RATE
                }
                R.id.navLogout -> {
                    AlertDialog.Builder(this)
                        .setTitle("Fishthi Baker")
                        .setMessage("Are you sure you want to Logout?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes,
                            DialogInterface.OnClickListener { dialog, whichButton ->
                                FirebaseAuth.getInstance().signOut()
                                startActivity(Intent(this, LoginActivity::class.java))
                            })
                        .setNegativeButton(android.R.string.no, null).show()

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

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Fishthi Baker")
            .setMessage("Are you sure you want to Logout?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { dialog, whichButton ->
                    super.onBackPressed()
                })
            .setNegativeButton(android.R.string.no, null).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}