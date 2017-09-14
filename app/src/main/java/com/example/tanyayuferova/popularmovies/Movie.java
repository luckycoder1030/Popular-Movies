package com.example.tanyayuferova.popularmovies;

import android.net.Uri;

/**
 * Created by Tanya Yuferova on 9/13/2017.
 */

public class Movie {

    String id;
    String posterPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getFullPosterPath() {
        String BASE_URL = "https://image.tmdb.org/t/p/";
        String DEFAULT_SIZE = "w500";
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(DEFAULT_SIZE)
                .appendEncodedPath(posterPath)
                .build();
        return uri.toString();
    }
}
