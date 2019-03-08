package com.zflabs.marveldatabase.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class FavoritesService extends IntentService {

    public static final String MODIFY_LIST = "com.zflabs.marveldatabase.modifylist";

    public FavoritesService() {
        super("FavoritesService");
    }

    public FavoritesService(String name) {
        super(name);
    }

    public static void startActionListModified(Context context) {
        Intent intent = new Intent(context, FavoritesService.class);
        intent.setAction(MODIFY_LIST);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (MODIFY_LIST.equals(action)) {
                handleListModified();
            }
        }
    }

    private void handleListModified() {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(this, FavoriteWidget.class));
        FavoriteWidget.updateAppWidgets(this, manager, appWidgetIds);
    }


}