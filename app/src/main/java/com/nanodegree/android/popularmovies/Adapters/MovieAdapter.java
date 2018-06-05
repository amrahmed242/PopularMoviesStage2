package com.nanodegree.android.popularmovies.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nanodegree.android.popularmovies.DB.DbContract;
import com.nanodegree.android.popularmovies.Utilities.Api;
import com.nanodegree.android.popularmovies.Activities.DetailActivity;
import com.nanodegree.android.popularmovies.Modules.MovieItem;
import com.nanodegree.android.popularmovies.R;
import com.squareup.picasso.Picasso;
import java.util.List;


//////////////////////////////***********normal Recycler view adapter filling the grid with the movie data*******///////////////////////////////////
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final List<MovieItem> Movies_Array_list;
    private final Context context;
    private final Boolean favorites;

    public MovieAdapter(List<MovieItem> movies_Array_list,Context context,Boolean fav) {
        this.Movies_Array_list = movies_Array_list;
        this.context = context;
        this.favorites=fav;

    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{

        final ImageView poster;
        final ImageView favButton;


        private ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            poster=itemView.findViewById(R.id.movie_poster);
            favButton=itemView.findViewById(R.id.fav);
        }


        //overriding onclick to send clicked item data to the detail activity & request more data from the api
        @Override
        public void onClick(View v) {

//          getting selected movie trailers and reviews
            String BASE_API = context.getResources().getString(R.string.BASE_API);
            String ID=BASE_API+Movies_Array_list.get(getAdapterPosition()).getMovie_id()+"/";
            Api api=new Api(context);
            api.new MovieRequest(false,"movie_search",ID).execute();



            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("movie_id",Movies_Array_list.get(getAdapterPosition()).getMovie_id());
            intent.putExtra("movie_title", Movies_Array_list.get(getAdapterPosition()).getMovie_title());
            intent.putExtra("movie_picture", Movies_Array_list.get(getAdapterPosition()).getMovie_picture());
            intent.putExtra("movie_rating", Movies_Array_list.get(getAdapterPosition()).getMovie_rating());
            intent.putExtra("movie_overview", Movies_Array_list.get(getAdapterPosition()).getMovie_overview());
            intent.putExtra("movie_release_date", Movies_Array_list.get(getAdapterPosition()).getMovie_release_date());
            context.startActivity(intent);

        }

        @Override
        public boolean onLongClick(View v) {

            int position=getAdapterPosition();
            String Movie_id=Movies_Array_list.get(position).getMovie_id();

            if(checkMovie(position)){

                if(deleteMovie(Movie_id)){

                if(favorites){

                    //remove from adapter
                    Movies_Array_list.remove(position);
                    notifyItemRemoved(position);

                }else {
                    favButton.animate().alpha(.1f).setDuration(500);
                }

             }

            }else {
                favButton.animate().alpha(1f).setDuration(500);
                addMovie(position);

            }

            return true;

        }



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        int item_layout=R.layout.movie_item_view;
        View Movie_item = LayoutInflater.from(context).inflate(item_layout, parent, false);
        return new ViewHolder(Movie_item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {Picasso.with(holder.poster.getContext())
                    .load(String.valueOf("http://image.tmdb.org/t/p/w500/"+Movies_Array_list.get(position).getMovie_picture()))
                    .error(R.drawable.error)
                    .into(holder.poster);
        } catch (Exception e) {
            e.printStackTrace();
        }

       if(checkMovie(position)){
           holder.favButton.setAlpha(1f);
       }else {
           holder.favButton.setAlpha(.1f);

       }



    }
    @Override
    public int getItemCount() {
        if (Movies_Array_list==null) return 0;
        return Movies_Array_list.size();
    }



    private Boolean checkMovie(int position) {

        String id=Movies_Array_list.get(position).getMovie_id();

        return DbContract.CheckFav(id, context);

    }

    private void addMovie(int position) {

        DbContract.addNewFav(context, Movies_Array_list.get(position));

    }

    private Boolean deleteMovie(String Movie_id) {

       return DbContract.removeFav(context,Movie_id);

    }


}
