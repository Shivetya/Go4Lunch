package com.gt.go4lunch.views.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.gt.go4lunch.R
import com.gt.go4lunch.data.repositories.location.LocationRepoImpl
import com.gt.go4lunch.models.Restaurant
import com.gt.go4lunch.viewmodels.ListRestaurantsViewModel
import com.gt.go4lunch.viewmodels.ViewModelFactory
import com.gt.go4lunch.views.adapters.PlacesSearchApiResponseAdapter

class ListRestaurantsFragment : Fragment() {

    companion object{
        fun newInstance(): ListRestaurantsFragment{
            return ListRestaurantsFragment()
        }
    }

    private lateinit var viewModel: ListRestaurantsViewModel
    lateinit var adapter: PlacesSearchApiResponseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_list_restaurants, container, false)

        viewModel = ViewModelProviders.of(this, ViewModelFactory.INSTANCE).get(ListRestaurantsViewModel::class.java)
        val fragmentRV = view.findViewById<View>(R.id.fragment_list_restaurants_recycler_view) as RecyclerView
        configureRecyclerView(fragmentRV)

        setObserveAndLaunchSearch()

        return view
    }

    private fun setObserveAndLaunchSearch(){
        LocationRepoImpl.instance.getLocationLiveData().observe(this, Observer {
            viewModel.getListRestaurant(it)
        })
        viewModel.listRestaurants.observe(this, Observer {
            updateUI(it)
        })
    }

    private fun updateUI(listRestaurants: List<Restaurant>){
        adapter.setDataRestaurants(listRestaurants)
    }

    private fun configureRecyclerView(recyclerView : RecyclerView){

        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        adapter = PlacesSearchApiResponseAdapter(Glide.with(this))
        recyclerView.adapter = adapter

    }

}
