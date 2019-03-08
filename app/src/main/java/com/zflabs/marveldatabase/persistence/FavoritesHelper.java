package com.zflabs.marveldatabase.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FavoritesHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favoriteCharacters.db";

    public static final int DATABASE_VERSION = 5;

    public FavoritesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " +
                FavoritesContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoritesContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_ID + " INTEGER , " +
                FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_NAME + " TEXT NOT NULL, " +
                FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_IMAGE_PATH + " TEXT NOT NULL, " +
                FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_IMAGE_EXT + " TEXT NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
