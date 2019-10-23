package com.gt.go4lunch.usecases

import com.gt.go4lunch.data.PlacesDetailsApiResponse
import com.gt.go4lunch.data.PlacesSearchApiResponse
import com.gt.go4lunch.data.repositories.places.GooglePlacesCacheRepo
import com.gt.go4lunch.data.repositories.places.GooglePlacesCacheRepoImpl


class GoogleListRestaurantsUseCase(private val placesCacheRepo: GooglePlacesCacheRepo): GoogleListRestaurant {

    companion object{
        val instance: GoogleListRestaurantsUseCase by lazy{
            GoogleListRestaurantsUseCase(GooglePlacesCacheRepoImpl.instance)
        }
    }

    override suspend fun getListRestaurant(location: String): PlacesSearchApiResponse?{

        return placesCacheRepo.getListRestaurants(location)
    }

    override suspend fun getDetailRestaurant(restaurantID: String): PlacesDetailsApiResponse? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}