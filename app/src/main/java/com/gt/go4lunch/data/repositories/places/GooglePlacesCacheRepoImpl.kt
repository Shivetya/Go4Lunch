package com.gt.go4lunch.data.repositories.places

import com.gt.go4lunch.data.PlacesSearchApiResponse

class GooglePlacesCacheRepoImpl(private val placesRepo: GooglePlacesRepo):
    GooglePlacesCacheRepo {

    override fun getListRestaurants(location: String): PlacesSearchApiResponse?{
        return placesRepo.getNearbyRestaurants(location)
    }
}