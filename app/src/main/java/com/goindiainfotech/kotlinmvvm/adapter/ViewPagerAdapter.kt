package com.goindiainfotech.kotlinmvvm.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.goindiainfotech.kotlinmvvm.R
import com.goindiainfotech.kotlinmvvm.fragments.FirstFragment
import com.goindiainfotech.kotlinmvvm.fragments.DetailsFragment

class ViewPagerAdapter(
    private val mContext: Context,
    fm: FragmentManager?
) : /*FragmentPagerAdapter(fm!!)*/ FragmentStatePagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            FirstFragment()
        } else {
            DetailsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources
            .getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }

    companion object {
        @StringRes
        private val TAB_TITLES =
            intArrayOf(R.string.offers, R.string.details)
    }

}