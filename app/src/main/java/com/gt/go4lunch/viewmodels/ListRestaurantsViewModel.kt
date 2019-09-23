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

    suspend fun getListRestaurant(location: Location){

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
                val lat1 = location.latitude
                val lng1 = location.longitude
                val lat2 = it.geometry.location.lat
                val lng2 = it.geometry.location.lng

                val distance = calculateDistance(lat1, lng1, lat2, lng2)
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

    @VisibleForTesting
    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double{

        val earthRadius = 6378137

        val rLat1 = transformDegreeToRad(lat1)
        val rLng1 = transformDegreeToRad(lng1)
        val rLat2 = transformDegreeToRad(lat2)
        val rLng2 = transformDegreeToRad(lng2)

        val dLo = (rLat2 - rLat1)/2
        val dLa = (rLng2 - rLng1)/2

        val a = (sin(dLa)*sin(dLa)) + cos(rLat1)* cos(rLat2) * (sin(dLo)* sin(dLo))

        val d = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * d

    }

    private fun transformDegreeToRad(angle: Double): Double{
        return Math.PI*angle/180
    }
}