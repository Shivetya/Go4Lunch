package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OpeningHours {

    @SerializedName("open_now")
    @Expose
    var openNow: Boolean? = null

}
