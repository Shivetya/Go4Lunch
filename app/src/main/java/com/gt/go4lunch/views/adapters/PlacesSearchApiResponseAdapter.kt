package com.gt.go4lunch.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.gt.go4lunch.R
import com.gt.go4lunch.models.Restaurant
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class PlacesSearchApiResponseAdapter(private val listRestaurant: List<Restaurant>,
                                     private val glide: RequestManager) : RecyclerView.Adapter<PlacesSearchApiResponseAdapter.PlacesSearchApiResponseViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlacesSearchApiResponseViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_view_item, parent, false)

        return PlacesSearchApiResponseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listRestaurant.size
    }

    override fun onBindViewHolder(holder: PlacesSearchApiResponseViewHolder, position: Int) {
        holder.updateWithResponse(listRestaurant, position, glide)
    }


    class PlacesSearchApiResponseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val textViewName: TextView = itemView.recycler_view_item_name
        private val textViewAddress: TextView = itemView.recycler_view_item_address
        private val textViewDistance: TextView = itemView.recycler_view_item_distance
        private val textViewTimeTable: TextView = itemView.recycler_view_item_timetable
        private val imageViewRestaurant: ImageView = itemView.recycler_view_item_image
        private val textViewVotes: TextView = itemView.recycler_view_item_votes
        private val listImageViewStars: List<ImageView> = listOf(itemView.recycler_view_item_first_star,
            itemView.recycler_view_item_second_star,
            itemView.recycler_view_item_third_star)


        fun updateWithResponse(listRestaurants: List<Restaurant>, position: Int, glide: RequestManager){

            textViewName.text = listRestaurants[position].name
            textViewAddress.text = listRestaurants[position].address
            textViewDistance.text = listRestaurants[position].distance
            glide.load(listRestaurants[position].urlPicture).into(imageViewRestaurant)

        }

    }
}
