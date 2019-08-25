package com.gt.go4lunch.usecases


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.gt.go4lunch.models.User
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UsersFirestoreUseCase {

    companion object {
        private const val COLLECTION_NAME = "users"
    }

    private fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    fun setUserWithMerge(
        userID: String,
        userName: String?,
        urlProfilePicture: String?
    ): Task<Void> {

        val userToSet = User(userID, userName, urlProfilePicture)

        return getUsersCollection().document(userID).set(userToSet, SetOptions.mergeFields("urlProfilePicture"))
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