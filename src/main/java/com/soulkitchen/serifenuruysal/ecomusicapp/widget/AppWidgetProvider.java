package com.soulkitchen.serifenuruysal.ecomusicapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.soulkitchen.serifenuruysal.ecomusicapp.service.BackgroundSoundService;
import com.soulkitchen.serifenuruysal.ecomusicapp.R;

/**
 * Created by S.Nur Uysal on 2019-12-08.
 */

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {


  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId) {

    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.eco_music_app_widget);

    Intent playIntent = new Intent(context, BackgroundSoundService.class);
    playIntent.setAction(BackgroundSoundService.ACTION_PLAY_NEXT);
    PendingIntent playPendingIntent = PendingIntent.getService(context, 0, playIntent, 0);
    views.setOnClickPendingIntent(R.id.btn_play_next, playPendingIntent);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override
  public void onDeleted(Context context, int[] appWidgetIds) {
    // When the user deletes the widget, delete the preference associated with it.
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
  }
}

