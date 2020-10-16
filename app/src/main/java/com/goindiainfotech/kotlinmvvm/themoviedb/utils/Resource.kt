package com.goindiainfotech.kotlinmvvm.themoviedb.utils

data class Resource<out T>(val status: StatusApi, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(StatusApi.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(StatusApi.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(StatusApi.LOADING, data, null)
        }

        fun <T> unknown(data: T? = null): Resource<T>{
            return Resource(StatusApi.UNKNOWN, data,null)

        }


    }

}