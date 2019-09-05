package com.gt.go4lunch.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gt.go4lunch.views.activities.UserActivity
import com.gt.go4lunch.models.User
import com.gt.go4lunch.usecases.UsersFirestoreUseCase

class SettingsViewModel(private val usersFirestoreUseCase: UsersFirestoreUseCase): ViewModel() {

    private val _isOperationSucceed: MutableLiveData<Int> = MutableLiveData()
    val isOperationSucceed: LiveData<Int> = _isOperationSucceed

    private val _usernameToSet: MutableLiveData<String> = MutableLiveData()
    val usernameToSet: LiveData<String> = _usernameToSet

    fun updateUsernameInFirestore(newUserName: String?){

        val userID: String? = FirebaseAuth.getInstance().currentUser?.uid

        if (userID != null && !newUserName.isNullOrEmpty()) {
            usersFirestoreUseCase
                .updateUserName(userID, newUserName)
                .addOnSuccessListener{
                    _isOperationSucceed.postValue(UserActivity.UPDATE_USER_NAME)
                }.addOnFailureListener {
                    Log.w(javaClass.simpleName, "Could not change username.")
                }
        }
    }

    fun deleteUserInFirestore(){

        val userID = FirebaseAuth.getInstance().currentUser?.uid

        if (userID != null){
            usersFirestoreUseCase.deleteUser(userID).addOnSuccessListener {
                _isOperationSucceed.postValue(UserActivity.DELETE_USER_TASK)
            }.addOnFailureListener{
                Log.w(javaClass.simpleName, "Could not delete user in Firestore")
            }
        }

    }

    fun getUsernameForTextView(){

        val userID = FirebaseAuth.getInstance().currentUser?.uid

        if (userID != null){
            usersFirestoreUseCase.getUser(userID).addOnSuccessListener {
                val currentUser: User? = it.toObject(User::class.java)
                _usernameToSet.postValue(currentUser?.userName)
            }.addOnFailureListener {
                Log.w(javaClass.simpleName, "Could not reach database.")
            }
        }
    }
}