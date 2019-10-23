package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlacesSearchApiResponse (

    @SerializedName("html_attributions")
    @Expose
    var htmlAttributions: List<Any>? = null,
    @SerializedName("results")
    @Expose
    var results: List<Result>?,
    @SerializedName("status")
    @Expose
    var status: String? = null

)
