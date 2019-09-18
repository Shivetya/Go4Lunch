package com.gt.go4lunch.usecases

import android.location.Location
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gt.go4lunch.data.repositories.places.GooglePlacesCacheRepo
import com.gt.go4lunch.data.repositories.places.GooglePlacesCacheRepoImpl
import com.gt.go4lunch.models.Restaurant
import kotlinx.coroutines.*

class GoogleListRestaurantsUseCase(private val placesCacheRepo: GooglePlacesCacheRepo) {

    private val _listRestaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()
    val listRestaurants: LiveData<List<Restaurant>> = _listRestaurants

    var currentJob: Job? = null

    companion object{
        val instance: GoogleListRestaurantsUseCase by lazy{
            GoogleListRestaurantsUseCase(GooglePlacesCacheRepoImpl.instance)
        }
    }

    fun getListRestaurant(location: Location){

        val locationQueryReady = transformLocationQueryReady(location)

        cancelJobIfActive()

        currentJob = GlobalScope.launch(Dispatchers.IO){
            fetchListRestaurants(locationQueryReady)
        }

    }

    @VisibleForTesting
    suspend fun fetchListRestaurants(location: String){

        val listRestaurant = placesCacheRepo.getListRestaurants(location)
            ?.results?.map{

            Restaurant(it.name,
                it.icon,
                it.vicinity,
                it.openingHours?.openNow,
                listOf(it.geometry?.location?.lat, it.geometry?.location?.lng),
                it.types)
        }

        withContext(Dispatchers.Main){
            _listRestaurants.value = listRestaurant
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