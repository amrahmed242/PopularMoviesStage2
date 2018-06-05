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

import com.nanodegree.android.popularmovies.Adapters.ReviewAdapter;
import com.nanodegree.android.popularmovies.R;

import java.util.ArrayList;


public class ReviewFragment extends Fragment {

    //region @Overrides

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_review, container, false);


        ArrayList<String> movieReviews_authors = getArguments().getStringArrayList("Authors");
        ArrayList<String> movieReviews_content = getArguments().getStringArrayList("Content");

        if(movieReviews_authors !=null && movieReviews_content !=null) {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            view.setLayoutManager(linearLayoutManager);

            ReviewAdapter reviewAdapter = new ReviewAdapter(movieReviews_authors, movieReviews_content);
            view.setAdapter(reviewAdapter);

        }
        return view;
    }

    //endregion

}
