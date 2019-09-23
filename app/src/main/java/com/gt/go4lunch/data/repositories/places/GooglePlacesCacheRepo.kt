package com.gt.go4lunch.data.repositories.places

import com.gt.go4lunch.data.PlacesSearchApiResponse

interface GooglePlacesCacheRepo {
    suspend fun getListRestaurants(location: String): PlacesSearchApiResponse?
}