package com.example.tanyayuferova.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Tanya Yuferova on 9/13/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private List<Movie> data;

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        protected final ImageView movieIcon;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            movieIcon = (ImageView) itemView.findViewById(R.id.iv_movie_icon);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        ImageView movieIcon = holder.movieIcon;
        String posterPath = data.get(position).getFullPosterPath();
        Picasso.with(movieIcon.getContext()).load(posterPath).into(movieIcon);
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    public void setData(List<Movie> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
