package com.goindiainfotech.kotlinmvvm.themoviedb.ui.fragments.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.goindiainfotech.kotlinmvvm.R
import com.goindiainfotech.kotlinmvvm.themoviedb.data.model.PojoResultsMovie
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.activities.main.MainViewModel
import com.goindiainfotech.kotlinmvvm.themoviedb.ui.adaptermovie.ViewPagerPopularMovie
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.ConstFun
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.ConstStrings
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.MyCountDownTimer
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.StatusApi
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class HomeFragment constructor(
    private val someString: String
): Fragment(R.layout.home_fragment) , ViewPagerPopularMovie.OnItemClickListener{

    @Named(ConstStrings.HORIZONTAL)
    @Inject
    lateinit var lManagerMovieNowPlaying: GridLayoutManager
    @Named(ConstStrings.HORIZONTAL)
    @Inject
    lateinit var lManagerOnTv : GridLayoutManager

    private lateinit var callback:Callback



    private var timer: MyCountDownTimer?=null
    //val viewModel: MainViewModel by navGraphViewModels(R.id.main_mv) { defaultViewModelProviderFactory }
    private val viewModel : MainViewModel by viewModels()

    init {
        Log.d("HomeFragment", someString)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetch()
        setupObserver()


    }

    private fun setupObserver() {
        viewModel.movies.observe(requireActivity(), Observer {
            when (it.status) {
                StatusApi.SUCCESS -> {
                    progress_popular_movie.visibility = View.GONE
                    viewpager_popular_movie.visibility = View.VISIBLE
                    var gson = Gson()
                    var data = gson.toJson(it)
                    Log.d("data", data)

                    it.data?.let { users -> setDataMoviesPopular(users.results) }

                }
                StatusApi.LOADING -> {
                    progress_popular_movie.visibility = View.VISIBLE
                }
                StatusApi.ERROR -> {
                    //Handle Error
                    progress_popular_movie.visibility = View.GONE
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        })
    }


    fun setDataMoviesPopular(list: MutableList<PojoResultsMovie>) {
        val pagerAdapter = ViewPagerPopularMovie(requireContext(), list)
        viewpager_popular_movie.apply {
            setScrollDurationFactor(4)
            setPageTransformer(
                true,
                ConstFun.parallaxPageTransformer(R.id.cardview_pager, R.id.id_text)
            )
            adapter = pagerAdapter
        }
        pagerAdapter.notifyDataSetChanged()
        pagerAdapter.setOnItemClickListener(this)
        pageSwitcher(list)
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = activity as Callback
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }

    override fun onItemCastClicked(id: Int, title: String, type: String, image: String) {
        callback.setMaximizeDraggable(id, title, type, image)

    }


    private fun pageSwitcher(list: MutableList<PojoResultsMovie>){
        timer = MyCountDownTimer(5000, 5000){
            try {
                if (list.size-1 == viewpager_popular_movie.currentItem) viewpager_popular_movie.currentItem = 0
                else viewpager_popular_movie.currentItem = viewpager_popular_movie.currentItem+1
                timer!!.start()
            }catch (t: Throwable){
                timer!!.cancel()
            }
        }
        timer!!.start()
    }

    fun onStopTimer(){
        if (timer!=null) timer!!.cancel()
    }

    override fun onPause() {
        super.onPause()
        onStopTimer()
    }

    override fun onStart() {
        super.onStart()
        if (isAdded) if (timer!=null) timer!!.start()
    }



    interface Callback {
        fun setMaximizeDraggable(id: Int, name: String, type: String, image: String)

    }



}