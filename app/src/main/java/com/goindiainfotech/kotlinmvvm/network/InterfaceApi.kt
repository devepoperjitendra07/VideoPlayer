package com.goindiainfotech.kotlinmvvm.network

import com.goindiainfotech.kotlinmvvm.network.response.MainResponse
import retrofit2.Call
import retrofit2.http.GET

interface InterfaceApi {

    @GET("test/testing_data")
    fun searchData(): Call<MainResponse?>?

}