package com.gt.go4lunch.activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.firebase.ui.auth.AuthUI
import com.gt.go4lunch.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : UserActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        configureToolbar()

        activity_settings_delete_account_button.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.message_confirm_delete_account))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    deleteUserFromFirebase()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
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
    }

}
