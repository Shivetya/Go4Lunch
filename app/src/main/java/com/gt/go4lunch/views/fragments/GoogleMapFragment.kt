package com.gt.go4lunch.views.fragments

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gt.go4lunch.R
import com.gt.go4lunch.data.repositories.location.LocationRepoImpl

class GoogleMapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    companion object{

        fun newInstance(): GoogleMapFragment{
            return GoogleMapFragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_google_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_google_map_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        setObserve()

    }

    private fun setObserve(){
        LocationRepoImpl.instance.getLocationLiveData().observe(this, Observer {
            updateUI(it)
        })
    }

    private fun updateUI(userLoc: Location){
        val userLatLng = LatLng(userLoc.latitude, userLoc.longitude)

        map.addMarker(MarkerOptions().position(userLatLng).title("User Location"))

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15F))

    }

}