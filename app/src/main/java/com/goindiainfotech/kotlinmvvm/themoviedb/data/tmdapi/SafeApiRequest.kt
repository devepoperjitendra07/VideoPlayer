package com.goindiainfotech.kotlinmvvm.themoviedb.data.tmdapi

import com.goindiainfotech.kotlinmvvm.themoviedb.utils.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber

abstract class SafeApiRequest {


    suspend fun <T : Any> apiRequest(call1: suspend () -> Response<T>): T {

        val call = call1.invoke()
        if (call.isSuccessful) {
            if (call.code() == 200) {
                Timber.d("SafeApiRequest %s", call.body().toString())
                return call.body()!!
            } else {
                Timber.d("SafeApiRequest %s", "1  %s" + call.code().toString())
                Timber.d("SafeApiRequest %s", "2  " + call.body().toString())
                Timber.d("SafeApiRequest %s", "3  " + call.message())
                Timber.d("SafeApiRequest %s", "4  " + call.errorBody().toString())
               // Timber.d("SafeApiRequest %s", "5  " + call.headers().size().toString())
               // Timber.d("SafeApiRequest %s", "6  " + call.raw().message())
                Timber.d("SafeApiRequest %s", "7  " + call.raw().header("message"))
               // Timber.d("SafeApiRequest %s", "8  " + call.raw().networkResponse().toString())
                throw ApiException(JSONObject(call.message()).getString("message"))

            }
        } else {
            val error = call.errorBody()?.string()
            Timber.d("SafeApiRequest %s", call.errorBody()?.string())
            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                    Timber.d("SafeApiRequest %s", JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                }
                message.append("\n")

            }
            //message.append("Error code: $error")
            //message.append("Error code: ${call.code()}")
            //message.append("Error : ${JSONObject(call.message()).getString("message")}")
            Timber.d("SafeApiRequest %s", message.toString())
            Timber.d("SafeApiRequest %s", call.message())
            Timber.d("SafeApiRequest %s", call.headers().toString())
            throw ApiException(message.toString())
        }
    }

}