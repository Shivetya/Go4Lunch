package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Open (

    @SerializedName("day")
    @Expose
    var day: Int?,
    @SerializedName("time")
    @Expose
    var time: String?

)