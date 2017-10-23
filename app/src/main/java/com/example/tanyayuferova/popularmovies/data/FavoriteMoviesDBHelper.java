package com.example.tanyayuferova.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tanyayuferova.popularmovies.data.MovieContract.*;


/**
 * Created by Tanya Yuferova on 10/21/2017.
 */

/**
 * Works with data base
 */
public class FavoriteMoviesDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 5;


    public FavoriteMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createMovieTable(db);
        createTrailerTable(db);
        createReviewTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TrailerEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + ReviewEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }

    private void createMovieTable(SQLiteDatabase db) {
        final String SQL = "create table " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " integer primary key, " +
                MovieEntry.COLUMN_TITLE + " varchar(500) not null, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " varchar(500), " +
                MovieEntry.COLUMN_OVERVIEW + " text not null, " +
                MovieEntry.COLUMN_POSTER_PATH + " varchar(500) not null, " +
                MovieEntry.COLUMN_RELEASE_DATE + " vdate not null, " +
                MovieEntry.COLUMN_VOTE_AVG + " varchar(15) not null, " +
                MovieEntry.COLUMN_TAGLINE + " varchar(500));";
        db.execSQL(SQL);
    }

    private void createTrailerTable(SQLiteDatabase db) {
        final String SQL = "create table " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry._ID + " integer primary key autoincrement, " +
                TrailerEntry.COLUMN_ID + " varchar(20) not null, " +
                TrailerEntry.COLUMN_NAME + " varchar(500) not null, " +
                TrailerEntry.COLUMN_SITE + " varchar(100) not null, " +
                TrailerEntry.COLUMN_KEY + " varchar(50), " +
                TrailerEntry.COLUMN_LANGUAGE + " varchar(10), " +
                TrailerEntry.COLUMN_MOVIE_ID + " integer not null, " +
                " foreign key (" + TrailerEntry.COLUMN_MOVIE_ID+ ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") on delete cascade); ";
        db.execSQL(SQL);
    }

    private void createReviewTable(SQLiteDatabase db) {
        final String SQL = "create table " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " integer primary key autoincrement, " +
                ReviewEntry.COLUMN_ID + " varchar(20) not null, " +
                ReviewEntry.COLUMN_AUTHOR + " varchar(500) not null, " +
                ReviewEntry.COLUMN_CONTENT + " text not null, " +
                ReviewEntry.COLUMN_URL + " varchar(500), " +
                ReviewEntry.COLUMN_MOVIE_ID + " integer not null, " +
                " foreign key (" + ReviewEntry.COLUMN_MOVIE_ID+ ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") on delete cascade); ";
        db.execSQL(SQL);
    }
}
