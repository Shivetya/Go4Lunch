package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OpeningHours (

    @SerializedName("open_now")
    @Expose
    var openNow: Boolean?,
    @SerializedName("periods")
    @Expose
    var periods: List<Period>? = null,
    @SerializedName("weekday_text")
    @Expose
    var weekdayText: List<String>? = null

)
