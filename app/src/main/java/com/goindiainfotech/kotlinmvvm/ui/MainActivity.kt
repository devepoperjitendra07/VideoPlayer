package com.goindiainfotech.kotlinmvvm.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.goindiainfotech.kotlinmvvm.R
import com.goindiainfotech.kotlinmvvm.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = viewPagerAdapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }

}
