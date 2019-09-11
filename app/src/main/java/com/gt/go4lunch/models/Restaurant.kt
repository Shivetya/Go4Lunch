package com.gt.go4lunch.models

import android.location.Location

data class Restaurant(
    val name: String,
    val urlPicture: String?,
    val address: String,
    val isOpen: Boolean?,
    val latLng: List<Float>,
    val types: List<String>?,
    val location: Location
)