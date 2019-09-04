package com.gt.go4lunch.usecases

import com.gt.go4lunch.data.PlacesSearchApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface GooglePlacesApi {

    @GET("maps/api/place/nearbysearch/json")
    fun getNearbyRestaurant() : Call<PlacesSearchApiResponse>
}