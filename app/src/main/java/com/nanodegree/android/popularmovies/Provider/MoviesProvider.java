package com.nanodegree.android.popularmovies.Provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.nanodegree.android.popularmovies.DB.DbContract;
import com.nanodegree.android.popularmovies.DB.DbHelper;

public class MoviesProvider extends ContentProvider {

    //region variables

    private static final int MOVIES = 1;
    private static final int MOVIE_ID = 2;

    private static final String AUTHORITY = "com.nanodegree.android.popularmovies.Provider";
    private static final String BASE_PATH = DbContract.MovieEntry.TABLE_NAME;


    public static final Uri MOVIES_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    public static final Uri MOVIE_ID_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH + "/" + MOVIE_ID);

    private SQLiteDatabase database;

    private static final UriMatcher UriMatcher = buildUriMatcher();


    //endregion

    //region functions

    //return the uri matcher of the CB
    private static UriMatcher buildUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(android.content.UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY,BASE_PATH, MOVIES);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",MOVIE_ID);

        return uriMatcher;
    }

    //endregion

    //region @Overrides


    @Override
    public boolean onCreate() {
        DbHelper helper = new DbHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor=null;

        switch (UriMatcher.match(uri)) {


            case MOVIES:
                cursor =  database.query(
                        DbContract.MovieEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        DbContract.MovieEntry._ID
                );
                break;


            case MOVIE_ID:
                String query = "SELECT * FROM "+ DbContract.MovieEntry.TABLE_NAME+" WHERE "+ DbContract.MovieEntry.COLUMN_MOVIE_ID+"='"+selection+"'";

                cursor = database.rawQuery(query, null);

                break;

        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues cv) {

        long id = database.insert(DbContract.MovieEntry.TABLE_NAME,null,cv);
        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(MOVIE_ID_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (UriMatcher.match(uri)) {
            case MOVIES:
                return AUTHORITY;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        int delCount;

        switch (UriMatcher.match(uri)) {

            case MOVIE_ID:
                delCount =  database.delete(DbContract.MovieEntry.TABLE_NAME,s,strings);
                break;


                default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updCount;

        switch (UriMatcher.match(uri)) {
            case MOVIE_ID:
                updCount =  database.update(DbContract.MovieEntry.TABLE_NAME,contentValues,s,strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
    }

    //endregion

}
