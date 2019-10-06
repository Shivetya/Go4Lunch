package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PlacesDetailsApiResponse {

    @SerializedName("html_attributions")
    @Expose
    var htmlAttributions: List<Any>? = null
    @SerializedName("result")
    @Expose
    var result: Result? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}
