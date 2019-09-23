package com.gt.go4lunch.usecases

import com.gt.go4lunch.data.PlacesSearchApiResponse

interface GoogleListRestaurant {

    suspend fun getListRestaurant(location: String): PlacesSearchApiResponse?
}