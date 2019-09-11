package com.gt.go4lunch.views.fragments

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class GoogleMapFragment: SupportMapFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    companion object{

        fun newInstance(): GoogleMapFragment{
            return GoogleMapFragment()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


    }

    private fun setObserve(){

    }

}