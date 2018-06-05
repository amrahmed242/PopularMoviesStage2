package com.nanodegree.android.popularmovies.Utilities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.nanodegree.android.popularmovies.Activities.DetailActivity;
import com.nanodegree.android.popularmovies.Activities.MainActivity;
import com.nanodegree.android.popularmovies.R;

import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class Api {

    // region Variables

    private final Context mContext;

    //constructor
    public Api(Context context) {
        mContext = context;
    }

    //endregion

    // region functions

    //this function check the setting of the user
    private String setupSharedPreferences() {

        Resources resources=mContext.getResources();
        String Key=resources.getString(R.string.SORTING_ORDER_KEY);
        String Default=resources.getString(R.string.DEFAULT_SORTING_ORDER);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(Key,Default);

    }

    //this function create the URL for the query of the movies from the movie api
    private URL Generate_API_URL(String SORTING_ORDER,String Type,String MovieID){
        URL url=null;
        Uri builtUri;

        Resources resources=mContext.getResources();
        String API=resources.getString(R.string.API);
        String API_KEY=resources.getString(R.string.API_KEY);
        String SORTING_ORDER_POPULAR=resources.getString(R.string.SORTING_ORDER_POPULAR);

        if (SORTING_ORDER.equals(SORTING_ORDER_POPULAR)) {
            String POPULAR_API = resources.getString(R.string.SORTING_BY_POPULAR_API);
            builtUri = Uri.parse(POPULAR_API).buildUpon()
                    .appendQueryParameter(API, API_KEY).build();
        }
        else {
            String RATING_API = resources.getString(R.string.SORTING_BY_RATING_API);
            builtUri = Uri.parse(RATING_API).buildUpon()
                    .appendQueryParameter(API, API_KEY)
                    .build();
        }


        if(Type.equals("movie_search")){


            builtUri = Uri.parse(MovieID).buildUpon()
                    .appendQueryParameter(API, API_KEY)
                    .build();

        }



        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //this function initiate the api call and get the response with a scanner, return string (api response if available)
    private String getDataFromApi(URL url) throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {return scanner.next();}
            else          {return null;}

        }
        finally {httpURLConnection.disconnect();}
    }

    //this function parce the json respone of the api url
    private void Parce_Movies(String[] JSON_OBJECT_STRING,String Type) {

        try {MovieParser movieParser = new MovieParser();
            if (Type.equals("category_search")){
                MainActivity.setMovies(movieParser.ParseCategory(JSON_OBJECT_STRING[0]));
            }
            if(Type.equals("movie_search")){
                DetailActivity.setMovie_trailers(movieParser.ParseTrailers(JSON_OBJECT_STRING[0]));
                DetailActivity.setMovie_reviews_author(movieParser.ParseReviews(JSON_OBJECT_STRING[1],"author"));
                DetailActivity.setMovie_reviews_content(movieParser.ParseReviews(JSON_OBJECT_STRING[1],"content"));
            }

        } catch (JSONException e) {e.printStackTrace();}
    }

    //endregion

    //region asyncTask Class

    //created an async task to make the network call in it to prevent runtime crashes, then sends the results to main activity
    public class MovieRequest extends AsyncTask<String, Void, String[]> {

        private final Boolean Root_Splash;
        private final String Type;
        private final String MovieID;

        public MovieRequest(Boolean root_Splash, String type, String movieID) {
            Root_Splash = root_Splash;
            Type = type;
            MovieID = movieID;
        }

        @Override
        protected String[] doInBackground(String... params) {

            if (Type.equals("category_search")){

                URL ApiRequestUrl = Generate_API_URL(setupSharedPreferences(),"",null);

                try {
                    String JSON_Response = getDataFromApi(ApiRequestUrl);
                    return new String[]{JSON_Response};

                } catch (Exception e) { e.printStackTrace(); return null;}

            }if(Type.equals("movie_search")) {

                String trailersRequestUrl=MovieID+"trailers?";
                URL TrailersRequestUrl = Generate_API_URL("",Type,trailersRequestUrl);
                String reviewsRequestUrl=MovieID+"reviews?";
                URL ReviewsRequestUrl = Generate_API_URL("",Type,reviewsRequestUrl);

                try {
                    String JSON_Response1 = getDataFromApi(TrailersRequestUrl);
                    String JSON_Response2 = getDataFromApi(ReviewsRequestUrl);
                    return new String[]{JSON_Response1,JSON_Response2};

                } catch (Exception e) { e.printStackTrace(); return null;}

            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] JSON_Data) {

            if (JSON_Data != null) {


                Parce_Movies(JSON_Data,Type);


                //check if the AsyncTask is called by the splash screen.. if so, an intent handler will start the MainActivity
                if(Root_Splash) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            mContext.startActivity(intent);
                        }
                    }, 3500);

                    //extra handler to prevent transition glitches :)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((Activity)mContext).finish();
                        }
                    }, 5000);


                }


            }


        }
    }

    //endregion

}
