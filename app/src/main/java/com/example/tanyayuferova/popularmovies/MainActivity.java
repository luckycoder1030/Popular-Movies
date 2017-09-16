package com.example.tanyayuferova.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
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
public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private RecyclerView moviesRV;
    private ProgressBar progressBar;
    private TextView errorMessage;
    protected MoviesAdapter moviesAdapter;
    /* Last loaded page */
    protected int currentPage = 1;
    /* Last selected sorting type */
    protected SortingParam currentSorting = SortingParam.POPULAR;
    public static String EXTRA_MOVIE = "movie";

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
        refreshData(currentSorting);
    }

    /**
     * Load and show new movies list
     * @param sortingParam sorting type
     */
    protected void refreshData(SortingParam sortingParam) {
        showDataView();
        currentPage = 1;
        moviesAdapter.setData(null);
        new FetchMoviesTask().execute(sortingParam);
    }

    /**
     * Load the next page of movies list
     * @param sortingParam sorting type
     */
    protected void loadMoreData(SortingParam sortingParam) {
        /* Page must be less than or equal to 1000 */
        if(++currentPage > 1000)
            return;
        new FetchMoviesTask().execute(sortingParam);
    }

    /**
     * Async task loads movies data
     */
    public class FetchMoviesTask extends AsyncTask<SortingParam, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(SortingParam... params) {
            if (params.length == 0) {
                return null;
            }

            SortingParam sort = params[0];
            URL url = NetworkUtils.buildUrl(sort, currentPage);

            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                List<Movie> data = MoviesJsonUtils.getMoviesDataFromJson(json);
                return data;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> data) {
            progressBar.setVisibility(View.INVISIBLE);
            if (data != null) {
                showDataView();
                moviesAdapter.addData(data);
            } else {
                showErrorMessage();
            }
        }
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
                refreshData(currentSorting);
                return true;

            case R.id.top_rated_action :
                currentSorting = SortingParam.TOP_RATED;
                refreshData(currentSorting);
                return true;

            case R.id.load_more_action :
                loadMoreData(currentSorting);
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
}
