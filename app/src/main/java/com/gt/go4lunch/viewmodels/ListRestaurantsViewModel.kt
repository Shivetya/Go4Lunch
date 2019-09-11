package com.gt.go4lunch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gt.go4lunch.models.Restaurant

class ListRestaurantsViewModel: ViewModel() {

    private val _listRestaurantLiveData: MutableLiveData<Collection<Restaurant>> = MutableLiveData()
    val listRestaurantLiveData: LiveData<Collection<Restaurant>> = _listRestaurantLiveData

}