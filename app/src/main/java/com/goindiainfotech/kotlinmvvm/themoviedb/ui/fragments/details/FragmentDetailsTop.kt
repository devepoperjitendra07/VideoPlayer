package com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.details

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.goindiainfotech.kotlinmvvm.R
import com.goindiainfotech.kotlinmvvm.themoviedb.data.model.ObjectDetailsTrailers
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.activities.main.MainViewModel
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.activities.main.MvMainActivity
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.ConstFun.getUrlYoutube
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.ConstFun.setImageUrl
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.ConstStrings
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.StatusApi
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.gson.Gson
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details_top.*



@AndroidEntryPoint
class FragmentDetailsTop :Fragment(R.layout.fragment_details_top) , Player.EventListener{

    private val viewModel : MainViewModel by viewModels()

    private var idDetails = 0
    private var image: String? = null
    private var key: String? = null
    private var name: String? = null
    private var title: String? = null
    private var checkImage = false
    private var fb:String?=null
    private var tw:String?=null
    private var it:String?=null
    private var tm:String?=null
    private var im:String?=null
    private var urlVideo : String?=null
    private var TAG:String = "FragmentDetailsTop data"


    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition: Long = 0

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(requireContext(), "exoplayer-sample")
    }

    private fun preparePlayer(videoUrl: String) {
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
            Uri.parse(
                videoUrl
            )
        )
        simpleExoplayer.prepare(mediaSource)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aspectRatioFrameLayout.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        aspectRatioFrameLayout.setAspectRatio(16f / 9)

        initView()
        allObserFun()


    }


    fun releasePlayer() {
        try {
            playbackPosition = simpleExoplayer.currentPosition
            simpleExoplayer.release()
        }catch (e:java.lang.Exception){}

    }

    fun destroyPlayer() {
        try {
            simpleExoplayer.removeListener(this)
            releasePlayer()
        }catch (e:java.lang.Exception){}


    }

    override fun onDestroy() {
        try{
        simpleExoplayer.removeListener(this)
        releasePlayer()
        }catch (e:java.lang.Exception){}
        super.onDestroy()
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        hiddenFailed()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if (playbackState == Player.STATE_ENDED) {
            simpleExoplayer.playWhenReady = true
        }
        if (playbackState == Player.STATE_BUFFERING)
            progressBar.visibility = VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            progressBar.visibility = INVISIBLE
    }



    private fun initView() {
        click_keyboard_top.setOnClickListener {
            (requireActivity() as MvMainActivity).setMinimizeDraggable()

        }

        click_trailers_top.setOnClickListener {
            if (urlVideo!=null) {
                try {
                    releasePlayer()
                }catch (e: Exception){
                    e.printStackTrace()
                }

                simpleExoplayer = ExoPlayerFactory.newSimpleInstance(requireContext())

                simpleExoplayer.addListener(this)

                preparePlayer(urlVideo!!)
                video_player.player = simpleExoplayer
                simpleExoplayer.playWhenReady = true

                showPlayer()
            }

        }

    }

    private fun showPlayer() {
        clickTrailers(false)
        imageTrailers(false)
        fragmentYoutube(true)
    }

    private fun imageTrailers(b: Boolean) {
        if (b) img_trailer_top.show() else img_trailer_top.hide()
    }

    private fun fragmentYoutube(b: Boolean) {
        if (b) video_player.show() else video_player.hide()
    }

    private fun clickTrailers(b: Boolean) {
        if (b) click_trailers_top.show() else click_trailers_top.hide()
    }

    fun getIdDetails(): Int = idDetails

    fun setDetailsTop(id: Int, name: String, type: String, image: String) {
        this.idDetails = id
        this.image = image
        this.title = name
        setPausePlayer()
        setTmdb(id, type)
        setTypeTrailer(id, type)
    }

    fun setPausePlayer() {
        try {
            if (simpleExoplayer != null) {
                simpleExoplayer.playWhenReady = false
                simpleExoplayer.playbackState

            }
         }catch (e:Exception){}

    }

    private fun isPlaying(): Boolean {
        return simpleExoplayer != null &&
                simpleExoplayer.playbackState !== Player.STATE_ENDED
                && simpleExoplayer.playbackState !== Player.STATE_IDLE &&
                simpleExoplayer.playWhenReady


    }

    fun pausePlayVideo(){
        try {
            if (isPlaying()) {
                setPausePlayer()
            } else {
                startPauseVideo()

            }
        }catch (e:Exception){}

    }

   private fun startPauseVideo(){
       try {
           if (simpleExoplayer != null) {
               simpleExoplayer.playWhenReady = true
               simpleExoplayer.playbackState

           }

       }catch (e:Exception){}

    }

    private fun setTmdb(id: Int, type: String){
        when (type) {
            ConstStrings.MOVIE -> tm = "movie/$id"
            ConstStrings.TV -> tm = "tv/$id"
        }
    }


    private fun allObserFun(){
        setDataTrailer()

    }

    private fun progressDetails(b: Boolean) {
        if (b) progress_details_top.show() else progress_details_top.hide()
    }

    private fun failedDetails(b: Boolean) {
        if (b) failed_details_top.show() else failed_details_top.hide()
    }

    fun setImage() {
        urlVideo = null
        checkImage = true
        clickTrailers(false)
        fragmentYoutube(false)
        progressDetails(false)

        imageTrailers(true)
        setImageUrl("${ConstStrings.BASE_URL_IMAGE}${ConstStrings.SIZE_IMAGE_BACKDROP}$image")
    }

    private fun setDataTrailer(){
        viewModel.apiresponsMT.observe(requireActivity(), Observer {

            when (it.status) {
                StatusApi.SUCCESS -> {
                    val gson = Gson()
                    val data = gson.toJson(it)
                    Log.d(TAG, data)

                    it.data?.let { data ->
                        if (data.results.isNotEmpty()) {
                            if (!data.results[0].key.isNullOrEmpty())
                                setTrailer(data.results[0])
                            else setImage()
                        } else setImage()

                    }

                }
                StatusApi.LOADING -> {
                    progressDetails(true)
                    fragmentYoutube(false)
                    clickTrailers(false)
                    imageTrailers(false)
                    failedDetails(false)

                }
                StatusApi.ERROR -> {
                    //Handle Error

                    progressDetails(false)
                    failedDetails(true)

                }
            }


        })
    }

    fun hiddenFailed() {
        progressDetails(false)
        failedDetails(true)
    }

    private fun setImageUrl(url: String) {
        img_trailer_top.setImageUrl(url)
    }

    private fun hiddenLoading() {
        progressDetails(false)
        clickTrailers(true)
        imageTrailers(true)
        fragmentYoutube(true)
    }


    private fun clickKeyboardHideShow(b: Boolean) {
        if (b) click_keyboard_top.show() else click_keyboard_top.hide()
    }

    private fun setTrailer(data: ObjectDetailsTrailers) {
        key = data.key
        name = data.name
        requireActivity().getUrlYoutube(data.key){
            hiddenLoading()
            checkImage = false

            setImageUrl("${ConstStrings.BASE_URL_IMAGE_YOUTUBE}${data.key}${ConstStrings.SIZE_IMG_YOUTUBE}")

            if (it!=null) {
                urlVideo = it
                Log.d(TAG, it)

                /*try {
                    releasePlayer()
                }catch (e:Exception){
                    e.printStackTrace()
                }
                simpleExoplayer = ExoPlayerFactory.newSimpleInstance(requireContext())

                simpleExoplayer.addListener(this)

                preparePlayer(urlVideo!!)
                video_player.player = simpleExoplayer
                Log.d(TAG,"seekTo")
                simpleExoplayer.playWhenReady = true*/


            } else setImage()


        }

    }

    private fun setTypeTrailer(id: Int, type: String) {
        when (type) {
            ConstStrings.MOVIE -> {
                viewModel.fetchMTrailer("movie/$id/videos")

            }
            ConstStrings.TV -> {

            }
        }
    }




}