package com.example.tanyayuferova.popularmovies.utils;

import com.example.tanyayuferova.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanya Yuferova on 9/13/2017.
 */

public class MoviesJsonUtils {

    private static String LIST_RESULTS = "results";
    private static String POSTER_PATH = "poster_path";
    private static String ID = "id";

    public static List<Movie> getMoviesDataFromJson(String json) throws JSONException {
        List<Movie> result = null;

        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = jsonObject.getJSONArray(LIST_RESULTS);
        result = new ArrayList<>();
        for(int i = 0; i <array.length(); i++) {
            JSONObject object  = array.getJSONObject(i);
            Movie movie = new Movie();

            movie.setId(object.getString(ID));
            movie.setPosterPath(object.getString(POSTER_PATH));

            result.add(movie);
        }

        return result;
    }
}
