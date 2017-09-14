package com.example.tanyayuferova.popularmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tanya Yuferova on 9/13/2017.
 */

public class Movie implements Parcelable {

    String id;
    String posterPath;
    String title;
    String voteAvg;
    Date releasedDate;
    String overview;
    String originalTitle;

    public Movie() {
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(String voteAvg) {
        this.voteAvg = voteAvg;
    }

    public Date getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFullPosterPath() {
        return getFullPosterPath("w185");
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getFullTitle() {
        if (title != null) {
            if(originalTitle != null && !title.equals(originalTitle))
                return title + " (" + originalTitle + ")";
            else return title;
        }
        return originalTitle;
    }

    public Double getDoubleVoteAvg() {
        return Double.parseDouble(voteAvg);
    }

    public String getFullPosterPath(String size) {
        String BASE_URL = "https://image.tmdb.org/t/p/";
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(posterPath)
                .build();
        return uri.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        String[] array = new String[7];
        array[0] = id;
        array[1] = posterPath;
        array[2] = title;
        array[3] = voteAvg;
        array[4] = new SimpleDateFormat().format(releasedDate);
        array[5] = overview;
        array[6] = originalTitle;
        parcel.writeStringArray(array);
    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel parcel) {
        String[] array = new String[7];
        parcel.readStringArray(array);
        id = array[0];
        posterPath = array[1];
        title = array[2];
        voteAvg = array[3];
        releasedDate = null;
        try {
            releasedDate = new SimpleDateFormat().parse(array[4]);
        } catch (ParseException ex){
            ex.printStackTrace();
        }
        overview = array[5];
        originalTitle = array[6];
    }
}
