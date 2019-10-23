package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Period (

    @SerializedName("close")
    @Expose
    var close: Close?,
    @SerializedName("open")
    @Expose
    var open: Open?

)