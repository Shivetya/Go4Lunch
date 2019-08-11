package com.gt.go4lunch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        private const val ID_ACTIVITY_SIGN_IN = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_button_login_mail.setOnClickListener {
            this.startLoginByMailActivity()
        }
    }

    private fun startLoginByMailActivity(){

        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setTheme(R.style.LoginTheme)
            .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
            .setIsSmartLockEnabled(false, true)
            .setLogo(R.drawable.ic_launcher)
            .build()
        , ID_ACTIVITY_SIGN_IN)
    }
}
