package com.example.tanyayuferova.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.tanyayuferova.popularmovies.adapters.ReviewsAdapter;
import com.example.tanyayuferova.popularmovies.entities.Movie;

import static com.example.tanyayuferova.popularmovies.MainActivity.EXTRA_MOVIE;

public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView reviewsRV;
    private ReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        reviewsRV = (RecyclerView) findViewById(R.id.rv_reviews);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reviewsRV.setLayoutManager(layoutManager);
        adapter = new ReviewsAdapter();
        adapter.setData(movie.getReviews());
        reviewsRV.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
