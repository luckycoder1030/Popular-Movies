package com.example.tanyayuferova.popularmovies;

import android.content.Intent;
import android.database.Cursor;
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


import com.example.tanyayuferova.popularmovies.adapters.MoviesAdapter;
import com.example.tanyayuferova.popularmovies.data.MovieContract;
import com.example.tanyayuferova.popularmovies.entities.Movie;
import com.example.tanyayuferova.popularmovies.utils.MoviesDataUtils;
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
    protected  static final int MOVIE_DETAILS_REQUEST_CODE = 5;


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
        if(SortingParam.FAVORITE.equals(currentSorting) || ++currentPage > 1000)
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
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

            case R.id.favorite_movies_action :
                currentSorting = SortingParam.FAVORITE;
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
        startActivityForResult(intent, MOVIE_DETAILS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MOVIE_DETAILS_REQUEST_CODE && resultCode == RESULT_OK) {
            /* After viewing movie details we need to update the item in the list, because we could
            change some movie data (for example favorite movie) or load additional data that could be
            useful later*/
            Movie movie = data.getParcelableExtra(EXTRA_MOVIE);
            moviesAdapter.updateItem(movie);
        }
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
                Cursor moviesCursor = getContentResolver().query(
                        MovieContract.MovieEntry.CONTENT_URI,
                        null, null, null, null);

                List<Movie> favoriteMovies = MoviesDataUtils.createMoviesListFromCursor(moviesCursor);

                for (Movie movie : favoriteMovies) {
                    Cursor reviews = getContentResolver().query(
                            MovieContract.ReviewEntry.buildReviewsUriWithMovieId(movie.getId()),
                            null, null, null, null);
                    movie.setReviews(MoviesDataUtils.createReviewsListFromCursor(reviews));
                    Cursor trailers = getContentResolver().query(
                            MovieContract.TrailerEntry.buildTrailersUriWithMovieId(movie.getId()),
                            null, null, null, null);
                    movie.setTrailers(MoviesDataUtils.createTrailersListFromCursor(trailers));
                }

                if (SortingParam.FAVORITE.equals(currentSorting)) {
                    return favoriteMovies;
                }

                URL url = NetworkUtils.buildMoviesUrl(currentSorting, currentPage);

                try {
                    String json = NetworkUtils.getResponseFromHttpUrl(url);
                    List<Movie> result = MoviesJsonUtils.getMoviesDataFromJson(json);
                    for(Movie m : result) {
                        if(favoriteMovies.contains(m)){
                            m.setFavorite(true);
                        }
                    }
                    return result;

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
