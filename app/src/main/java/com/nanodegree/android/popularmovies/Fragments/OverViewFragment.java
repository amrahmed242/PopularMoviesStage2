package com.nanodegree.android.popularmovies.Fragments;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nanodegree.android.popularmovies.R;
import com.nanodegree.android.popularmovies.databinding.FragmentOverviewBinding;



public class OverViewFragment extends Fragment {

    //region variables
    private String ReleaseDate;
    private String OverView;
    //endregion


    //region @Overrides

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OverView=getArguments().getString("OverView");
        ReleaseDate=getArguments().getString("ReleaseDate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        FragmentOverviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_overview, container, false);
        View view = binding.getRoot();

        binding.DetailDescription.setText(OverView);
        String text=getString(R.string.ReleaseDate)+" "+ReleaseDate;
        binding.DetailReleaseDate.setText(text);

        return view;
    }
    //endregion

}
