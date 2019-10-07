package com.gt.go4lunch.data.repositories.places

import android.media.Image
import com.gt.go4lunch.data.PlacesDetailsApiResponse
import com.gt.go4lunch.data.PlacesSearchApiResponse

interface GooglePlacesRepo {

    fun getNearbyRestaurants(location: String): PlacesSearchApiResponse?
    fun getDetailsRestaurant(restaurantId: String): PlacesDetailsApiResponse?
    fun getPhotoRestaurant(photoId: String): Image?
}