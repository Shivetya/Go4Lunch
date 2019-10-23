package com.gt.go4lunch.viewmodels

import android.location.Location
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gt.go4lunch.models.RestaurantMarker
import com.gt.go4lunch.usecases.GoogleListRestaurant
import kotlinx.coroutines.*

class GoogleMapViewModel(private val listRestaurantsUseCase: GoogleListRestaurant): ViewModel(){

    private val _listRestaurantMarker: MutableLiveData<List<RestaurantMarker>> = MutableLiveData()
    val listRestaurantMarker: LiveData<List<RestaurantMarker>> = _listRestaurantMarker

    private var currentJob: Job? = null

    fun getListRestaurantMarker(location: Location){

        cancelJobIfActive()

        currentJob = GlobalScope.launch(Dispatchers.IO) {
            fetchListRestaurantMarker(location)
        }
    }

    @VisibleForTesting
    suspend fun fetchListRestaurantMarker(location: Location){

        val locationQueryReady = transformLocationQueryReady(location)

        val listRestaurantMarker = listRestaurantsUseCase.getListRestaurant(locationQueryReady)
            ?.results
            ?.map {
                RestaurantMarker(it.name,
                    it.geometry.location.lat,
                    it.geometry.location.lng,
                    it.placeId)
            }

        withContext(Dispatchers.Main){
            _listRestaurantMarker.value = listRestaurantMarker
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