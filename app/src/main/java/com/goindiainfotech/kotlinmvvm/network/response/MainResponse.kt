package com.goindiainfotech.kotlinmvvm.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MainResponse {
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("APICODERESULT")
    @Expose
    var aPICODERESULT: String? = null

    @SerializedName("result")
    @Expose
    var result: Result? = null

}