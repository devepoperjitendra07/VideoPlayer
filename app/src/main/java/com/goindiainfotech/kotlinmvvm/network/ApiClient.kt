package com.goindiainfotech.kotlinmvvm.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goindiainfotech.kotlinmvvm.AppExecutors
import com.goindiainfotech.kotlinmvvm.network.response.MainResponse
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class ApiClient private constructor() {

    private val mainData: MutableLiveData<MainResponse?> = MutableLiveData()
    private var mRetrieveRecipesRunnable: RetrieveRecipesRunnable? = null
    private val mRequestTimeout = MutableLiveData<Boolean>()

    companion object {
        private const val TAG = "ApiClient"
        var instance: ApiClient? = null
            get() {
                if (field == null) {
                    field = ApiClient()
                }
                return field
            }
            private set
    }




    val mainapidata: LiveData<MainResponse?>
        get() = mainData

    val isRequestTimedOut: LiveData<Boolean>
        get() = mRequestTimeout

    fun searchApiData() {
        if (mRetrieveRecipesRunnable != null) {
            mRetrieveRecipesRunnable = null
        }
        mRetrieveRecipesRunnable = RetrieveRecipesRunnable()
        val handler = AppExecutors.instance!!.networkIO().submit(mRetrieveRecipesRunnable)
        Log.d("data", "AppExecutors called")
        AppExecutors.instance!!.networkIO().schedule({
            // let the user know its timed out
            Log.d("data", "AppExecutors called")
            handler.cancel(true)
        }, 5000, TimeUnit.MILLISECONDS)

    }

    private inner class RetrieveRecipesRunnable : Runnable {
        var cancelRequest = false
        override fun run() {
            try {
                val response: Response<*> = getApiData!!.execute()
                Log.d("data", "etRecipes().execute() called")
                if (cancelRequest) {
                    return
                }
                if (response.code() == 200) {
                    Log.d("data", "200 response called")
                    val currentData = response.body() as MainResponse?
                    Log.d("data", "200 response called" + currentData!!.result.toString())
                    mainData.postValue(currentData)

                } else {
                    val error = response.errorBody()!!.string()
                    Log.d("data", "error   $error")
                    mainData.postValue(null)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("data", "error   " + e.localizedMessage)
                Log.d("data", "error   " + e.message)
                Log.d("data", "error   " + e.stackTrace)
                mainData.postValue(null)
            }
        }

        private val getApiData: Call<MainResponse?>?
            private get() = RetroSerGenerator.callSerInterface.searchData()

        fun cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling the search request.")
            cancelRequest = true
        }

    }

    fun cancelRequest() {
        if (mRetrieveRecipesRunnable != null) {
            mRetrieveRecipesRunnable!!.cancelRequest()
        }
    }



}