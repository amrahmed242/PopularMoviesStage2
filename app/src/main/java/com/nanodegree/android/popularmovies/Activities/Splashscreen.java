package com.nanodegree.android.popularmovies.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.nanodegree.android.popularmovies.R;
import com.nanodegree.android.popularmovies.Utilities.Api;
import com.nanodegree.android.popularmovies.Utilities.ConnectionStatus;
import com.nanodegree.android.popularmovies.databinding.ActivitySplashscreenBinding;


/*
 * placing all the background process and network calls & parsing
 * in the splash screen to improve the UI response time
 * and reduce waiting time for the user
 *
 */
public class Splashscreen extends Activity {


    //region variables
    private ActivitySplashscreenBinding Binding;
    //endregion

    //region @Overrides
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_splashscreen);

        //show animation
        animate_Views();

        //check if device is connected to internet
        checkConnection();

    }
    //endregion

    //region functions

    //this function animate the splashscreen view objects
    private void animate_Views() {

        Animation animate = AnimationUtils.loadAnimation(this, R.anim.show);
        animate.reset();
        Binding.splash.clearAnimation();
        Binding.splash.startAnimation(animate);

        animate = AnimationUtils.loadAnimation(this, R.anim.move);
        animate.reset();

        Binding.imageView1.clearAnimation();
        Binding.imageView1.startAnimation(animate);

        animate = AnimationUtils.loadAnimation(this, R.anim.move2);
        animate.reset();

        Binding.imageView2.clearAnimation();
        Binding.imageView2.startAnimation(animate);

        Binding.imageView3.animate().alpha(1f).setDuration(3000);
        Binding.progressBar.animate().alpha(.9f).setDuration(6000);

    }

    //this function check the connection status and call the API
    private void checkConnection(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String KEY=getResources().getString(R.string.SORTING_ORDER_KEY);
        String Default_Sort=getResources().getString(R.string.SORTING_ORDER_POPULAR);
        String Fav_Sort=getResources().getString(R.string.SORTING_ORDER_FAVOURITES);



        ConnectionStatus connectionStatus=new ConnectionStatus(this);

        if(connectionStatus.isOnline()){

            if(!pref.getString(KEY, Default_Sort).equals(Fav_Sort)) {

                Api api = new Api(this);
                api.new MovieRequest(true, "category_search", null).execute();

            } else{


                final Context context=this;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("Fav",getResources().getString(R.string.SORTING_ORDER_FAVOURITES));
                        context.startActivity(intent);
                    }
                }, 2500);

                //extra handler to prevent transition glitches :)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((Activity)context).finish();
                    }
                }, 4000);


            }


        }else {

            final Context context=this;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(context,"Check internet connection",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("Fav",getResources().getString(R.string.SORTING_ORDER_FAVOURITES));
                    context.startActivity(intent);
                }
            }, 2500);

            //extra handler to prevent transition glitches :)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((Activity)context).finish();
                }
            }, 4000);
        }

    }

    //endregion

}
