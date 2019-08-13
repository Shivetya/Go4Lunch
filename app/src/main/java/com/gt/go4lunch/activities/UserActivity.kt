package com.gt.go4lunch.activities

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

abstract class UserActivity : AppCompatActivity() {

    protected fun getCurrentUser(): FirebaseUser?{
        return FirebaseAuth.getInstance().currentUser
    }

    protected fun isUserLogged(): Boolean{
        return this.getCurrentUser() != null
    }
}