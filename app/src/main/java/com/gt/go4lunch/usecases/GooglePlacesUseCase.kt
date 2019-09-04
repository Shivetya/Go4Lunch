package com.gt.go4lunch.usecases

import com.gt.go4lunch.data.PlacesSearchApiResponse

interface GooglePlacesUseCase {

    fun getNearbyRestaurant(): PlacesSearchApiResponse
}