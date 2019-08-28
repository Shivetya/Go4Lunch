package com.gt.go4lunch.usecases


import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.gt.go4lunch.models.User

class UsersFirestoreUseCase {

    companion object {
        private const val COLLECTION_NAME = "users"
    }

    private fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    fun setUserIfDoesntExist(
        userID: String,
        userName: String?,
        urlProfilePicture: String?
    ): Task<DocumentSnapshot>? {

        val userToSet = User(userID, userName, urlProfilePicture)

        return getUser(userID).addOnSuccessListener {
            if (!it.exists() || it == null) {
                getUsersCollection().document(userID).set(userToSet)
            }
        }.addOnFailureListener {
            Log.w(javaClass.simpleName, "Couldn't reach Database.")
        }
    }

    fun getUser(userID: String): Task<DocumentSnapshot> {
        return getUsersCollection().document(userID).get()
    }

    fun updateUserName(userID: String, userName: String): Task<Void> {
        return getUsersCollection().document(userID).update("userName", userName)
    }

    fun deleteUser(userID: String): Task<Void> {
        return getUsersCollection().document(userID).delete()
    }

}