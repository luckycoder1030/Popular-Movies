package com.example.tanyayuferova.popularmovies;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tanyayuferova.popularmovies.entities.Movie;
import com.example.tanyayuferova.popularmovies.utils.MoviesJsonUtils;
import com.example.tanyayuferova.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.SimpleDateFormat;

import static com.example.tanyayuferova.popularmovies.MainActivity.EXTRA_MOVIE;

/**
 * This activity displays detailed movie information
 */
public class MovieDetails extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Movie> {

    private static final int DETAILS_MOVIE_LOADER_ID = 2;
    private TextView titleTextView;
    private TextView overviewTextView;
    private TextView voteAvgTextView;
    private TextView releasedDateTextView;
    private ImageView posterImageView;
    private TextView starsTextView;

    /* Current item */
    protected Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setResult(RESULT_OK, getIntent());

        titleTextView = (TextView) findViewById(R.id.tv_title);
        overviewTextView = (TextView) findViewById(R.id.tv_overview);
        voteAvgTextView = (TextView) findViewById(R.id.tv_vote_avg);
        releasedDateTextView = (TextView) findViewById(R.id.tv_released_date);
        posterImageView = (ImageView) findViewById(R.id.iv_poster);
        starsTextView = (TextView) findViewById(R.id.tv_vote_starts);

        if(getIntent().hasExtra(EXTRA_MOVIE)) {
            displayMovieData((Movie) getIntent().getParcelableExtra(EXTRA_MOVIE));
        }

        /* If some data is not loaded we need to load id */
        if(isLoaderNeeded(movie))
            getSupportLoaderManager().initLoader(DETAILS_MOVIE_LOADER_ID, null, MovieDetails.this);

        /* If we loaded it earlier, we need just display it */
        else
            displayAdditionalMovieData(movie);

    }

    /**
     * Sets movie data in fields
     * @param movie
     */
    protected void displayMovieData(Movie movie) {
        this.movie = movie;
        titleTextView.setText(movie.getFullTitle());
        overviewTextView.setText(movie.getOverview());
        if(movie.getReleasedDate() != null) {
            releasedDateTextView.setText(R.string.release_date);
            releasedDateTextView.append(new SimpleDateFormat(" MMMM dd, yyyy").format(movie.getReleasedDate()));
        }
        Picasso.with(this).load(movie.getFullPosterPath()).into(posterImageView);

        Double doubleVoteAvg = movie.getDoubleVoteAvg();
        voteAvgTextView.setText(String.format("%1.1f", doubleVoteAvg));
        starsTextView.setText(getVoteStarsText(doubleVoteAvg));
    }

    /**
     * Sets additional movie data that can be loaded in background thread
     * @param movie
     */
    protected void displayAdditionalMovieData(Movie movie) {

    }

    /**
     * @param doubleVoteAvg average vote
     * @return id of string which shows how many stars the movie has
     */
    protected int getVoteStarsText(Double doubleVoteAvg){
        int stars = R.string.stars_0;
        if(doubleVoteAvg >= 9.5)
            stars = R.string.stars_10;
        else if(doubleVoteAvg >= 8.5)
            stars = R.string.stars_9;
        else if(doubleVoteAvg >= 7.5)
            stars = R.string.stars_8;
        else if(doubleVoteAvg >= 6.5)
            stars = R.string.stars_7;
        else if(doubleVoteAvg >= 5.5)
            stars = R.string.stars_6;
        else if(doubleVoteAvg >= 4.5)
            stars = R.string.stars_5;
        else if(doubleVoteAvg >= 3.5)
            stars = R.string.stars_4;
        else if(doubleVoteAvg >= 2.5)
            stars = R.string.stars_3;
        else if(doubleVoteAvg >= 1.5)
            stars = R.string.stars_2;
        else if(doubleVoteAvg >= 0.5)
            stars = R.string.stars_1;
        return stars;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            /* To prevent MainActivity reload data*/
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean isLoaderNeeded(Movie movie) {
        return movie.getTagline() == null || movie.getReviews() == null || movie.getTrailers() == null;
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {

            Movie data = null;

            @Override
            protected void onStartLoading() {
                if (data != null) {
                    deliverResult(data);
                } else {
                    forceLoad();
                }
            }
            @Override
            public Movie loadInBackground() {
                /**
                 * Loads additional data
                 */
                try {
                    URL url = NetworkUtils.buildMovieUrl(movie.getId());
                    String json = NetworkUtils.getResponseFromHttpUrl(url);
                    movie.setTagline(MoviesJsonUtils.getStringValueFromJson(json, MoviesJsonUtils.TAG_LINE));

                    url = NetworkUtils.buildReviewsUrl(movie.getId(), 1);
                    json = NetworkUtils.getResponseFromHttpUrl(url);
                    movie.setReviews(MoviesJsonUtils.getReviewsDataFromJson(json));

                    url = NetworkUtils.buildVideosUrl(movie.getId(), 1);
                    json = NetworkUtils.getResponseFromHttpUrl(url);
                    movie.setTrailers(MoviesJsonUtils.getTrailersDataFromJson(json));

                    return movie;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Movie data) {
                this.data = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        if (data != null) {
            this.movie = data;
            displayAdditionalMovieData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }
}
