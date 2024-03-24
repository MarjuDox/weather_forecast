package com.example.demo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class AppWidgetTemperature extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {

            // create intent to open activity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Onclick event
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.demo_temperature);
            views.setOnClickPendingIntent(R.id.widget_temperature, pendingIntent);

            // Widget update
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
