package com.gt.go4lunch.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gt.go4lunch.data.repositories.location.LocationRepo
import com.gt.go4lunch.usecases.GoogleListRestaurantsUseCase
import com.gt.go4lunch.usecases.UsersFirestoreUseCase

class LoggedViewModel(private val usersFirestoreUseCase: UsersFirestoreUseCase,
                      private val locationRepo: LocationRepo,
                      private val googleListRestaurantsUseCase: GoogleListRestaurantsUseCase): ViewModel() {

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

    fun startLocationUpdate(locationEnabled: Boolean){
        locationRepo.beginSearchLocation(locationEnabled)
    }

    fun launchSearchNearbyRestaurant(userLocation: Location){

        googleListRestaurantsUseCase.getListRestaurant(userLocation)

    }

    fun stopLocationUpdate(){
        locationRepo.stopLocationUpdate()
    }
}