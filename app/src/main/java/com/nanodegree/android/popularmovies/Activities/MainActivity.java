package com.nanodegree.android.popularmovies.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.nanodegree.android.popularmovies.Adapters.MovieAdapter;
import com.nanodegree.android.popularmovies.DB.DbContract;
import com.nanodegree.android.popularmovies.Modules.MovieItem;
import com.nanodegree.android.popularmovies.R;
import com.nanodegree.android.popularmovies.Utilities.Api;
import com.nanodegree.android.popularmovies.databinding.ActivityMainBinding;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    // region Variables
    private String KEY;
    private int SNACKBAR_STATE = 0;
    private final int SNACKBAR_VALUE_SHOWN = 1;
    private static List<MovieItem> Movies;
    private Parcelable RecyclerView_state;
    private ActivityMainBinding Binding;
    private MovieAdapter movieAdapter = null;



    public static void setMovies(List<MovieItem> movies) {
        Movies = movies;
    }

    //endregion


    //region @Overrides

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        //getting defaults
        checkFavSort();


        //onclick listener for the fab button to access setting popup
        Binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow();
            }
        });

        //setting and change listener for the Preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        KEY = getResources().getString(R.string.SORTING_ORDER_KEY);

    }


    //overriding {onSaveInstanceState-onRestoreInstanceState} to save the state of the recycler view and SnackBar
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        RecyclerView_state = Binding.Recycler.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(getResources().getString(R.string.RECYCLER_STATE_KEY), RecyclerView_state);
        outState.putInt(getResources().getString(R.string.SNACKBAR_STATE_KEY), SNACKBAR_VALUE_SHOWN);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            RecyclerView_state = savedInstanceState.getParcelable(getResources().getString(R.string.RECYCLER_STATE_KEY));
            SNACKBAR_STATE = savedInstanceState.getInt(getResources().getString(R.string.SNACKBAR_STATE_KEY), SNACKBAR_VALUE_SHOWN);
        }
    }

    //overriding onSharedPreferenceChanged to detect Preference changes in the main run time
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(KEY)) {

            String favSort = getResources().getString(R.string.SORTING_ORDER_FAVOURITES);
            String Default = getResources().getString(R.string.SORTING_ORDER_POPULAR);

            if (sharedPreferences.getString(KEY, Default).equals(favSort)) {

                //call DB to get the fav movies query list
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFavorites();
                    }
                }, 1500);

            } else {

                /* creating an object from API classto call the api request again */

                Api api = new Api(this);
                api.new MovieRequest(false, "category_search", null).execute();
            }
        }
    }

    //overriding onResume to restore the state of the RecyclerView
    @Override
    protected void onResume() {
        super.onResume();

        if (RecyclerView_state != null) {
            Binding.Recycler.getLayoutManager().onRestoreInstanceState(RecyclerView_state);
        }

        if (SNACKBAR_STATE != 1) {
            show_snack();
            SNACKBAR_STATE = 1;
        }
    }

    //overriding onDestroy to unRegister the SharedPreferences Listener
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    //endregion


    //region Functions

    //welcome message for the user
    private void show_snack() {


        //delaying snackBar calls for a smoother activity transition

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String Snack1_Text = getResources().getString(R.string.Snack1);
                Snackbar mSnackBar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), Snack1_Text, Snackbar.LENGTH_LONG);
                TextView mainTextView = (mSnackBar.getView()).findViewById(android.support.design.R.id.snackbar_text);

                mainTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mSnackBar.show();
            }
        }, 1500);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String Snack2_Text = getResources().getString(R.string.Snack2);
                Snackbar mSnackBar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), Snack2_Text, 6000);
                TextView mainTextView = (mSnackBar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                mainTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mSnackBar.show();
            }
        }, 5500);

    }

    //fill the recycler with the parsed movies data
    private void populate_recycler(Boolean fav) {

        movieAdapter = new MovieAdapter(Movies,this,fav);
        GridLayoutManager manager = new GridLayoutManager(this, calculateBestSpanCount(600));
        Binding.Recycler.setLayoutManager(manager);
        Binding.Recycler.setAdapter(movieAdapter);

    }

    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }


    //create the popup window
    private void popupWindow() {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        @SuppressLint("InflateParams") final View popupView = layoutInflater.inflate(R.layout.popup_window, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setIgnoreCheekPress();
        popupView.animate().alpha(.9f).setDuration(700);

        Button popular = popupView.findViewById(R.id.popular);
        Button topRated = popupView.findViewById(R.id.top_rated);
        Button favourites = popupView.findViewById(R.id.favourites);

        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                String Sorting_Value = getResources().getString(R.string.SORTING_ORDER_POPULAR);

                editor.putString(KEY, Sorting_Value);
                editor.apply();

                popupView.animate().alpha(0f).setDuration(700);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        populate_recycler(false);
                        popupWindow.dismiss();
                    }
                }, 1800);
            }
        });

        topRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                String Sorting_Value = getResources().getString(R.string.SORTING_ORDER_TOP_RATED);

                editor.putString(KEY, Sorting_Value);
                editor.apply();

                popupView.animate().alpha(0f).setDuration(700);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        populate_recycler(false);
                        popupWindow.dismiss();
                    }
                }, 1800);
            }
        });


        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                popupView.animate().alpha(0f).setDuration(700);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        String Sorting_Value = getResources().getString(R.string.SORTING_ORDER_FAVOURITES);

                        editor.putString(KEY, Sorting_Value);
                        editor.apply();

                        popupWindow.dismiss();

                    }
                }, 1000);
            }
        });


    }

    //check if splashscreen detected no connection
    private void checkFavSort() {
        Intent intent = getIntent();
        if (intent.hasExtra("Fav")) {
            getFavorites();
        } else {
            populate_recycler(false);
        }

    }

    //get the fav movies from DB
    private void getFavorites() {

        Movies = DbContract.GetFavsList(DbContract.QueryFavs(getApplicationContext()));
        populate_recycler(true);

    }

  //endregion

}