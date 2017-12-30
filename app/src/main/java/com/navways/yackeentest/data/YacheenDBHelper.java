package com.navways.yackeentest.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

/**
 * Created by husse on 25/12/2017.
 */

public class YacheenDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "yacheen.db";
    private static final int DATABASE_VERSION = 12;

    public static final String TABLE_STORIES= "stories";
    public static final String COLUMN_STORIES_TITLE = "story_title";
    public static final String COLUMN_STORIES_DATE= "story_published_date";
    public static final String COLUMN_STORIES_IMG_URL= "story_img_url";

    public static final String TABLE_WEATHER= "weather";
    public static final String COLUMN_WEATHER_DATE= "weather_date";
    public static final String COLUMN_WEATHER_TEMP_MIN= "weather_temp_min";
    public static final String COLUMN_WEATHER_TEMP_MAX= "weather_temp_max";


    public static final String[] STORIES_COLUMNS = new String[]{
            YacheenDBHelper.COLUMN_STORIES_TITLE,
            YacheenDBHelper.COLUMN_STORIES_DATE,
            YacheenDBHelper.COLUMN_STORIES_IMG_URL};
    public static final String[] WEATHER_COLUMNS = new String[]{
            YacheenDBHelper.COLUMN_WEATHER_DATE,
            YacheenDBHelper.COLUMN_WEATHER_TEMP_MIN,
            YacheenDBHelper.COLUMN_WEATHER_TEMP_MAX};

    private static final String CREATE_TABLE_STORIES=
            "CREATE TABLE " + TABLE_STORIES+ " (" +
                    COLUMN_STORIES_TITLE+ " TEXT," +
                    COLUMN_STORIES_DATE+ " TEXT," +
                    COLUMN_STORIES_IMG_URL+ " TEXT " +
                    ");";
    private static final String CREATE_TABLE_WEATHER=
            "CREATE TABLE " + TABLE_WEATHER+ " (" +
                    COLUMN_WEATHER_DATE+ " TEXT," +
                    COLUMN_WEATHER_TEMP_MIN+ " TEXT ," +
                    COLUMN_WEATHER_TEMP_MAX+ " TEXT " +
                    ");";

    public static final String AUTHORITY = "com.navways.yackeentest";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static Uri CONTENT_URI_STORIES =
            BASE_CONTENT_URI.buildUpon().appendPath(TABLE_STORIES).build();

    public static Uri CONTENT_URI_WEATHER=
            BASE_CONTENT_URI.buildUpon().appendPath(TABLE_WEATHER).build();

    public YacheenDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STORIES);
        db.execSQL(CREATE_TABLE_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        onCreate(db);
    }
}
