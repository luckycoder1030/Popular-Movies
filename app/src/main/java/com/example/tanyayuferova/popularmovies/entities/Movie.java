package com.example.tanyayuferova.popularmovies.entities;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tanya Yuferova on 9/13/2017.
 */

/**
 * Movie entity
 */
public class Movie implements Parcelable {

    private String id;
    private String posterPath;
    private String title;
    private String voteAvg;
    private Date releasedDate;
    private String overview;
    private String originalTitle;
    private String tagline;
    private boolean isFavorite = false;
    private List<Trailer> trailers;
    private List<Review> reviews;

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

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    /**
     * @return title and the original title if they differ, else title only
     */
    public String getFullTitle() {
        if (title != null) {
            if(originalTitle != null && !title.equals(originalTitle))
                return title + " (" + originalTitle + ")";
            else return title;
        }
        return originalTitle;
    }

    /**
     * @return title and and year of release
     */
    public String getDescription() {
        return String.format("%1$s (%2$tY)", title, releasedDate);
    }

    public Double getDoubleVoteAvg() {
        return Double.parseDouble(voteAvg);
    }

    /**
     * Builds poster url string
     * @param size poster size
     * @return
     */
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
        String[] array = new String[9];
        array[0] = id;
        array[1] = posterPath;
        array[2] = title;
        array[3] = voteAvg;
        array[4] = new SimpleDateFormat().format(releasedDate);
        array[5] = overview;
        array[6] = originalTitle;
        array[7] = tagline;
        array[8] = String.valueOf(isFavorite);
        parcel.writeStringArray(array);
        parcel.writeList(trailers);
        parcel.writeList(reviews);
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
        String[] array = new String[9];
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
        tagline = array[7];
        isFavorite = Boolean.parseBoolean(array[8]);

        trailers = new ArrayList<>();
        parcel.readList(trailers, Trailer.class.getClassLoader());

        reviews = new ArrayList<>();
        parcel.readList(reviews, Review.class.getClassLoader());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Movie)
            return this.getId().equals(((Movie) obj).getId());
        return super.equals(obj);
    }
}
