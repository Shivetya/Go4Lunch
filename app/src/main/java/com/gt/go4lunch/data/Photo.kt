package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Photo (

    @SerializedName("height")
    @Expose
    var height: Int?,
    @SerializedName("html_attributions")
    @Expose
    var htmlAttributions: List<Any>?,
    @SerializedName("photo_reference")
    @Expose
    var photoReference: String?,
    @SerializedName("width")
    @Expose
    var width: Int? = null

)
