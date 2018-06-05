package com.nanodegree.android.popularmovies.Modules;

public class MovieItem {


    private final String movie_id;
    private final String movie_title;
    private final String movie_picture;
    private final String movie_rating;
    private final String movie_overview;
    private final String movie_release_date;

    public MovieItem(String movie_id, String movie_title, String movie_picture, String movie_rating, String movie_overview, String movie_release_date) {
        this.movie_id = movie_id;
        this.movie_title = movie_title;
        this.movie_picture = movie_picture;
        this.movie_rating = movie_rating;
        this.movie_overview = movie_overview;
        this.movie_release_date = movie_release_date;
    }


    public String getMovie_id() {
        return movie_id;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public String getMovie_picture() {
        return movie_picture;
    }

    public String getMovie_rating() {
        return movie_rating;
    }

    public String getMovie_overview() {
        return movie_overview;
    }

    public String getMovie_release_date() {
        return movie_release_date;
    }


}
