package com.gt.go4lunch.data.repositories.places

import com.gt.go4lunch.data.PlacesSearchApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {

    @GET("maps/api/place/nearbysearch/json")
    fun getNearbyRestaurants(@Query("location")locationLatLng: String)
            : Call<PlacesSearchApiResponse>
}