package com.example.tanyayuferova.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Tanya Yuferova on 9/13/2017.
 */

public class NetworkUtils {

    private static String BASE_URL = "https://api.themoviedb.org/";
    private static String VERSION = "3";
    private static String MOVIE_PARAM = "movie";
    private static String REVIEWS_PARAM = "reviews";
    private static String VIDEOS_PARAM = "videos";
    private static String API_KEY_PARAM = "api_key";
    private static String API_KEY = ""; //FIXME API_KEY
    private static String LANGUAGE_PARAM = "language";
    private static String LANGUAGE_DEFAULT = "en-US";
    private static String PAGE_PARAM = "page";

    /**
     * Movies sorting type
     */
    public enum SortingParam {
        POPULAR("popular"),
        TOP_RATED("top_rated");

        private String id;

        SortingParam(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * Builds url for themoviedb api to get movies list
     * @param sort
     * @param pageNumber
     * @return
     */
    public static URL buildMoviesUrl(SortingParam sort, int pageNumber) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(VERSION)
                .appendPath(MOVIE_PARAM)
                .appendPath(sort.getId())
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_DEFAULT)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(pageNumber))
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Builds url for themoviedb api to get current movie
     * @param movieId current movie id
     * @return
     */
    public static URL buildMovieUrl(String movieId) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(VERSION)
                .appendPath(MOVIE_PARAM)
                .appendPath(movieId)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Builds url for themoviedb api to get reviews list for current movie
     * @param movieId current movie id
     * @param pageNumber
     * @return
     */
    public static URL buildReviewsUrl(String movieId, int pageNumber) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(VERSION)
                .appendPath(MOVIE_PARAM)
                .appendPath(movieId)
                .appendPath(REVIEWS_PARAM)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_DEFAULT)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(pageNumber))
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Builds url for themoviedb api to get videos list for current movie
     * @param movieId current movie id
     * @param pageNumber
     * @return
     */
    public static URL buildVideosUrl(String movieId, int pageNumber) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(VERSION)
                .appendPath(MOVIE_PARAM)
                .appendPath(movieId)
                .appendPath(VIDEOS_PARAM)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_DEFAULT)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(pageNumber))
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
