package com.zflabs.marveldatabase.persistence;

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

public class FavoritesProvider extends ContentProvider {

    private FavoritesHelper dbHelper;

    public static final int FAVORITES = 100;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.zflabs.marveldatabase", null, FAVORITES);
        return uriMatcher;
    }

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new FavoritesHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        Cursor result;
        switch (match) {
            case FAVORITES:
                result = db.query(FavoritesContract.FavoriteEntry.TABLE_NAME,
                        new String[]{FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_ID,
                                FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_NAME,
                                FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_IMAGE_PATH,
                                FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_IMAGE_EXT},
                        s,
                        null,
                        null,
                        null,
                        FavoritesContract.FavoriteEntry._ID
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITES:
                long id = db.insert(FavoritesContract.FavoriteEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoritesContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int deleted;
        switch (match) {
            case FAVORITES:
                deleted = db.delete(FavoritesContract.FavoriteEntry.TABLE_NAME, s, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
