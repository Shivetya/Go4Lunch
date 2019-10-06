package com.gt.go4lunch.data.repositories.places

import android.media.Image
import com.gt.go4lunch.data.PlacesDetailsApiResponse
import com.gt.go4lunch.data.PlacesSearchApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {

    @GET("maps/api/place/nearbysearch/json")
    fun getNearbyRestaurants(@Query("location")locationLatLng: String)
            : Call<PlacesSearchApiResponse>

    @GET("maps/api/place/details/json")
    fun getDetailsForRestaurant(@Query("place_id")placeId: String)
            : Call<PlacesDetailsApiResponse>

    @GET("maps/api/place/photo")
    fun getPhotoForRestaurant(@Query("photoreference")photoReference: String)
            : Call<Image>
}