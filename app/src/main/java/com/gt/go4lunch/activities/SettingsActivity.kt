package com.gt.go4lunch.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.gt.go4lunch.R
import com.gt.go4lunch.viewmodels.SettingsViewModel
import com.gt.go4lunch.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : UserActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsViewModel = ViewModelProviders.of(this, ViewModelFactory.INSTANCE).get(SettingsViewModel::class.java)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        configureToolbar()
        setUsernameInTextView()

        configureDeleteAccountButton()
        configureUpdateUsernameButtonAndEditText()

    }

    private fun configureDeleteAccountButton(){

        activity_settings_delete_account_button.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.message_confirm_delete_account))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    deleteUserFromFirebase()
                    deleteUserFromFirestore()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }
    }

    private fun configureUpdateUsernameButtonAndEditText(){

        activity_settings_button_update.setOnClickListener {

            val newUserName: String? = activity_settings_edittext_new_username.text.toString()

            updateUsernameInFirestore(newUserName)
        }
    }

    private fun deleteUserFromFirebase(){
        if(this.getCurrentUser() != null){
            AuthUI.getInstance()
                .delete(this)
                .addOnFailureListener {
                    Log.w(javaClass.simpleName, "Could not delete user in Firebase. $it")
                }
        }
    }

    private fun configureToolbar(){
        val toolbar: Toolbar = findViewById(R.id.activity_toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDefaultDisplayHomeAsUpEnabled(true)
    }

    private fun deleteUserFromFirestore(){

        settingsViewModel.deleteUserInFirestore()
    }

    private fun updateUsernameInFirestore(newUsername: String?){

        activity_settings_progress_bar.visibility = View.VISIBLE

        settingsViewModel.updateUsernameInFirestore(newUsername)

        settingsViewModel.isOperationSucceed.observe(this, observerUserTaskSucceed.also {
            setUsernameInTextView(newUsername)
        })



    }

    fun setUsernameInTextView(newUsername: String? = null){

        if (newUsername == null){
            settingsViewModel.usernameToSet.observe(this, Observer {
                activity_settings_username_textview.text = it
            })
            settingsViewModel.getUsernameForTextView()
        } else {
            activity_settings_username_textview.text = newUsername
        }

    }

}
