package com.goindiainfotech.kotlinmvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.goindiainfotech.kotlinmvvm.network.ApiClient
import com.goindiainfotech.kotlinmvvm.network.response.MainResponse

class Repository private constructor() {


    private val mApiClient: ApiClient = ApiClient.instance!!
    private val mIsQueryExhausted = MutableLiveData<Boolean>()
    private val maindata = MediatorLiveData<MainResponse>()

    init {
        getData()
    }


    private fun getData() {
        val rApiSource = mApiClient.mainapidata
        maindata.addSource(rApiSource) { it ->
            if (it != null) {
                maindata.value = it
                doneQuery(it)
            } else {
                doneQuery(null)
            }
        }
    }

    private fun doneQuery(list: MainResponse?) {
        if (list != null) {
            mIsQueryExhausted.setValue(true)
        } else {
            mIsQueryExhausted.setValue(true)
        }
    }

    val isQueryExhausted: LiveData<Boolean>
        get() = mIsQueryExhausted

    val callapiData: LiveData<MainResponse>
        get() = maindata

    fun searchRecipesApi() {
        mIsQueryExhausted.value = false
        mApiClient.searchApiData()
        Log.d("data", "searchRecipesApi called")
    }

    fun cancelRequest() {
        mApiClient.cancelRequest()
    }

    val isRecipeRequestTimedOut: LiveData<Boolean>
        get() = mApiClient.isRequestTimedOut

    companion object {
        var instance: Repository? = null
            get() {
                if (field == null) {
                    field = Repository()
                }
                return field
            }
            private set
    }


}