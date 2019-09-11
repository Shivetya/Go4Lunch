package com.gt.go4lunch.viewmodels

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gt.go4lunch.models.Restaurant
import com.gt.go4lunch.usecases.UsersFirestoreUseCase

class LoggedViewModel(private val usersFirestoreUseCase: UsersFirestoreUseCase): ViewModel() {

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

    fun launchSearchNearbyRestaurant(userLocation: Location){

    }
}