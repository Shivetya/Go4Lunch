package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Close {

    @SerializedName("day")
    @Expose
    var day: Int? = null
    @SerializedName("time")
    @Expose
    var time: String? = null

}
