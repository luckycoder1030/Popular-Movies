package com.example.tanyayuferova.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.tanyayuferova.popularmovies.utils.MoviesJsonUtils;
import com.example.tanyayuferova.popularmovies.utils.NetworkUtils;
import com.example.tanyayuferova.popularmovies.utils.NetworkUtils.SortingParam;


import java.net.URL;
import java.util.List;

/**
 * This activity is responsible to show movies list
 */
public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MoviesAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private RecyclerView moviesRV;
    private ProgressBar progressBar;
    private TextView errorMessage;
    protected MoviesAdapter moviesAdapter;
    /* Last loaded page */
    protected int currentPage = 1;
    /* Last selected sorting type */
    protected SortingParam currentSorting = SortingParam.POPULAR;
    public static String EXTRA_MOVIE = "movie";
    private static final int MOVIES_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRV = (RecyclerView) findViewById(R.id.rv_movies);
        progressBar = (ProgressBar) findViewById(R.id.pb_progress);
        errorMessage = (TextView) findViewById(R.id.tv_error_message);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        moviesRV.setLayoutManager(layoutManager);
        moviesAdapter = new MoviesAdapter(this);
        moviesRV.setAdapter(moviesAdapter);
        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, MainActivity.this);
    }

    /**
     * Load new movies list
     */
    protected void refreshData() {
        currentPage = 1;
        moviesAdapter.setData(null);
        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, MainActivity.this);
    }

    /**
     * Load the next page of movies list
     */
    protected void loadMoreData() {
        /* Page must be less than or equal to 1000 */
        if(++currentPage > 1000)
            return;
        getSupportLoaderManager().getLoader(MOVIES_LOADER_ID).forceLoad();
    }

    protected void showDataView() {
        errorMessage.setVisibility(View.INVISIBLE);
        moviesRV.setVisibility(View.VISIBLE);
    }

    protected void showErrorMessage() {
        errorMessage.setVisibility(View.VISIBLE);
        moviesRV.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();
        switch (selectedId) {
            case R.id.popular_action :
                currentSorting = SortingParam.POPULAR;
                refreshData();
                return true;

            case R.id.top_rated_action :
                currentSorting = SortingParam.TOP_RATED;
                refreshData();
                return true;

            case R.id.load_more_action :
                loadMoreData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            List<Movie> data = null;

            @Override
            protected void onStartLoading() {
                if (data != null) {
                    deliverResult(data);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }
            @Override
            public List<Movie> loadInBackground() {
                URL url = NetworkUtils.buildUrl(currentSorting, currentPage);

                try {
                    String json = NetworkUtils.getResponseFromHttpUrl(url);
                    return MoviesJsonUtils.getMoviesDataFromJson(json);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(List<Movie> data) {
                this.data = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        progressBar.setVisibility(View.INVISIBLE);
        if (data != null) {
            showDataView();
            moviesAdapter.addData(data);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}
