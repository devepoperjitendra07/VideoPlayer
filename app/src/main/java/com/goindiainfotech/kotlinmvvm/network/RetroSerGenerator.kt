package com.goindiainfotech.kotlinmvvm.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroSerGenerator {
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl("http://13.232.62.239/talzo/dummy/")
        .addConverterFactory(GsonConverterFactory.create())
    private val retrofit = retrofitBuilder.build()
    val callSerInterface = retrofit.create(
        InterfaceApi::class.java
    )

}