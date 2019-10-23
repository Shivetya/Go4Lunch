package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Geometry (

    @SerializedName("location")
    @Expose
    var location: Location

)
