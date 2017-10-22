package com.example.tanyayuferova.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tanyayuferova.popularmovies.adapters.TrailersAdapter;
import com.example.tanyayuferova.popularmovies.entities.Movie;
import com.example.tanyayuferova.popularmovies.entities.Trailer;
import com.example.tanyayuferova.popularmovies.utils.NetworkUtils;

import static com.example.tanyayuferova.popularmovies.MainActivity.EXTRA_MOVIE;

public class TrailersActivity extends AppCompatActivity
        implements TrailersAdapter.TrailersAdapterOnClickHandler {

    private RecyclerView trailersRV;
    private TrailersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        trailersRV = (RecyclerView) findViewById(R.id.rv_trailers);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        trailersRV.setLayoutManager(layoutManager);
        adapter = new TrailersAdapter(this);
        adapter.setData(movie.getTrailers());
        trailersRV.setAdapter(adapter);

        String trailersCaption = String.format(getString(R.string.trailers_caption),
                movie.getTrailers() == null ? 0 : movie.getTrailers().size());
        ((TextView) findViewById(R.id.tv_trailers_caption)).setText(trailersCaption);
    }

    @Override
    public void onClick(Trailer trailer) {
        switch (trailer.getSite()) {
            case "YouTube":
                startActivity(new Intent(Intent.ACTION_VIEW, NetworkUtils.getYouTubeVideoUri(trailer.getKey())));
                break;
        }
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
