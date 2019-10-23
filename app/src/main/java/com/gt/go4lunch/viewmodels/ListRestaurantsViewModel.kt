package com.gt.go4lunch.viewmodels

import android.location.Location
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gt.go4lunch.models.Restaurant
import com.gt.go4lunch.usecases.GoogleListRestaurant
import com.gt.go4lunch.utils.OpeningHoursCalculator
import com.gt.go4lunch.utils.RatingCalculator
import kotlinx.coroutines.*


class ListRestaurantsViewModel(private val listRestaurantsUseCase: GoogleListRestaurant): ViewModel() {

    private var currentJob: Job? = null

    private val _listRestaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()
    val listRestaurants: LiveData<List<Restaurant>> = _listRestaurants

    fun getListRestaurant(location: Location){

        cancelJobIfActive()

        currentJob = GlobalScope.launch(Dispatchers.IO){
            fetchListRestaurants(location)
        }
    }

    @VisibleForTesting
    suspend fun fetchListRestaurants(location: Location){

        val locationQueryReady = transformLocationQueryReady(location)

        val listRestaurants = listRestaurantsUseCase.getListRestaurant(locationQueryReady)
            ?.results
            ?.map {

                val locationRestaurant = Location("restaurant")
                locationRestaurant.latitude = it.geometry.location.lat
                locationRestaurant.longitude = it.geometry.location.lng

                val distance = location.distanceTo(locationRestaurant)
                    Restaurant(name = it.name,
                        urlPicture =  it.photos?.get(0)?.photoReference,
                        address =  it.vicinity,
                        isOpen = getOpenOrCloseString(it.placeId),
                        distance = distance.toString().substringBefore("."),
                        types = it.types?.joinToString(
                            separator = ", "
                        ),
                        id = it.placeId,
                        rating = getRatingForStars(it.placeId)
                    )
            }

        withContext(Dispatchers.Main){
            _listRestaurants.value = listRestaurants
        }

    }


    @VisibleForTesting
    fun transformLocationQueryReady(locationToTransform: Location): String{
        val lat = locationToTransform.latitude
        val lng = locationToTransform.longitude

        return "$lat,$lng"
    }

    private fun cancelJobIfActive(){

        currentJob?.let {
            if(it.isActive){
                it.cancel()
            }
        }
    }

    private suspend fun getOpenOrCloseString(restaurantID: String): String{

        val listPeriod = listRestaurantsUseCase.getDetailRestaurant(restaurantID)?.result?.openingHours?.periods

        val openingHoursCalculator = OpeningHoursCalculator(listPeriod)

        return openingHoursCalculator.stringOpenUntilOrClose

    }

    private suspend fun getRatingForStars(restaurantID: String): Int{

        val googleRating = listRestaurantsUseCase.getDetailRestaurant(restaurantID)?.result?.rating

        return if (googleRating != null){
            val ratingCalculator = RatingCalculator(googleRating)
            ratingCalculator.howManyStars
        }else {
            0
        }
    }
}