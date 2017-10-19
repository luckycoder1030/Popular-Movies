package com.example.tanyayuferova.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tanya Yuferova on 10/19/2017.
 */

/**
 * Review entity
 */
public class Review implements Parcelable {

    private String id;
    private String author;
    private String content;
    private String url;

    public Review() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] array = new String[4];
        array[0] = id;
        array[1] = author;
        array[2] = content;
        array[3] = url;
        dest.writeStringArray(array);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    private Review(Parcel parcel) {
        String[] array = new String[4];
        parcel.readStringArray(array);
        id = array[0];
        author = array[1];
        content = array[2];
        url = array[3];
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Review)
            return this.getId().equals(((Review) obj).getId());
        return super.equals(obj);
    }
}
