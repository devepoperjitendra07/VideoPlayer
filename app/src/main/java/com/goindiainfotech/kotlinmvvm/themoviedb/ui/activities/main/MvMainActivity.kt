package com.goindiainfotech.kotlinmvvm.themoviedb.ui.activities.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.goindiainfotech.kotlinmvvm.R
import com.goindiainfotech.kotlinmvvm.drag.DragView
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.details.FragmentDetailsBottom
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.details.FragmentDetailsTop
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_mv_main.*

import kotlinx.android.synthetic.main.layout_bottom.*
import kotlinx.android.synthetic.main.layout_top.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MvMainActivity : AppCompatActivity(), HomeFragment.Callback {


    var navController: NavController? = null
    var appBarConfiguration: AppBarConfiguration? = null

    @Inject
    lateinit var draggableTopFragment: FragmentDetailsTop
    @Inject
    lateinit var draggableBottomFragment: FragmentDetailsBottom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mv_main)
        setSupportActionBar(toolbar)

        init()

        NavigationUI.setupActionBarWithNavController(
            this,
            navController!!,
            appBarConfiguration!!
        )
        NavigationUI.setupWithNavController(navigationView!!, navController!!)
        //NavigationUI.setupWithNavController(bottomNavigation!!, navController!!)

        initializeDraggable()


    }



    private fun initializeDraggable() {

        dragView.setDragListener(object : DragView.DragListener {
            override fun onChangeState(state: DragView.State) {


            }

            override fun onChangePercent(percent: Float) {
                //alpha.alpha = 1 - percent
                shadow.alpha = percent
            }

        })

        supportFragmentManager.beginTransaction().add(R.id.frameTop, draggableTopFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.frameBottom, draggableBottomFragment).commit()


        ivClose.setOnClickListener {
            draggableTopFragment.destroyPlayer()
            dragView.close()
        }

        ivPause.setOnClickListener{
            draggableTopFragment.pausePlayVideo()

        }

    }

    private fun init() {

        navController = Navigation.findNavController(this, R.id.main_mv)
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.home_mv, R.id.about_mv), drawer_layout)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                Toast.makeText(this,"search click",Toast.LENGTH_LONG).show();
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_mv)
        return navController.navigateUp(appBarConfiguration!!) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
            drawer_layout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    override fun setMaximizeDraggable(id: Int, name: String, type: String, image: String) {
        Toast.makeText(applicationContext, type + " :: " + name, Toast.LENGTH_LONG).show()
        if (draggableTopFragment.getIdDetails() != id) {
            draggableTopFragment.setDetailsTop(id, name, type, image)

        }
        dragView.maximize()

    }


    fun setMinimizeDraggable() {
        dragView.minimize()

    }

    override fun onDestroy() {
        draggableTopFragment.destroyPlayer()
        super.onDestroy()
    }



}