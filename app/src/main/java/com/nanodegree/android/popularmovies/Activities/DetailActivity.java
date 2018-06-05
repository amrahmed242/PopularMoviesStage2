package com.nanodegree.android.popularmovies.Activities;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.nanodegree.android.popularmovies.DB.DbContract;
import com.nanodegree.android.popularmovies.Fragments.ReviewFragment;
import com.nanodegree.android.popularmovies.Fragments.TrailerFragment;
import com.nanodegree.android.popularmovies.Modules.MovieItem;
import com.nanodegree.android.popularmovies.R;
import com.nanodegree.android.popularmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;
import com.nanodegree.android.popularmovies.Fragments.OverViewFragment;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    //region variables
    private ActivityDetailBinding Binding;
    private static ArrayList<String> Movie_trailers;
    private static ArrayList<String> Movie_reviews_author;
    private static ArrayList<String> Movie_reviews_content;
    private Boolean fadein =false;
    private final Float[] animon={1f,200f,350f};
    private final Float[] animoff={0f,-120f,-350f};
    private final long animduration=900;
    private MovieItem movieItem;
    private String movie_id,movie_title,movie_picture,movie_rating,movie_overview,movie_release_date;
    //endregion

    //region @Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //receive the extras from the intent and fill the UI elements
        fillUI();


        //region ClickListners
        Binding.DetailIMDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Overview",Toast.LENGTH_SHORT).show();

                initOverView();

            }
        });

        Binding.DetailIMDB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(!fadein){
                    fadein = true;
                    animateClick(animon[0], animon[1], animon[2], animduration, true);
                }
                else{
                    fadein=false;
                    animateClick(animoff[0],animoff[1],animoff[2],animduration,false);
                }
                return true;
            }
        });

        Binding.DetailTrailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Trailers",Toast.LENGTH_SHORT).show();
                initTrailers();

            }
        });

        Binding.DetailReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Reviews",Toast.LENGTH_SHORT).show();

                initReviews();
            }
        });


        Binding.DetailFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkMovie(movie_id)){

                    if(deleteMovie(movie_id)){

                        Toast.makeText(getApplicationContext(),"removed from favorites",Toast.LENGTH_SHORT).show();
                        Binding.DetailFavorite.setAlpha(.5f);

                    }

                }else {

                     addMovie(movieItem);
                     Toast.makeText(getApplicationContext(),"added to favorites",Toast.LENGTH_SHORT).show();
                     Binding.DetailFavorite.setAlpha(1f);

                }


            }
        });

        //endregion



    }

    @Override
    protected void onResume() {
        super.onResume();

            fadein = true;
            animateClick(animon[0], animon[1], animon[2], animduration, true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Movie_trailers=null;
        Movie_reviews_author=null;
        Movie_reviews_content=null;
    }

    //endregion

    //region functions

    private Boolean checkMovie(String movie_id) {

        return DbContract.CheckFav(movie_id, this);

    }

    private void addMovie(MovieItem movieItem) {

        DbContract.addNewFav(this, movieItem);

    }

    private Boolean deleteMovie(String Movie_id) {

        return DbContract.removeFav(this,Movie_id);

    }

    //get the activity needed data and setup views
    private void fillUI() {

        Binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            movie_id=intent.getStringExtra("movie_id");
            movie_title = intent.getStringExtra("movie_title");
            movie_picture = intent.getStringExtra("movie_picture");
            movie_rating = intent.getStringExtra("movie_rating");
            movie_overview = intent.getStringExtra("movie_overview");
            movie_release_date = intent.getStringExtra("movie_release_date");

            movieItem=new MovieItem(movie_id,movie_title,movie_picture,movie_rating,movie_overview,movie_release_date);

            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(Binding.DetailTitle,
                    15,35,1,TypedValue.COMPLEX_UNIT_SP);
        }

        if(checkMovie(movie_id)){
            Binding.DetailFavorite.setAlpha(1f);

        }

        //load movie poster & data
        try {
            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w780/" + movie_picture)
                    .error(R.drawable.error)
                    .into(Binding.DetailPoster);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Binding.DetailTitle.setText(movie_title);
        int i = (int) Float.parseFloat(movie_rating);
        Binding.DetailRating.setText(String.valueOf(i));

        //initialize OverView fragment
        initOverView();


    }

    //animate button clicks
    private void animateClick(float alph,float transl,float rotate,long dur,Boolean clck){
        Binding.DetailTrailers.animate().alpha(alph).setDuration(dur);
        Binding.DetailReviews.animate().alpha(alph).setDuration(dur);
        Binding.DetailTrailers.animate().translationX(transl);
        Binding.DetailReviews.animate().translationX(-transl);
        Binding.DetailTrailers.animate().rotation(rotate).setDuration(dur);
        Binding.DetailReviews.animate().rotation(-rotate).setDuration(dur);
        Binding.DetailTrailers.setClickable(clck);
        Binding.DetailReviews.setClickable(clck);
    }


    //call the overview fragment
    private void initOverView(){

        Bundle bundle=new Bundle();
        bundle.putString("OverView",movie_overview);
        bundle.putString("ReleaseDate",movie_release_date);
        OverViewFragment overViewFragment=new OverViewFragment();
        overViewFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.Detail_Frag,overViewFragment,"overViewFrag").commit();

    }

    //call the trailers fragment
    private void initTrailers(){

        Bundle bundle=new Bundle();
        bundle.putStringArrayList("Trailers",Movie_trailers);
        TrailerFragment trailerFragment=new TrailerFragment();
        trailerFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.Detail_Frag,trailerFragment,"trailerFragment").commit();


    }

    //call the overview fragment
    private void initReviews(){

        Bundle bundle=new Bundle();
        bundle.putStringArrayList("Authors",Movie_reviews_author);
        bundle.putStringArrayList("Content",Movie_reviews_content);
        ReviewFragment reviewFragment=new ReviewFragment();
        reviewFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.Detail_Frag,reviewFragment,"reviewFragment").commit();



    }

    //setter
    public static void setMovie_trailers(ArrayList<String> movie_trailers) {
        Movie_trailers = movie_trailers;
    }

    //setter
    public static void setMovie_reviews_author(ArrayList<String> movie_reviews_author) {
        Movie_reviews_author = movie_reviews_author;
    }

    //setter
    public static void setMovie_reviews_content(ArrayList<String> movie_reviews_content) {
        Movie_reviews_content = movie_reviews_content;
    }

    //endregion


}
