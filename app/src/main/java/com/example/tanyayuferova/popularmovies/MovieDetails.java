package com.example.tanyayuferova.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import static com.example.tanyayuferova.popularmovies.MainActivity.EXTRA_MOVIE;

public class MovieDetails extends AppCompatActivity {

    private TextView titleTextView;
    private TextView overviewTextView;
    private TextView voteAvgTextView;
    private TextView releasedDateTextView;
    private ImageView posterImageView;
    private TextView starsTextView;

    protected Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        titleTextView = (TextView) findViewById(R.id.tv_title);
        overviewTextView = (TextView) findViewById(R.id.tv_overview);
        voteAvgTextView = (TextView) findViewById(R.id.tv_vote_avg);
        releasedDateTextView = (TextView) findViewById(R.id.tv_released_date);
        posterImageView = (ImageView) findViewById(R.id.iv_poster);
        starsTextView = (TextView) findViewById(R.id.tv_vote_starts);

        if(getIntent().hasExtra(EXTRA_MOVIE)) {
            setMovie((Movie) getIntent().getParcelableExtra(EXTRA_MOVIE));
        }
    }

    protected void setMovie(Movie movie) {
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
        setVoteStarsText(doubleVoteAvg);
    }

    protected void setVoteStarsText(Double doubleVoteAvg){
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
        starsTextView.setText(stars);
    }
}
