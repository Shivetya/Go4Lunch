package com.gt.go4lunch.views.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.gt.go4lunch.R
import com.gt.go4lunch.data.repositories.location.LocationRepoImpl
import com.gt.go4lunch.viewmodels.LoggedViewModel
import com.gt.go4lunch.viewmodels.ViewModelFactory
import com.gt.go4lunch.views.fragments.GoogleMapFragment
import kotlinx.android.synthetic.main.activity_logged.*
import kotlinx.android.synthetic.main.nav_header_logged.view.*
import kotlinx.android.synthetic.main.toolbar.*
import net.danlew.android.joda.JodaTimeAndroid

class LoggedActivity : UserActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var loggedViewModel : LoggedViewModel
    private lateinit var userLoc: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        loggedViewModel = ViewModelProviders.of(this, ViewModelFactory.INSTANCE).get(LoggedViewModel::class.java)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_logged)

        JodaTimeAndroid.init(this)
        configureToolbarAndDrawer()

        updateUIWithUsersInfo()

        loggedViewModel.createUserInFirestoreIfDoesntExist()

        setObserveLocation()

        val locationEnabled = checkLocationAccessGranted()

        loggedViewModel.startLocationUpdate(locationEnabled)


        launchGoogleMapFragment()

    }

    override fun onResume() {
        super.onResume()

        if (!isUserLogged()) {
            startMainActivity()
        }
    }

    override fun onStop() {
        super.onStop()

        FirebaseAuth.getInstance().signOut()
    }

    override fun onPause() {
        super.onPause()
        loggedViewModel.stopLocationUpdate()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.logged, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.nav_drawer_settings -> {
                startSettingsActivity()
            }
            R.id.nav_drawer_logout -> {
                logoutUser()
            }
            R.id.menu_bottom_nav_map -> {
                launchGoogleMapFragment()
            }
            R.id.menu_bottom_nav_list -> {

            }
            R.id.menu_bottom_nav_workmates -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun configureToolbarAndDrawer(){
        val toolbar: Toolbar = findViewById(R.id.activity_toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.activity_logged_nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        activity_toolbar.title = getString(R.string.i_am_hungry)
    }

    private fun updateUIWithUsersInfo(){

        if(this.isUserLogged()) {

            val navHeaderView = activity_logged_nav_view.getHeaderView(0)

            val user = this.getCurrentUser()

            if(user?.photoUrl != null){
                Glide.with(this)
                    .load(user.photoUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(navHeaderView.nav_header_imageView_user_image)
            }

            val emailUser = user?.email ?: getString(R.string.info_no_email_found)
            val userName = user?.displayName ?: getString(R.string.info_no_username_found)

            navHeaderView.nav_header_text_user_email.text = emailUser
            navHeaderView.nav_header_text_user_name.text = userName
        }
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun startSettingsActivity(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun logoutUser(){
        AuthUI.getInstance()
            .signOut(this)
            .addOnSuccessListener{
                startMainActivity()
                finish()
            }
    }

    private fun launchGoogleMapFragment(){

        val googleMapFragment = GoogleMapFragment.newInstance()

        supportFragmentManager.beginTransaction().replace(R.id.activity_logged_frame_layout, googleMapFragment).commit()
    }

    private fun checkLocationAccessGranted(): Boolean{

        val permissionAccessCoarseLocationApproved = ActivityCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        val permissionAccessFineLocationApproved = ActivityCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (!permissionAccessCoarseLocationApproved || !permissionAccessFineLocationApproved){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_COARSE_LOCATION)

            checkLocationAccessGranted()
        } else {
            return true
        }

        return true
    }

    private fun setObserveLocation(){
        LocationRepoImpl.instance.getLocationLiveData().observe(this, Observer {
            loggedViewModel.launchSearchNearbyRestaurant(it)
        })
    }

}
