package com.example.tanyayuferova.popularmovies.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.tanyayuferova.popularmovies.entities.Movie;
import com.example.tanyayuferova.popularmovies.entities.Review;
import com.example.tanyayuferova.popularmovies.entities.Trailer;

import static com.example.tanyayuferova.popularmovies.data.MovieContract.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanya Yuferova on 10/21/2017.
 */

/**
 * Helper to provide data for content provider
 */
public class MoviesDataUtils {

    /**
     * Converts cursor to Movies list. Only for favorite movies
     * @param cursor
     * @return
     */
    public static List<Movie> createMoviesListFromCursor(Cursor cursor) {
        List<Movie> result = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(MovieEntry._ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)));
                movie.setVoteAvg(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVG)));
                try {
                    movie.setReleasedDate(new SimpleDateFormat("yyyy-MM-dd").parse(
                            cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE))));
                } catch (ParseException ex){
                    ex.printStackTrace();
                }
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE)));
                movie.setTagline(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TAGLINE)));
                movie.setFavorite(true);
                result.add(movie);
            }
            while (cursor.moveToNext());
        }
        return result;
    }

    /**
     * Converts cursor to Trailers list
     * @param cursor
     * @return
     */
    public static List<Trailer> createTrailersListFromCursor(Cursor cursor) {
        List<Trailer> result = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                Trailer trailer = new Trailer();
                trailer.setId(cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_ID)));
                trailer.setName(cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_NAME)));
                trailer.setSite(cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_SITE)));
                trailer.setKey(cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_KEY)));
                trailer.setLanguage(cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_LANGUAGE)));
                result.add(trailer);
            }
            while (cursor.moveToNext());
        }
        return result;
    }

    /**
     * Converts cursor to Reviews list
     * @param cursor
     * @return
     */
    public static List<Review> createReviewsListFromCursor(Cursor cursor) {
        List<Review> result = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                Review review = new Review();
                review.setId(cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_ID)));
                review.setUrl(cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_URL)));
                review.setContent(cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_CONTENT)));
                review.setAuthor(cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_AUTHOR)));
                result.add(review);
            }
            while (cursor.moveToNext());
        }
        return result;
    }

    /**
     * Creates content values from movie
     * @param movie
     * @return
     */
    public static ContentValues getContentValues(Movie movie) {
        ContentValues result = new ContentValues();
        result.put(MovieEntry._ID, Long.parseLong(movie.getId()));
        result.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        result.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        result.put(MovieEntry.COLUMN_VOTE_AVG, movie.getVoteAvg());
        result.put(MovieEntry.COLUMN_RELEASE_DATE, new SimpleDateFormat("yyyy-MM-dd").format(movie.getReleasedDate()));
        result.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        result.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        result.put(MovieEntry.COLUMN_TAGLINE, movie.getTagline());
        return result;
    }

    /**
     * Creates content values from trailer
     * @param trailer
     * @return
     */
    public static ContentValues getContentValues(Trailer trailer) {
        ContentValues result = new ContentValues();
        result.put(TrailerEntry.COLUMN_ID, trailer.getId());
        result.put(TrailerEntry.COLUMN_NAME, trailer.getName());
        result.put(TrailerEntry.COLUMN_KEY, trailer.getKey());
        result.put(TrailerEntry.COLUMN_LANGUAGE, trailer.getLanguage());
        result.put(TrailerEntry.COLUMN_SITE, trailer.getSite());
        return result;
    }

    /**
     * Created content values from Review
     * @param review
     * @return
     */
    public static ContentValues getContentValues(Review review) {
        ContentValues result = new ContentValues();
        result.put(ReviewEntry.COLUMN_ID, review.getId());
        result.put(ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
        result.put(ReviewEntry.COLUMN_URL, review.getUrl());
        result.put(ReviewEntry.COLUMN_CONTENT, review.getContent());
        return result;
    }
}
