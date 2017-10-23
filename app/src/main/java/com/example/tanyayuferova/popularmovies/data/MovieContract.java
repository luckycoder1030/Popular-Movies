package com.example.tanyayuferova.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tanya Yuferova on 10/21/2017.
 */

/**
 * Description of tables and content provider data
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.tanyayuferova.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favoritemovies";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_REVIEWS = "reviews";


    /**
     * Movie table
     */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIES)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE_AVG = "vote_avg";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_ORIGINAL_TITLE = "orig_title";
        public static final String COLUMN_TAGLINE = "tagline";

        /**
         * Builds url to select movie with particular id
         * @param id
         * @return
         */
        public static Uri buildMovieUriWithId(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }

    /**
     * Trailer table
     */
    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIES)
                .appendPath(PATH_TRAILERS)
                .build();

        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";

        /**
         * Builds uri to select trailers related to particular movie
         * @param id movie id
         * @return
         */
        public static Uri buildTrailersUriWithMovieId(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }

    /**
     * Review table
     */
    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIES)
                .appendPath(PATH_REVIEWS)
                .build();

        public static final String TABLE_NAME = "review";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";

        /**
         * Builds uri to select reviews related to particular movie
         * @param id movie id
         * @return
         */
        public static Uri buildReviewsUriWithMovieId(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }
}
