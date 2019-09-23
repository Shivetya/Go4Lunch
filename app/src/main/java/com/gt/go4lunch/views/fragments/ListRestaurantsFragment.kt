package com.gt.go4lunch.views.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.gt.go4lunch.R
import com.gt.go4lunch.models.Restaurant
import com.gt.go4lunch.viewmodels.ListRestaurantsViewModel
import com.gt.go4lunch.viewmodels.ViewModelFactory

class ListRestaurantsFragment : Fragment() {

    companion object{
        fun newInstance(): ListRestaurantsFragment{
            return ListRestaurantsFragment()
        }
    }

    private lateinit var viewModel: ListRestaurantsViewModel
    //private lateinit var restaurants: MutableCollection<Restaurant>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_list_restaurants, container, false)

        viewModel = ViewModelProviders.of(this, ViewModelFactory.INSTANCE).get(ListRestaurantsViewModel::class.java)

        return view
    }

    /*private fun updateUI(restaurants: Collection<Restaurant>){

    }*/


}
