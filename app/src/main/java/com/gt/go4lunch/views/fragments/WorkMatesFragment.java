package com.gt.go4lunch.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gt.go4lunch.R;
import com.gt.go4lunch.models.User;
import com.gt.go4lunch.viewmodels.ViewModelFactory;
import com.gt.go4lunch.viewmodels.WorkMatesViewModel;
import com.gt.go4lunch.views.adapters.ListWorkMatesAdapter;

import java.util.List;
import java.util.Objects;


public class WorkMatesFragment extends Fragment {

    public WorkMatesFragment() {
        // Required empty public constructor
    }

    public static WorkMatesFragment newInstance() {

        return new WorkMatesFragment();
    }

    private WorkMatesViewModel mViewModel;
    private ListWorkMatesAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_work_mates, container, false);

        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getINSTANCE()).get(WorkMatesViewModel.class);
        RecyclerView fragmentRV = v.findViewById(R.id.fragment_list_work_mates_recycler_view);

        configureRecyclerView(fragmentRV);

        setObserve();

        return v;
    }

    private void configureRecyclerView(RecyclerView fragmentRV){

        fragmentRV.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext()));
        mAdapter = new ListWorkMatesAdapter(Glide.with(this));
        fragmentRV.setAdapter(mAdapter);

    }

    private void setObserve(){
        mViewModel.mListUsersLiveData.observe(this, this::updateUI);
    }

    private void updateUI(List<User> listUsers){
        mAdapter.setDataChanged(listUsers);
    }

}
