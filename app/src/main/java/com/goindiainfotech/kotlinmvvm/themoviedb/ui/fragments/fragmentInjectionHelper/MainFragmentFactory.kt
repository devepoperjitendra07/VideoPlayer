package com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.fragmentInjectionHelper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.details.FragmentDetailsTop
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.home.HomeFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainFragmentFactory
@Inject
constructor(
    private val someString: String
): FragmentFactory(){

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {

            HomeFragment::class.java.name -> {
                val fragment = HomeFragment(someString)
                fragment
            }

            FragmentDetailsTop::class.java.name -> {
                val fragment = FragmentDetailsTop()
                fragment
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}