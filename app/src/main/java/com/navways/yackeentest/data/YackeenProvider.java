package com.navways.yackeentest.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by husse on 26/12/2017.
 */

public class YackeenProvider extends ContentProvider {
    private YacheenDBHelper dbHelper;
    public static final int STORES_ID = 100;
    private static final int WEATHER_ID = 200;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Assigning the Table.
    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI
                (YacheenDBHelper.AUTHORITY, YacheenDBHelper.TABLE_STORIES, STORES_ID);
        uriMatcher.addURI
                (YacheenDBHelper.AUTHORITY, YacheenDBHelper.TABLE_WEATHER, WEATHER_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new YacheenDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case STORES_ID:
                retCursor =  db.query(YacheenDBHelper.TABLE_STORIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case WEATHER_ID:
                retCursor = db.query(YacheenDBHelper.TABLE_WEATHER,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {

            case STORES_ID:
                long storiesRowId = db.insert(YacheenDBHelper.TABLE_STORIES, null, values);
                if ( storiesRowId > 0 )
                    returnUri = ContentUris.withAppendedId(YacheenDBHelper.CONTENT_URI_STORIES, storiesRowId );
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            case WEATHER_ID:
                long weatherRowId = db.insert(YacheenDBHelper.TABLE_WEATHER, null, values);
                if ( weatherRowId  > 0 )
                    returnUri = ContentUris.withAppendedId(YacheenDBHelper.CONTENT_URI_WEATHER, weatherRowId);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksDeleted;

        switch (match) {

            case STORES_ID:
                tasksDeleted = db.delete(YacheenDBHelper.TABLE_STORIES,
                        null,null);
                break;
            case WEATHER_ID:
                tasksDeleted = db.delete(YacheenDBHelper.TABLE_WEATHER,
                        null,null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
