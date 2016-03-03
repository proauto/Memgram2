package com.creativestudioaq.honggyu.Memkey;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Creativestudio AQ on 2015-10-30.
 */
public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {


        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);


        Intent test = new Intent(context, WriteActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, test, PendingIntent.FLAG_UPDATE_CURRENT);

        updateViews.setOnClickPendingIntent(R.id.mLayout, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, updateViews);

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
}
