package com.goindiainfotech.kotlinmvvm.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goindiainfotech.kotlinmvvm.network.response.MainResponse
import com.goindiainfotech.kotlinmvvm.repository.Repository

class PageViewModel : ViewModel() {

    private var mRepository: Repository? = null
    private var mDidRetrieveData = false
    var mainRes = MutableLiveData<MainResponse>()

    fun setMainData(mainResponse: MainResponse){
        mainRes.value = mainResponse
    }

    fun init() {
        mRepository = Repository.instance
        mDidRetrieveData = false
    }


    fun getLiveData(): LiveData<MainResponse>? {
        return mRepository!!.callapiData
    }

    fun RequestForData() {
        mRepository!!.searchRecipesApi()
    }

    fun isRequestTimedOut(): LiveData<Boolean> {
        return mRepository!!.isRecipeRequestTimedOut
    }


    fun didRetrieveData(): Boolean {
        return mDidRetrieveData
    }

}