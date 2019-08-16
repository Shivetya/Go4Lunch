package com.gt.go4lunch.usecases


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.gt.go4lunch.models.User

class UsersFirestoreUseCase {

    companion object {
        private const val COLLECTION_NAME = "users"

        fun getUsersCollection(): CollectionReference{
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
        }

        fun createUser(userMail: String, userName: String, urlProfilePicture: String): Task<Void> {
            val userToCreate = User(userMail, userName, urlProfilePicture)

            return getUsersCollection().document(userMail).set(userToCreate)
        }

        fun getUser(userMail: String): Task<DocumentSnapshot>{
            return getUsersCollection().document(userMail).get()
        }

        fun updateUserName(userMail: String, userName: String): Task<Void>{
            return getUsersCollection().document(userMail).update("username", userName)
        }

        fun deleteUser(userMail: String): Task<Void>{
            return getUsersCollection().document(userMail).delete()
        }
    }



}