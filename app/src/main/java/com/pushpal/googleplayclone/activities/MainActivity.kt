package com.pushpal.googleplayclone.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import com.arlib.floatingsearchview.FloatingSearchView
import com.bumptech.glide.Glide
import com.pushpal.googleplayclone.R
import com.pushpal.googleplayclone.adapters.ViewPagerAdapter
import com.pushpal.googleplayclone.fragments.main.HomeFragment
import com.pushpal.googleplayclone.viewPager.CustomViewPager

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<CustomViewPager>(R.id.mainViewPager)
        setupViewPager(viewPager)

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        val mSearchView = findViewById<FloatingSearchView>(R.id.search_view)

        mSearchView.setOnQueryChangeListener { oldQuery, newQuery -> }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, null,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.itemIconTintList = null

        val profileImageView = navigationView.getHeaderView(0).findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profile_image)
        Glide.with(this)
                .load(R.drawable.profile_image)
                .into(profileImageView)

        mSearchView.attachNavigationDrawerToMenuButton(drawer)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(), getString(R.string._home))
        adapter.addFragment(HomeFragment(), getString(R.string._games))
        adapter.addFragment(HomeFragment(), "Movies")
        adapter.addFragment(HomeFragment(), "Books")
        adapter.addFragment(HomeFragment(), "Music")
        viewPager.adapter = adapter
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
