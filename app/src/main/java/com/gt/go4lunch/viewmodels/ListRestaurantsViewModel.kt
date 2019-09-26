package com.gt.go4lunch.viewmodels

import android.location.Location
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gt.go4lunch.models.Restaurant
import com.gt.go4lunch.usecases.GoogleListRestaurant
import kotlinx.coroutines.*
import kotlin.math.*


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
                    Restaurant(it.name,
                        it.icon,
                        it.vicinity,
                        it.openingHours?.openNow.toString(),
                        distance.toString().substringBefore("."),
                        it.types?.joinToString(
                            separator = ", "
                        ))
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
}