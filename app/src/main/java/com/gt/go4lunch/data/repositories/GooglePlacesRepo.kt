package com.gt.go4lunch.data.repositories

import com.gt.go4lunch.data.PlacesSearchApiResponse

interface GooglePlacesRepo {

    fun getNearbyRestaurants(): PlacesSearchApiResponse
}