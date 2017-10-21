package com.example.tanyayuferova.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.tanyayuferova.popularmovies.databinding.ReviewItemBinding;
import com.example.tanyayuferova.popularmovies.entities.Review;

import java.util.List;

/**
 * Created by Tanya Yuferova on 10/21/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

    private List<Review> data;


    public ReviewsAdapter() {
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        protected final ReviewItemBinding binding;

        public ReviewsAdapterViewHolder(ReviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Review item) {
            binding.tvReviewAuthor.setText(item.getAuthor());
            binding.tvReviewContent.setText(item.getContent());
        }
    }

    @Override
    public ReviewsAdapter.ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ReviewItemBinding itemBinding = ReviewItemBinding.inflate(layoutInflater, parent, false);
        return new ReviewsAdapter.ReviewsAdapterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsAdapterViewHolder holder, int position) {
        Review item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    /**
     * Sets new data list
     * @param data
     */
    public void setData(List<Review> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}

