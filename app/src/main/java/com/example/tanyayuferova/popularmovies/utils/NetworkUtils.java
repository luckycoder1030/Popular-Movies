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

    private static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static String API_KEY_PARAM = "api_key";
    private static String API_KEY = "0de0b107ebea0f1fa487a5afa259cac1"; //todo API_KEY
    private static String LANGUAGE_PARAM = "language";
    private static String LANGUAGE_DEFAULT = "en-US";
    private static String PAGE_PARAM = "page";

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

    public static URL buildUrl(SortingParam sort) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sort.getId())
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_DEFAULT)
                .appendQueryParameter(PAGE_PARAM, "1")
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
