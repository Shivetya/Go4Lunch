package com.gt.go4lunch.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.gt.go4lunch.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        private const val ID_ACTIVITY_SIGN_IN = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_button_login_mail.setOnClickListener {
            this.startLoginActivity()
        }
    }

    override fun onResume() {
        super.onResume()

        if (FirebaseAuth.getInstance().currentUser != null){
            startLoggedActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        handleWithActivityResult(requestCode, resultCode, data)
    }

    private fun startLoginActivity(){

        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setTheme(R.style.LoginTheme)
            .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()
                ,AuthUI.IdpConfig.GoogleBuilder().build()
                ,AuthUI.IdpConfig.FacebookBuilder().build()))
            .setIsSmartLockEnabled(false, true)
            .setLogo(R.drawable.ic_launcher)
            .build()
        , ID_ACTIVITY_SIGN_IN
        )
    }

    private fun handleWithActivityResult(requestCode: Int, resultCode: Int, data: Intent?){

        val response: IdpResponse? = IdpResponse.fromResultIntent(data)

        if (requestCode == ID_ACTIVITY_SIGN_IN){
            if (resultCode == RESULT_OK){
                startLoggedActivity()
            } else {
                when (response?.error?.errorCode){
                    null -> Snackbar.make(activity_main_root, "Authentication Canceled", Snackbar.LENGTH_SHORT).show()
                    ErrorCodes.NO_NETWORK -> Snackbar.make(activity_main_root, "No Internet !", Snackbar.LENGTH_SHORT).show()
                    ErrorCodes.UNKNOWN_ERROR -> Snackbar.make(activity_main_root, "Unknown Error !", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startLoggedActivity(){
        val intent = Intent(this, LoggedActivity::class.java)
        this.startActivity(intent)
    }
}
