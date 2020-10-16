package com.goindiainfotech.kotlinmvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.goindiainfotech.kotlinmvvm.R
import com.goindiainfotech.kotlinmvvm.viewmodel.PageViewModel
import kotlinx.android.synthetic.main.activity_navigation_component.*

class NavigationComponent : AppCompatActivity() {

    var navController: NavController? = null
    var appBarConfiguration: AppBarConfiguration? = null
    private var pageViewModel: PageViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_component)

        pageViewModel =  ViewModelProvider(this@NavigationComponent).get(PageViewModel::class.java)
        pageViewModel!!.init()
        init()

        NavigationUI.setupActionBarWithNavController(
            this,
            navController!!,
            appBarConfiguration!!
        )
        NavigationUI.setupWithNavController(navigationView!!, navController!!)
        NavigationUI.setupWithNavController(bottomNavigation!!, navController!!)
        pageViewModel!!.RequestForData()

    }

    private fun init() {

        navController = Navigation.findNavController(this, R.id.main)
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.home,R.id.details, R.id.about),drawer)

    }


    override fun onSupportNavigateUp(): Boolean {
        //return NavigationUI.navigateUp(navController!!, appBarConfiguration!!)
        return findNavController(R.id.main).navigateUp(appBarConfiguration!!)
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}