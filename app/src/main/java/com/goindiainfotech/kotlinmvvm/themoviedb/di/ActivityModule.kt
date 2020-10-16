package com.goindiainfotech.kotlinmvvm.themoviedb.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.activities.main.MvMainActivity
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.details.FragmentDetailsBottom
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.details.FragmentDetailsTop
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object ActivityModule {

    @Provides
    fun provideSupportFragmentManager(activity: MvMainActivity): FragmentManager = activity.supportFragmentManager

    @Provides
    fun provideFragmentDetailsTop(): FragmentDetailsTop = FragmentDetailsTop()

    @Provides
    fun provideFragmentDetailsBottom(): FragmentDetailsBottom = FragmentDetailsBottom()

}