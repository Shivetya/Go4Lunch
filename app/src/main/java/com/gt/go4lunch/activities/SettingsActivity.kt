package com.gt.go4lunch.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.firebase.ui.auth.AuthUI
import com.gt.go4lunch.R
import com.gt.go4lunch.models.User
import com.gt.go4lunch.usecases.UsersFirestoreUseCase
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : UserActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usersFirestoreUseCase = UsersFirestoreUseCase()

        setContentView(R.layout.activity_settings)
        configureToolbar()
        setUsernameInTextView()

        configureDeleteAccountButton(usersFirestoreUseCase)
        configureUpdateUsernameButtonAndEditText(usersFirestoreUseCase)

    }

    private fun configureDeleteAccountButton(usersFirestoreUseCase: UsersFirestoreUseCase){

        activity_settings_delete_account_button.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.message_confirm_delete_account))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    deleteUserFromFirebase()
                    deleteUserFromFirestore(usersFirestoreUseCase)
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }
    }

    private fun configureUpdateUsernameButtonAndEditText(usersFirestoreUseCase: UsersFirestoreUseCase){

        activity_settings_button_update.setOnClickListener {

            val newUserName: String? = activity_settings_edittext_new_username.text.toString()

            if (!newUserName.isNullOrEmpty()){
                updateUsernameInFirestore(usersFirestoreUseCase, newUserName)
            }
        }
    }

    private fun deleteUserFromFirebase(){
        if(this.getCurrentUser() != null){
            AuthUI.getInstance()
                .delete(this)
                .addOnSuccessListener(this, this.updateUIAfterRequestsCompleted(DELETE_USER_TASK))
        }
    }

    private fun configureToolbar(){
        val toolbar: Toolbar = findViewById(R.id.activity_toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDefaultDisplayHomeAsUpEnabled(true)
    }

    private fun deleteUserFromFirestore(usersFirestoreUseCase: UsersFirestoreUseCase){

        val userID = getCurrentUser()?.uid

        if(userID != null) {
            usersFirestoreUseCase.deleteUser(userID).addOnFailureListener(onFailureListener())
        }
    }

    private fun updateUsernameInFirestore(usersFirestoreUseCase: UsersFirestoreUseCase, newUsername: String){

        activity_settings_progress_bar.visibility = View.VISIBLE

        val userID: String? = getCurrentUser()?.uid

        if (userID != null) {
            usersFirestoreUseCase
                .updateUserName(userID, newUsername)
                .addOnFailureListener(onFailureListener())
                .addOnSuccessListener(this.updateUIAfterRequestsCompleted(UPDATE_USER_NAME))
        }

    }

}
