package com.nanodegree.android.popularmovies.Utilities;

import com.nanodegree.android.popularmovies.Modules.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class MovieParser {

    // region Variables

    private final ArrayList<MovieItem> Movies=new ArrayList<>();
    private final ArrayList<String> MovieTrailer=new ArrayList<>();
    private final ArrayList<String> MovieReviews_authors=new ArrayList<>();
    private final ArrayList<String> MovieReviews_content=new ArrayList<>();

    //endregion

    //region parsing functions

    public List<MovieItem> ParseCategory(String JSON_OBJECT_STRING) throws JSONException {

        JSONObject jsonObject=new JSONObject(JSON_OBJECT_STRING);
        JSONArray jsonArray=jsonObject.getJSONArray("results");


        for(int i=0;i<jsonArray.length();i++){
            JSONObject Movie=jsonArray.getJSONObject(i);

            String movie_id=Movie.getString("id");
            String movie_title=Movie.getString("title");
            String movie_picture=Movie.getString("poster_path");
            String movie_rating=Movie.getString("vote_average");
            String movie_overview=Movie.getString("overview");
            String movie_release_date=Movie.getString("release_date");

            MovieItem movieItem=new MovieItem(movie_id, movie_title,movie_picture,movie_rating,movie_overview,movie_release_date);
            Movies.add(movieItem);
        }

        return Movies;
    }


    public ArrayList<String> ParseTrailers(String JSON_OBJECT_STRING) throws JSONException {

        JSONObject jsonObject=new JSONObject(JSON_OBJECT_STRING);
        JSONArray jsonArrayResults=jsonObject.getJSONArray("youtube");


        for(int i=0;i<jsonArrayResults.length();i++){
            JSONObject Trailer=jsonArrayResults.getJSONObject(i);

            String movie_trailer= Trailer.getString("source");

            MovieTrailer.add(movie_trailer);
        }

        return MovieTrailer;
    }


    public ArrayList<String> ParseReviews(String JSON_OBJECT_STRING,String Title) throws JSONException {

        JSONObject jsonObject=new JSONObject(JSON_OBJECT_STRING);
        JSONArray jsonArrayResults=jsonObject.getJSONArray("results");


        if(Title.equals("author")) {
            for (int i = 0; i < jsonArrayResults.length(); i++) {


                JSONObject Trailer = jsonArrayResults.getJSONObject(i);

                String movieReview_author = Trailer.getString("author")+" ";

                MovieReviews_authors.add(movieReview_author);


            }

            return MovieReviews_authors;
        }else {

            for (int i = 0; i < jsonArrayResults.length(); i++) {
                JSONObject Trailer = jsonArrayResults.getJSONObject(i);

                String movieReview_content = Trailer.getString("content");


                movieReview_content = movieReview_content.replaceAll("(\\r)", "");
                movieReview_content = movieReview_content.replaceAll("(_)", " ");
                movieReview_content = movieReview_content.replaceAll("(\\n\\n)", "\\n");


                MovieReviews_content.add(movieReview_content);
            }

            return MovieReviews_content;
        }
    }

    //endregion
}
