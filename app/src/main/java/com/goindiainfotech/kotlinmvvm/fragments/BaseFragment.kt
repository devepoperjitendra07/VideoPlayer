package com.goindiainfotech.kotlinmvvm.fragments

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

open class BaseFragment:Fragment() {

    fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .into(view)
    }

}