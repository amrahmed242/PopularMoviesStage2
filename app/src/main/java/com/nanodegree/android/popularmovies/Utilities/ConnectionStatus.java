package com.nanodegree.android.popularmovies.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionStatus {

    //region variables

    private final Context context;
    private boolean connected = false;

    //endregion


    //region functions

    public ConnectionStatus(Context context) {
        this.context = context;
    }

    public boolean isOnline() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {Log.v("connectivity", e.toString()); }
        return connected;
    }

    //endregion

}