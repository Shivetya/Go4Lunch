package com.gt.go4lunch.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.android.gms.tasks.OnFailureListener
import com.gt.go4lunch.R
import com.gt.go4lunch.models.User
import com.gt.go4lunch.usecases.UsersFirestoreUseCase
import kotlinx.android.synthetic.main.activity_settings.*

abstract class UserActivity : AppCompatActivity() {

    companion object{
        const val DELETE_USER_TASK = 10
        const val LOGOUT_USER_TASK = 20
        const val UPDATE_USER_NAME = 30
    }

    private val usersFirestoreUseCase: UsersFirestoreUseCase = UsersFirestoreUseCase()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        super.onCreate(savedInstanceState, persistentState)

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
                UPDATE_USER_NAME -> {
                    setUsernameInTextView()
                    activity_settings_progress_bar.visibility = View.GONE
                }
            }
        }
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    protected fun onFailureListener(): OnFailureListener {
        return OnFailureListener {
            Toast.makeText(
                applicationContext,
                getString(R.string.unknow_error),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    protected fun setUsernameInTextView(){

        val userID = getCurrentUser()?.uid

        if (userID != null){
            usersFirestoreUseCase.getUser(userID).addOnSuccessListener {
                val currentUser: User? = it.toObject(User::class.java)

                val userName = currentUser?.userName ?: getString(R.string.info_no_username_found)

                activity_settings_username_textview.text = userName
            }
        }

    }
}