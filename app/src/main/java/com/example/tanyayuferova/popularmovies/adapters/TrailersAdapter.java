package com.example.tanyayuferova.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tanyayuferova.popularmovies.databinding.TrailerItemBinding;
import com.example.tanyayuferova.popularmovies.entities.Trailer;

import java.util.List;

/**
 * Created by Tanya Yuferova on 10/21/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    private List<Trailer> data;
    private final TrailersAdapter.TrailersAdapterOnClickHandler onClickHandler;

    public interface TrailersAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    public TrailersAdapter(TrailersAdapter.TrailersAdapterOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected final TrailerItemBinding binding;

        public TrailersAdapterViewHolder(TrailerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(Trailer item) {
            binding.tvTrailerName.setText(item.getDescription());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            onClickHandler.onClick(data.get(adapterPosition));
        }
    }

    @Override
    public TrailersAdapter.TrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TrailerItemBinding itemBinding = TrailerItemBinding.inflate(layoutInflater, parent, false);
        return new TrailersAdapter.TrailersAdapterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailersAdapterViewHolder holder, int position) {
        Trailer item = data.get(position);
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
    public void setData(List<Trailer> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
