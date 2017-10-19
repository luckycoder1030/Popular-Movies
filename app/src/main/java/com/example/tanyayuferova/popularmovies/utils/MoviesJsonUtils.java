package com.example.tanyayuferova.popularmovies.utils;

import com.example.tanyayuferova.popularmovies.entities.Movie;
import com.example.tanyayuferova.popularmovies.entities.Review;
import com.example.tanyayuferova.popularmovies.entities.Trailer;

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

    public static String LIST_RESULTS = "results";
    public static String POSTER_PATH = "poster_path";
    public static String ID = "id";
    public static String ORIGINAL_TITLE = "original_title";
    public static String TITLE = "title";
    public static String OVERVIEW = "overview";
    public static String RELEASED_DATE = "release_date";
    public static String VOTE_AVG = "vote_average";
    public static String TAG_LINE = "tagline";
    public static String REVIEW_AUTHOR = "author";
    public static String REVIEW_CONTENT = "content";
    public static String REVIEW_URL = "url";
    public static String VIDEO_LANGUAGE = "iso_639_1";
    public static String VIDEO_KEY = "key";
    public static String VIDEO_NAME = "name";
    public static String VIDEO_SITE = "site";
    public static String VIDEO_TYPE = "type";
    public static String VIDEO_TYPE_TRAILER = "Trailer";

    /**
     * Reads json string and creates movies list
     * @param json
     * @return
     * @throws JSONException
     */
    public static List<Movie> getMoviesDataFromJson(String json) throws JSONException {
        List<Movie> result = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = jsonObject.getJSONArray(LIST_RESULTS);
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

    /**
     * Reads json string and creates reviews list
     * @param json
     * @return
     * @throws JSONException
     */
    public static List<Review> getReviewsDataFromJson(String json) throws JSONException {
        List<Review> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = jsonObject.getJSONArray(LIST_RESULTS);
        for(int i = 0; i <array.length(); i++) {
            JSONObject object  = array.getJSONObject(i);
            Review review = new Review();
            review.setId(object.getString(ID));
            review.setAuthor(object.getString(REVIEW_AUTHOR));
            review.setContent(object.getString(REVIEW_CONTENT));
            review.setUrl(object.getString(REVIEW_URL));
            result.add(review);
        }
        return result;
    }

    /**
     * Reads json string and creates trailers list
     * @param json
     * @return
     * @throws JSONException
     */
    public static List<Trailer> getTrailersDataFromJson(String json) throws JSONException {
        List<Trailer> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = jsonObject.getJSONArray(LIST_RESULTS);
        for(int i = 0; i <array.length(); i++) {
            JSONObject object  = array.getJSONObject(i);
            if(!VIDEO_TYPE_TRAILER.equals(object.getString(VIDEO_TYPE)))
                continue;
            Trailer trailer = new Trailer();
            trailer.setId(object.getString(ID));
            trailer.setKey(object.getString(VIDEO_KEY));
            trailer.setLanguage(object.getString(VIDEO_LANGUAGE));
            trailer.setName(object.getString(VIDEO_NAME));
            trailer.setSite(object.getString(VIDEO_SITE));
            result.add(trailer);
        }
        return result;
    }

    /**
     * Gets specific string value from json string
     * @param json
     * @param valueKey
     * @return
     * @throws JSONException
     */
    public static String getStringValueFromJson(String json, String valueKey) throws JSONException {
        JSONObject object = new JSONObject(json);
        if(object.has(valueKey))
            return object.getString(valueKey);
        return null;
    }
}
