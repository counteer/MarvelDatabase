package com.zflabs.marveldatabase.persistence;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {

    public static final String AUTHORITY = "com.zflabs.marveldatabase";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final String PATH_FAVORITES = "favorites";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

    public static final class FavoriteEntry implements BaseColumns {


        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_CHARACTER_ID = "character_id";
        public static final String COLUMN_CHARACTER_NAME = "name";
        public static final String COLUMN_CHARACTER_IMAGE_PATH = "image_url";
        public static final String COLUMN_CHARACTER_IMAGE_EXT = "image_ext";
    }
}
