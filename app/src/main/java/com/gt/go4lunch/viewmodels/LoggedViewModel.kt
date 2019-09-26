package com.gt.go4lunch.viewmodels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gt.go4lunch.data.repositories.location.LocationRepo
import com.gt.go4lunch.usecases.UsersFirestoreUseCase

class LoggedViewModel(private val usersFirestoreUseCase: UsersFirestoreUseCase,
                      private val locationRepo: LocationRepo,
                      private val application: Application): ViewModel() {

    private val _locationEnabledLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val locationEnabledLiveData: LiveData<Boolean> = _locationEnabledLiveData

    fun createUserInFirestoreIfDoesntExist(){

        val currentUser = FirebaseAuth.getInstance().currentUser

        val username = currentUser?.displayName
        val userID = currentUser?.uid!!
        val userPicture = if (currentUser.photoUrl != null){
            currentUser.photoUrl.toString()
        } else {
            null
        }

        usersFirestoreUseCase.setUserIfDoesntExist(userID, username, userPicture)

    }

    fun startLocationUpdate(){
        locationRepo.beginSearchLocation()
    }

    fun stopLocationUpdate(){
        locationRepo.stopLocationUpdate()
    }

    fun checkLocationEnabled(){

        val permissionAccessCoarseLocationApproved = ActivityCompat
            .checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        val permissionAccessFineLocationApproved = ActivityCompat
            .checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        val isEnabled = !(!permissionAccessCoarseLocationApproved || !permissionAccessFineLocationApproved)

        _locationEnabledLiveData.value = isEnabled
    }
}