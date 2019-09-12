package com.gt.go4lunch.data.repositories

import android.location.Location
import androidx.lifecycle.LiveData

interface LocationRepo {
    fun getLocationLiveData(): LiveData<Location>
    fun beginSearchLocation(isLocationEnabled: Boolean)
    fun stopLocationUpdate()
}