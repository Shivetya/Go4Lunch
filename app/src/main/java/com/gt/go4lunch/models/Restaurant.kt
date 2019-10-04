package com.gt.go4lunch.models

data class Restaurant(
    val name: String,
    val urlPicture: String?,
    val address: String,
    val isOpen: String,
    val distance: String,
    val types: String?,
    val id: String
)