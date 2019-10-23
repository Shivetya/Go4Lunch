package com.gt.go4lunch.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gt.go4lunch.viewmodels.UserViewModel
import com.gt.go4lunch.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_settings.*

abstract class UserActivity : AppCompatActivity() {

    companion object{
        const val DELETE_USER_TASK = 10
        const val UPDATE_USER_NAME = 20
        const val REQUEST_LOCATION = 30
    }

    abstract fun updateUI()

    private lateinit var userViewModel: UserViewModel

    protected val observerUserTaskSucceed = androidx.lifecycle.Observer<Int> {
        when(it){
            DELETE_USER_TASK -> {
                startMainActivity()
                finish()
            }
            UPDATE_USER_NAME -> {
                updateUI()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        super.onCreate(savedInstanceState, persistentState)

        userViewModel = ViewModelProviders.of(this, ViewModelFactory.INSTANCE).get(UserViewModel::class.java)

        if(!isUserLogged()){
            startMainActivity()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        if(!isUserLogged()){
            startMainActivity()
            finish()
        }
    }

    protected fun getCurrentUser(): FirebaseUser?{
        return FirebaseAuth.getInstance().currentUser.also {
            if (it == null){
                startMainActivity()
                finish()
            }
        }
    }

    protected fun isUserLogged(): Boolean{
        return this.getCurrentUser() != null
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}