package com.gt.go4lunch.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.gt.go4lunch.R;
import com.gt.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;

public class ListWorkMatesAdapter extends RecyclerView.Adapter<ListWorkMatesAdapter.ListWorkMatesViewHolder> {

    private RequestManager mGlide;
    private List<User> mListWorkMates = new ArrayList<>();

    public ListWorkMatesAdapter(RequestManager glide){
        mGlide = glide;
    }

    @NonNull
    @Override
    public ListWorkMatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_work_mates_recycler_view_item, parent, false);

        return new ListWorkMatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListWorkMatesViewHolder holder, int position) {
        holder.updateWithResponse(mListWorkMates, position, mGlide);
    }

    @Override
    public int getItemCount() {
        return mListWorkMates.size();
    }

    public void setDataChanged(List<User> listUsers){
        mListWorkMates = listUsers;
        notifyDataSetChanged();
    }

    class ListWorkMatesViewHolder extends RecyclerView.ViewHolder{

        ImageView mProfilePicture;
        TextView mTextView;

        ListWorkMatesViewHolder(@NonNull View itemView) {

            super(itemView);

            mProfilePicture = itemView.findViewById(R.id.recycler_work_mates_profile_picture);
            mTextView = itemView.findViewById(R.id.recycler_work_mates_text_view);

        }

        void updateWithResponse(List<User> listUser, int position, RequestManager glide){

            glide.load(listUser.get(position).getUrlProfilePicture()).into(mProfilePicture);
            mTextView.setText(listUser.get(position).getStringChoiceRestaurant());
        }


    }
}
