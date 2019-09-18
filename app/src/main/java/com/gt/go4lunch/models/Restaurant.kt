package com.gt.go4lunch.models

data class Restaurant(
    val name: String?,
    val urlPicture: String?,
    val address: String?,
    val isOpen: Boolean?,
    val latLng: List<Double?>,
    val types: List<String>?
)