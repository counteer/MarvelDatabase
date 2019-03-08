package com.zflabs.marveldatabase.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.zflabs.marveldatabase.R;
import com.zflabs.marveldatabase.data.Character;
import com.zflabs.marveldatabase.data.Thumbnail;
import com.zflabs.marveldatabase.persistence.FavoritesContract;

import java.util.Random;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Character character) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setTextViewText(R.id.appwidget_text, character.getName());
        views.setTextViewTextSize(R.id.appwidget_text, COMPLEX_UNIT_SP, 24);
        Picasso.with(context).load(character.getThumbnailUrl()).into(views, R.id.widget_character, new int[] {appWidgetId});
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setTextViewText(R.id.appwidget_text, context.getString(R.string.widget_help));
        views.setTextViewTextSize(R.id.appwidget_text, COMPLEX_UNIT_SP, 12);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Cursor result = context.getContentResolver().query(FavoritesContract.BASE_CONTENT_URI,
                new String[]{FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_ID,
                },
                null,
                null,
                FavoritesContract.FavoriteEntry._ID);
        int count = result.getCount();
        if(count != 0) {
            int rnd = new Random().nextInt(result.getCount());
            result.moveToPosition(rnd);
            Character character = new Character();
            character.setId(result.getInt(0));
            character.setName(result.getString(1));
            Thumbnail thumbnail = new Thumbnail(result.getString(2), result.getString(3));
            character.setThumbnail(thumbnail);
            result.getInt(0);
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, character);
            }
        } else {
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateAppWidgets(Context context, AppWidgetManager manager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setTextViewText(R.id.appwidget_text, "debugging");

        Cursor result = context.getContentResolver().query(FavoritesContract.BASE_CONTENT_URI,
                new String[]{FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_ID,
                },
                null,
                null,
                FavoritesContract.FavoriteEntry._ID);
        int count = result.getCount();
        if(count != 0) {
            int rnd = new Random().nextInt(result.getCount());
            result.moveToPosition(1);
            Character character = new Character();
            character.setId(result.getInt(0));
            character.setName(result.getString(1));
            Thumbnail thumbnail = new Thumbnail(result.getString(2), result.getString(3));
            character.setThumbnail(thumbnail);
            result.getInt(0);
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, manager, appWidgetId, character);
            }
        }
    }
}

