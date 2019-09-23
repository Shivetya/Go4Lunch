package com.gt.go4lunch.viewmodels

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gt.go4lunch.data.repositories.location.LocationRepo
import com.gt.go4lunch.usecases.UsersFirestoreUseCase

class LoggedViewModel(private val usersFirestoreUseCase: UsersFirestoreUseCase,
                      private val locationRepo: LocationRepo): ViewModel() {

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

    fun checkLocationEnabled(activity: Activity): Boolean{

        val permissionAccessCoarseLocationApproved = ActivityCompat
            .checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        val permissionAccessFineLocationApproved = ActivityCompat
            .checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        return !(!permissionAccessCoarseLocationApproved || !permissionAccessFineLocationApproved)
    }
}