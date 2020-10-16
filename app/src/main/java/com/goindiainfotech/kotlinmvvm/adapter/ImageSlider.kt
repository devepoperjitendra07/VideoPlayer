package com.goindiainfotech.kotlinmvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.goindiainfotech.kotlinmvvm.R
import java.util.*

class ImageSlider(
    private val context: Context,
    private val images: ArrayList<String>) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view =
            layoutInflater!!.inflate(R.layout.image_slider_layout_item, null)
        ////
        val imageView =
            view.findViewById<View>(R.id.iv_auto_image_slider) as ImageView
        val sliderItem = images[position]
        Glide.with(context)
            .load(sliderItem)
            .fitCenter()
            .into(imageView)
        view.setOnClickListener {
            /* if(position == 0){
                        Toast.makeText(context, "Slide 1 Clicked", Toast.LENGTH_SHORT).show();
                    } else if(position == 1){
                        Toast.makeText(context, "Slide 2 Clicked", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Slide 3 Clicked", Toast.LENGTH_SHORT).show();
                    }*/
        }
        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

}