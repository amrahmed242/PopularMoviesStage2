package com.nanodegree.android.popularmovies.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanodegree.android.popularmovies.Adapters.TrailerAdapter;
import com.nanodegree.android.popularmovies.R;
import java.util.ArrayList;



public class TrailerFragment extends Fragment {

    //region @Overrides


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_trailer, container, false);

        ArrayList<String> videoIds = getArguments().getStringArrayList("Trailers");

        if(videoIds != null) {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            view.setLayoutManager(linearLayoutManager);

            final TrailerAdapter trailerAdapter = new TrailerAdapter(videoIds, getActivity());
            view.setAdapter(trailerAdapter);

        }


        return view;
    }

    //endregion

}
