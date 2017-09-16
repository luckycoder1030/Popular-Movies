package com.example.tanyayuferova.popularmovies.utils;

import com.example.tanyayuferova.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanya Yuferova on 9/13/2017.
 */

public class MoviesJsonUtils {

    private static String LIST_RESULTS = "results";
    private static String POSTER_PATH = "poster_path";
    private static String ID = "id";
    private static String ORIGINAL_TITLE = "original_title";
    private static String TITLE = "title";
    private static String OVERVIEW = "overview";
    private static String RELEASED_DATE = "release_date";
    private static String VOTE_AVG = "vote_average";

    /**
     * Reads json string and creates movies list
     * @param json
     * @return
     * @throws JSONException
     */
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
            movie.setTitle(object.getString(TITLE));
            movie.setOverview(object.getString(OVERVIEW));
            movie.setVoteAvg(object.getString(VOTE_AVG));
            movie.setOriginalTitle(object.getString(ORIGINAL_TITLE));
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                movie.setReleasedDate(dateFormat.parse(object.getString(RELEASED_DATE)));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            result.add(movie);
        }

        return result;
    }
}
