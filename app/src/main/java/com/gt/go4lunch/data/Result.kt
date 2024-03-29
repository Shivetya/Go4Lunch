package com.gt.go4lunch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result (

    @SerializedName("geometry")
    @Expose
    var geometry: Geometry,
    @SerializedName("icon")
    @Expose
    var icon: String? = null,
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("opening_hours")
    @Expose
    var openingHours: OpeningHours? = null,
    @SerializedName("photos")
    @Expose
    var photos: List<Photo>? = null,
    @SerializedName("place_id")
    @Expose
    var placeId: String = "",
    @SerializedName("reference")
    @Expose
    var reference: String? = null,
    @SerializedName("types")
    @Expose
    var types: List<String>? = null,
    @SerializedName("vicinity")
    @Expose
    var vicinity: String? = "",
    @SerializedName("rating")
    @Expose
    var rating: Double? = 0.0

)
