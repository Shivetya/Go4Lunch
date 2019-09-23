package com.gt.go4lunch.data.repositories.location

import android.app.Application
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.gt.go4lunch.MainApplication

class LocationRepoImpl private constructor(app: Application):
    LocationRepo {

    companion object {
        val instance: LocationRepo by lazy {
            LocationRepoImpl(MainApplication.getInstance())
        }
    }
    private var isListeningToLocation = false

    private val locationLiveData: MutableLiveData<Location> = MutableLiveData()

    private val locationProviderClient = LocationServices.getFusedLocationProviderClient(app)

    private val locationCallback = object: LocationCallback(){
        override fun onLocationResult(newLocation: LocationResult?) {
            locationLiveData.postValue(newLocation?.lastLocation)
        }
    }

    override fun getLocationLiveData(): LiveData<Location> = locationLiveData

    override fun beginSearchLocation() {
        if (!isListeningToLocation){
            isListeningToLocation = true

            val locationRequest = LocationRequest()
                .setInterval(30_000)
                .setPriority(PRIORITY_BALANCED_POWER_ACCURACY)

            locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override fun stopLocationUpdate(){
        locationProviderClient.removeLocationUpdates(locationCallback)
    }
}