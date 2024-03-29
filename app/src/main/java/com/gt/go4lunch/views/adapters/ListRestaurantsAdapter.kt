package com.gt.go4lunch.views.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.gt.go4lunch.R
import com.gt.go4lunch.data.repositories.places.GooglePhotoRepo
import com.gt.go4lunch.models.Restaurant
import kotlinx.android.synthetic.main.list_restaurant_recycler_view_item.view.*

class ListRestaurantsAdapter(private val glide: RequestManager) : RecyclerView.Adapter<ListRestaurantsAdapter.PlacesSearchApiResponseViewHolder>(){

    private var listRestaurant: List<Restaurant>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlacesSearchApiResponseViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_restaurant_recycler_view_item, parent, false)

        return PlacesSearchApiResponseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listRestaurant?.size ?: 0
    }

    override fun onBindViewHolder(holder: PlacesSearchApiResponseViewHolder, position: Int) {
        holder.updateWithResponse(listRestaurant!!, position, glide)
    }

    fun setDataRestaurants(restaurants: List<Restaurant>){
        listRestaurant = restaurants
        notifyDataSetChanged()
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
            textViewTimeTable.text = listRestaurants[position].isOpen
            GooglePhotoRepo.getInstance().getBitmapImage(listRestaurants[position].photoId, object : GooglePhotoRepo.Callback{
                override fun onRequestFailed() {

                }

                override fun onImageLoaded(image: Bitmap?) {
                    glide.load(image).into(imageViewRestaurant)
                }

            })
            for (index in listImageViewStars.indices){
                if (index < listRestaurants[position].rating){
                    listImageViewStars[index].visibility = View.VISIBLE
                }else {
                    listImageViewStars[index].visibility = View.GONE
                }
            }



        }

    }
}
