package com.example.tanyayuferova.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.example.tanyayuferova.popularmovies.data.MovieContract.*;

/**
 * Created by Tanya Yuferova on 10/21/2017.
 */

/**
 * Content provider
 */
public class FavoriteMoviesProvider extends ContentProvider {
    public static final int CODE_FAVORITE_MOVIES = 100;
    public static final int CODE_FAVORITE_MOVIE_WITH_ID = 101;
    public static final int CODE_TRAILERS_WITH_MOVIE_ID = 201;
    public static final int CODE_REVIEWS_WITH_MOVIE_ID = 301;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMoviesDBHelper dbHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        /* All movies */
        matcher.addURI(authority, PATH_FAVORITE_MOVIES, CODE_FAVORITE_MOVIES);
        /* Movie with id */
        matcher.addURI(authority, PATH_FAVORITE_MOVIES + "/#", CODE_FAVORITE_MOVIE_WITH_ID);
        /* Movie trailers with movie id */
        matcher.addURI(authority, PATH_FAVORITE_MOVIES + "/" + PATH_TRAILERS + "/#", CODE_TRAILERS_WITH_MOVIE_ID);
        /* Movie reviews with movie id */
        matcher.addURI(authority, PATH_FAVORITE_MOVIES + "/" + PATH_REVIEWS + "/#", CODE_REVIEWS_WITH_MOVIE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new FavoriteMoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIES: {
                cursor = dbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_FAVORITE_MOVIE_WITH_ID: {
                String[] selectionArguments = new String[]{uri.getLastPathSegment()};
                cursor = dbHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        MovieEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_TRAILERS_WITH_MOVIE_ID: {
                String[] selectionArguments = new String[]{uri.getLastPathSegment()};
                cursor = dbHelper.getReadableDatabase().query(
                        TrailerEntry.TABLE_NAME,
                        projection,
                        TrailerEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_REVIEWS_WITH_MOVIE_ID: {
                String[] selectionArguments = new String[]{uri.getLastPathSegment()};
                cursor = dbHelper.getReadableDatabase().query(
                        ReviewEntry.TABLE_NAME,
                        projection,
                        ReviewEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIES: {
                long id = dbHelper.getWritableDatabase().insert(MovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    result = MovieEntry.buildMovieUriWithId(String.valueOf(id));
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            case CODE_REVIEWS_WITH_MOVIE_ID: {
                String movieId = uri.getLastPathSegment();
                values.put(ReviewEntry.COLUMN_MOVIE_ID, movieId);

                long newId = dbHelper.getWritableDatabase().insert(ReviewEntry.TABLE_NAME, null, values);
                if (newId > 0) {
                    result = ReviewEntry.buildReviewsUriWithMovieId(movieId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            case CODE_TRAILERS_WITH_MOVIE_ID: {
                String movieId = uri.getLastPathSegment();
                values.put(TrailerEntry.COLUMN_MOVIE_ID, movieId);

                long newId = dbHelper.getWritableDatabase().insert(TrailerEntry.TABLE_NAME, null, values);
                if (newId > 0) {
                    result = TrailerEntry.buildTrailersUriWithMovieId(movieId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIE_WITH_ID:
                String[] selectionArguments = new String[]{uri.getLastPathSegment()};
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        MovieEntry.TABLE_NAME,
                        MovieEntry._ID + " = ? ",
                        selectionArguments);
                /* Trailers and reviews would delete in cascade */
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
