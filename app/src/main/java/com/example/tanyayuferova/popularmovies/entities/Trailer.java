package com.example.tanyayuferova.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tanya Yuferova on 10/19/2017.
 */

/**
 * Trailer entity
 */
public class Trailer implements Parcelable {

    private String id;
    private String language;
    private String key;
    private String name;
    private String site;

    public Trailer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] array = new String[5];
        array[0] = id;
        array[1] = language;
        array[2] = key;
        array[3] = name;
        array[4] = site;
        dest.writeStringArray(array);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    private Trailer(Parcel parcel) {
        String[] array = new String[5];
        parcel.readStringArray(array);
        id = array[0];
        language = array[1];
        key = array[2];
        name = array[3];
        site = array[4];
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Trailer)
            return this.getId().equals(((Trailer) obj).getId());
        return super.equals(obj);
    }
}
