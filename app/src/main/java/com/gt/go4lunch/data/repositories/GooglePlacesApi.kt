package com.gt.go4lunch.data.repositories

import com.gt.go4lunch.data.PlacesSearchApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface GooglePlacesApi {

    @GET("maps/api/place/nearbysearch/json")
    fun getNearbyRestaurants() : Call<PlacesSearchApiResponse>
}