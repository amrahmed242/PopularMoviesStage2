package com.nanodegree.android.popularmovies.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import com.nanodegree.android.popularmovies.Modules.MovieItem;
import com.nanodegree.android.popularmovies.Provider.MoviesProvider;
import java.util.ArrayList;
import java.util.List;

public class DbContract {

    //region DB structure
    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "favmovies";
        public static final String COLUMN_MOVIE_ID = "mid";
        public static final String COLUMN_MOVIE_TITLE = "mtitle";
        public static final String COLUMN_MOVIE_PICTURE = "mpicture";
        public static final String COLUMN_MOVIE_RATING = "mrating";
        public static final String COLUMN_MOVIE_OVERVIEW = "moverview";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "mreleasedate";

    }
    //endregion

    //region DB Operations

    public static Cursor QueryFavs(Context C) {
        return C.getContentResolver().query(MoviesProvider.MOVIES_URI,null,null,null,null);
    }

    public static List<MovieItem> GetFavsList(Cursor cursor){
        int CursorCount=cursor.getCount();
        ArrayList<MovieItem> FavsList=new ArrayList<>();

        for(int i=0 ; i<CursorCount ; i++) {

            if (!cursor.moveToPosition(i)) {return FavsList;}

            String id = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID));
            String title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_TITLE));
            String picture = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_PICTURE));
            String rating = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_RATING));
            String overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_OVERVIEW));
            String release_date = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
            FavsList.add(new MovieItem(id,title,picture,rating,overview,release_date));
        }
        return FavsList;
    }

    public static boolean CheckFav(String Movie_id,Context C) {

        Cursor cursor =  C.getContentResolver().query(MoviesProvider.MOVIE_ID_URI,null,Movie_id,null,null);


        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }

        cursor.close();
        return false;

    }

    public static Uri addNewFav(Context C, MovieItem mI) {

        ContentValues cv = new ContentValues();
        cv.put(MovieEntry.COLUMN_MOVIE_ID, mI.getMovie_id());
        cv.put(MovieEntry.COLUMN_MOVIE_TITLE, mI.getMovie_title());
        cv.put(MovieEntry.COLUMN_MOVIE_PICTURE, mI.getMovie_picture());
        cv.put(MovieEntry.COLUMN_MOVIE_RATING, mI.getMovie_rating());
        cv.put(MovieEntry.COLUMN_MOVIE_OVERVIEW, mI.getMovie_overview());
        cv.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mI.getMovie_release_date());

        return C.getContentResolver().insert(MoviesProvider.MOVIE_ID_URI,cv);
    }

    public static Boolean removeFav(Context C,String id) {

        return C.getContentResolver().delete(MoviesProvider.MOVIE_ID_URI, MovieEntry.COLUMN_MOVIE_ID + "=" + id, null) >0;

    }

    //endregion
}
