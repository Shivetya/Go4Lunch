package com.gt.go4lunch.usecases

import com.gt.go4lunch.data.PlacesDetailsApiResponse
import com.gt.go4lunch.data.PlacesSearchApiResponse

interface GoogleListRestaurant {

    suspend fun getListRestaurant(location: String): PlacesSearchApiResponse?
    suspend fun getDetailRestaurant(restaurantID: String): PlacesDetailsApiResponse?
}