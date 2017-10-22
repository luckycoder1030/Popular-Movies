package com.example.tanyayuferova.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tanyayuferova.popularmovies.data.MovieContract;
import com.example.tanyayuferova.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.example.tanyayuferova.popularmovies.entities.Movie;
import com.example.tanyayuferova.popularmovies.entities.Review;
import com.example.tanyayuferova.popularmovies.entities.Trailer;
import com.example.tanyayuferova.popularmovies.utils.MoviesDataUtils;
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
    private ActivityMovieDetailsBinding binding;

    /* Current item */
    protected Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setResult(RESULT_OK, getIntent());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        if(getIntent().hasExtra(EXTRA_MOVIE)) {
            displayMovieData((Movie) getIntent().getParcelableExtra(EXTRA_MOVIE));
        }

        /* If some data is not loaded we need to load id */
        if(isLoaderNeeded(movie))
            getSupportLoaderManager().initLoader(DETAILS_MOVIE_LOADER_ID, null, MovieDetails.this);

        /* If we loaded it earlier, we need just display it */
        else
            displayAdditionalMovieData(movie);

        setTrailerOnClickListener();
    }

    /**
     * Sets movie data in fields
     * @param movie
     */
    protected void displayMovieData(Movie movie) {
        this.movie = movie;
        binding.tvTitle.setText(movie.getDescription());
        binding.tvOverview.setText(movie.getOverview());
        Picasso.with(this).load(movie.getFullPosterPath()).into(binding.ivPoster);
        Double doubleVoteAvg = movie.getDoubleVoteAvg();
        binding.tvVoteAvg.setText(String.format("%1.1f", doubleVoteAvg));
        binding.tvVoteStarts.setText(getVoteStarsText(doubleVoteAvg));
        isFavoriteChanged(movie.isFavorite());
    }

    /**
     * Sets additional movie data that can be loaded in background thread
     * @param movie
     */
    protected void displayAdditionalMovieData(Movie movie) {
        binding.tvTagLine.setText(movie.getTagline());
        String trailersCaption = String.format(getString(R.string.trailers_caption),
                movie.getTrailers()==null ? 0 :movie.getTrailers().size());
        String reviewsCaption = String.format(getString(R.string.reviews_caption),
                movie.getReviews()==null ? 0 :movie.getReviews().size());
        binding.tvTrailersCaption.setText(trailersCaption);
        binding.tvReviewsCaption.setText(reviewsCaption);

        if(movie.getTrailers() != null && movie.getTrailers().size()>0) {
            setTrailerData(movie.getTrailers().get(0));
        }
        if(movie.getReviews() != null && movie.getReviews().size()>0) {
            setReviewData(movie.getReviews().get(0));
        }
    }

    protected void setTrailerData(Trailer trailer) {
        binding.trailer.tvTrailerName.setText(trailer.getDescription());
    }

    protected void setReviewData(Review review) {
        binding.review.tvReviewAuthor.setText(review.getAuthor());
        binding.review.tvReviewContent.setText(review.getContent());
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

    public void trailersCaptionOnClick(View view) {
        Intent intent = new Intent(this, TrailersActivity.class);
        intent.putExtra(MainActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    public void reviewsCaptionOnClick(View view) {
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra(MainActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    protected void setTrailerOnClickListener() {
        findViewById(R.id.trailer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movie.getTrailers()==null || movie.getTrailers().size()<=0)
                    return;
                Trailer trailer = movie.getTrailers().get(0);
                switch (trailer.getSite()){
                    case "YouTube":
                        startActivity(new Intent(Intent.ACTION_VIEW, NetworkUtils.getYouTubeVideoUri(trailer.getKey())));
                        break;
                }
            }
        });
    }

    public void favoriteBtnOnClick(View view) {
        String notification = null;
        if(movie.isFavorite()) {
            getContentResolver().delete(MovieContract.MovieEntry.buildMovieUriWithId(movie.getId()), null, null);
            notification = String.format(getString(R.string.unfavorite_notification), movie.getTitle());
        } else {
            getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, MoviesDataUtils.getContentValues(movie));
            if(movie.getTrailers() != null) {
                for (Trailer trailer : movie.getTrailers()) {
                    getContentResolver().insert(MovieContract.TrailerEntry.buildTrailersUriWithMovieId(movie.getId()),
                            MoviesDataUtils.getContentValues(trailer));
                }
            }
            if(movie.getReviews() != null) {
                for(Review review : movie.getReviews()) {
                    getContentResolver().insert(MovieContract.ReviewEntry.buildReviewsUriWithMovieId(movie.getId()),
                            MoviesDataUtils.getContentValues(review));
                }
            }
            notification = String.format(getString(R.string.favorite_notification), movie.getTitle());
        }
        Toast.makeText(this, notification, Toast.LENGTH_LONG).show();
        movie.setFavorite(!movie.isFavorite());
        isFavoriteChanged(movie.isFavorite());
    }

    protected void isFavoriteChanged(boolean isFavorite) {
        binding.ivFavorite.setVisibility(isFavorite ? View.VISIBLE : View.INVISIBLE);
        binding.btnFavorite.setText(getString(isFavorite ? R.string.unfavorite : R.string.favorite));
    }
}
