package com.gt.go4lunch.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

abstract class UserActivity : AppCompatActivity() {

    companion object{
        const val DELETE_USER_TASK = 10
        const val LOGOUT_USER_TASK = 20
    }

    protected fun getCurrentUser(): FirebaseUser?{
        return FirebaseAuth.getInstance().currentUser
    }

    protected fun isUserLogged(): Boolean{
        return this.getCurrentUser() != null
    }

    protected fun updateUIAfterRequestsCompleted(task: Int): OnSuccessListener<Void> {
        return OnSuccessListener {
            when (task){
                DELETE_USER_TASK -> {
                    startMainActivity()
                    finish()
                }
                LOGOUT_USER_TASK -> {
                    startMainActivity()
                    finish()
                }
            }
        }
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}