package com.soulkitchen.serifenuruysal.ecomusicapp.widget;

import static com.soulkitchen.serifenuruysal.ecomusicapp.activity.MainActivity.REQUEST_PERMISSION;

import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.soulkitchen.serifenuruysal.ecomusicapp.R;

/**
 * Created by S.Nur Uysal on 2019-12-08.
 */

public class EcoMusicAppWidgetConfigureActivity extends Activity {

  int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
  View.OnClickListener mOnClickListener = new View.OnClickListener() {
    public void onClick(View v) {
      final Context context = EcoMusicAppWidgetConfigureActivity.this;

      // It is the responsibility of the configuration activity to update the app widget
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
      AppWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

      // Make sure we pass back the original appWidgetId
      Intent resultValue = new Intent();
      resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
      setResult(RESULT_OK, resultValue);
      finish();
    }
  };

  public EcoMusicAppWidgetConfigureActivity() {
    super();
  }


  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);

    // Set the result to CANCELED.  This will cause the widget host to cancel
    // out of the widget placement if the user presses the back button.
    setResult(RESULT_CANCELED);

    setContentView(R.layout.eco_music_app_widget_configure);
    findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

    // Find the widget id from the intent.
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      mAppWidgetId = extras.getInt(
          AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish();
      return;
    }

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat
          .requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
              REQUEST_PERMISSION);
    }

  }
}

