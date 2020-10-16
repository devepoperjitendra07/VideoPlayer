package com.goindiainfotech.kotlinmvvm.themoviedb.data.repository


import com.goindiainfotech.kotlinmvvm.themoviedb.data.model.ObjectMovies
import com.goindiainfotech.kotlinmvvm.themoviedb.data.model.ObjectTrailers
import com.goindiainfotech.kotlinmvvm.themoviedb.data.tmdapi.NetworkService
import retrofit2.Response

import javax.inject.Inject

class MainRepository @Inject constructor(private val networkService: NetworkService)  {

    suspend fun getMoviesData(url: String, params: MutableMap<String, String>): Response<ObjectMovies> {
        return  networkService.getMovies(url,params)

    }

    suspend fun getTailer(url: String, params: MutableMap<String, String>): Response<ObjectTrailers> {
        return networkService.getTrailer(url,params)

    }


}