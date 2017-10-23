package com.example.tanyayuferova.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tanyayuferova.popularmovies.R;
import com.example.tanyayuferova.popularmovies.entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tanya Yuferova on 9/13/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private List<Movie> data;

    private final MoviesAdapterOnClickHandler onClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected final ImageView movieIcon;
        protected final ImageView favoriteMark;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            movieIcon = (ImageView) itemView.findViewById(R.id.iv_movie_icon);
            favoriteMark = (ImageView) itemView.findViewById(R.id.iv_favorite_mark);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            onClickHandler.onClick(data.get(adapterPosition));
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
        Movie movie = data.get(position);

        ImageView movieIcon = holder.movieIcon;
        String posterPath = movie.getFullPosterPath();
        Picasso.with(movieIcon.getContext())
                .load(posterPath)
                .error(R.drawable.ic_question_mark)
                .into(movieIcon);

        holder.favoriteMark.setVisibility(movie.isFavorite() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    /**
     * Sets new data list
     * @param data
     */
    public void setData(List<Movie> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    /**
     * Add new data to the existing list
     * @param data
     */
    public void addData(List<Movie> data) {
        if(this.data == null)
            this.data = new ArrayList<>();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * Updates item in list
     * @param item
     */
    public void updateItem(Movie item) {
        int position = data.indexOf(item);
        if (position>0){
            data.set(position, item);
            notifyItemChanged(position);
        }
    }
}
