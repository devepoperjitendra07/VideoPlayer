package com.goindiainfotech.kotlinmvvm.themoviedb.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log.e
import android.util.SparseArray

import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.goindiainfotech.kotlinmvvm.BuildConfig
import com.goindiainfotech.kotlinmvvm.R
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.ConstStrings.Companion.BASE_URL_BROWSER_YOUTUBE
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.pawegio.kandroid.start
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object ConstFun {

    fun getApiKeyTmdb(): String = BuildConfig.API_KEY

    private inline fun <reified V : View> View.find(@IdRes id: Int): V = findViewById(id)

    var version = "v${BuildConfig.VERSION_NAME}"

    fun getLocale(): String = Locale.getDefault().toString().replace("_", "-")

    fun getCountry(): String = Locale.getDefault().country


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun ImageView.setImageUrl(url: String) {
        Picasso.get().load(url)
                .placeholder(context.getDrawable(R.drawable.ic_placeholder)!!)
                .error(context.getDrawable(R.drawable.ic_placeholder)!!)
                .into(this)
    }


    @SuppressLint("StaticFieldLeak")
    fun Context.getUrlYoutube(key: String, setUrl:(url:String?)-> Unit){
        try {
            object : YouTubeExtractor(this){
                override fun onExtractionComplete(p0: SparseArray<YtFile>?, p1: VideoMeta?) {
                    setUrl(p0?.get(22)?.url)
                }
            }.extract(BASE_URL_BROWSER_YOUTUBE+key,false,false)
        }catch (t:Throwable){
            e("youtube",t.toString())
        }
    }

  

    fun parallaxPageTransformer(vararg v : Int) : ViewPager.PageTransformer = ViewPager.PageTransformer { page, position ->
        for (element in v){
            val view = page.findViewById<View>(element)
            if (view!=null) ViewCompat.setTranslationX(view,(page.width/1.5 * position).toFloat())
        }
    }



}
