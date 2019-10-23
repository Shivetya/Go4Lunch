package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlacesDetailsApiResponse (

    @SerializedName("html_attributions")
    @Expose
    var htmlAttributions: List<Any>?,
    @SerializedName("result")
    @Expose
    var result: Result?,
    @SerializedName("status")
    @Expose
    var status: String?

)
