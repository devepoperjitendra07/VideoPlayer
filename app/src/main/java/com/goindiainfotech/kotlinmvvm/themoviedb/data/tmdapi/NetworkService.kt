package com.goindiainfotech.kotlinmvvm.themoviedb.data.tmdapi

import com.goindiainfotech.kotlinmvvm.themoviedb.data.model.ObjectMovies
import com.goindiainfotech.kotlinmvvm.themoviedb.data.model.ObjectTrailers
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface NetworkService {

    @GET
    suspend fun getMovies(@Url url: String,@QueryMap params: MutableMap<String, String>): Response<ObjectMovies>

    @GET
    suspend fun getTrailer(@Url url: String, @QueryMap params: MutableMap<String, String>): Response<ObjectTrailers>

}