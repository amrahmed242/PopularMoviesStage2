package com.nanodegree.android.popularmovies.Adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nanodegree.android.popularmovies.R;
import java.util.ArrayList;



public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final ArrayList<String> MovieReviews_authors;
    private final ArrayList<String> MovieReviews_content;



    public ReviewAdapter(ArrayList<String> movieReviews_authors, ArrayList<String> movieReviews_content) {
        MovieReviews_authors = movieReviews_authors;
        MovieReviews_content = movieReviews_content;

    }


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_view, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        TextView author=holder.author;
        TextView content=holder.content;

        author.setText(MovieReviews_authors.get(position));
        content.setText(MovieReviews_content.get(position));
    }

    @Override
    public int getItemCount() {
        return MovieReviews_authors.size();
    }


    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        final TextView author;
        final TextView content;

        private ReviewViewHolder(View view) {
            super(view);
            author =view.findViewById(R.id.Author);
            content =view.findViewById(R.id.Content);
        }
    }

}
