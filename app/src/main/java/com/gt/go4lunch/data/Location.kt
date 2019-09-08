package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location {

    @SerializedName("lat")
    @Expose
    var lat: Float? = null
    @SerializedName("lng")
    @Expose
    var lng: Float? = null

}
