package com.nanodegree.android.popularmovies.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nanodegree.android.popularmovies.R;
import com.nanodegree.android.popularmovies.Activities.TrailerActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;



public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final ArrayList<String> videoIds;
    private final Context context;

    public TrailerAdapter(ArrayList<String> videoIds, Context context) {
        this.videoIds = videoIds;
        this.context = context;
    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;

        private TrailerViewHolder(View view) {

            super(view);
            imageView =view.findViewById(R.id.trailerView);
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, TrailerActivity.class);
            intent.putExtra("trailer_id", videoIds.get(getAdapterPosition()));
            context.startActivity(intent);

        }


    }


    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_view, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        ImageView imageView=holder.imageView;
        final String videoId = videoIds.get(position);

        //load movie trailerView
        try {
            Picasso.with(context)
                    .load("https://img.youtube.com/vi/"+videoId+"/hqdefault.jpg")
                    .error(R.drawable.error)
                    .into(imageView);} catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return videoIds.size();
    }


}
