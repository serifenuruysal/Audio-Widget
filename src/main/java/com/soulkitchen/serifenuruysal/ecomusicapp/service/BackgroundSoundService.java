package com.soulkitchen.serifenuruysal.ecomusicapp.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import com.soulkitchen.serifenuruysal.ecomusicapp.R;
import com.soulkitchen.serifenuruysal.ecomusicapp.manager.SongsManager;
import com.soulkitchen.serifenuruysal.ecomusicapp.model.AudioModel;
import com.soulkitchen.serifenuruysal.ecomusicapp.widget.AppWidgetProvider;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by S.Nur Uysal on 2019-12-08.
 */
public class BackgroundSoundService extends Service
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {

  private static final String TAG = BackgroundSoundService.class.getSimpleName();
  public static final String ACTION_PLAY_NEXT =
      "com.soulkitchen.serifenuruysal.ecomusicapp.action.PLAY_NEXT";
  private MediaPlayer player;
  private SongsManager songsManager;
  private List<AudioModel> playlist;
  private CountDownTimer songDurationTimer;


  public IBinder onBind(Intent arg0) {

    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "onCreate");
    songsManager = new SongsManager();
    player = new MediaPlayer();
    player.setOnErrorListener(this);
    player.setOnPreparedListener(this);
    player.setOnCompletionListener(this);
    startPlayRandomSong();


  }

  public AudioModel getRandomSong() {
    Random rand = new Random();
    return playlist.get(rand.nextInt(playlist.size()));
  }

  private void startPlayRandomSong() {
    Log.d(TAG, "startPlayRandomSong");
    if (playlist == null) {
      playlist = songsManager.getAllAudioFromDevice(this);
    }
    RemoteViews view = new RemoteViews(getPackageName(), R.layout.eco_music_app_widget);

    if (player.isPlaying()) {
      view.setImageViewResource(R.id.btn_play_next, R.drawable.ic_play_button);
      player.pause();
      if (songDurationTimer != null) {
        songDurationTimer.cancel();
      }

    } else {
      try {
        view.setImageViewResource(R.id.btn_play_next, R.drawable.ic_pause_circular_button);
        AudioModel song = getRandomSong();
        player.reset();
        player.setDataSource(song.getaPath());

        Log.d(TAG, "play song name: " + song.getaName());
        view.setTextViewText(R.id.id_song_name, song.getaName());

        player.prepare();
      } catch (IOException e) {
        view.setImageViewResource(R.id.btn_play_next, R.drawable.ic_play_button);
        e.printStackTrace();
      }
      player.start();
      startSongDuration();
    }

    ComponentName theWidget = new ComponentName(this, AppWidgetProvider.class);
    AppWidgetManager manager = AppWidgetManager.getInstance(this);
    manager.updateAppWidget(theWidget, view);

  }

  private void startSongDuration() {
    if (songDurationTimer != null) {
      songDurationTimer.cancel();
    }
    songDurationTimer = new CountDownTimer(player.getDuration(), 1000) {

      public void onTick(long millisUntilFinished) {
        updatePlayer(millisUntilFinished);
      }

      public void onFinish() {
        updatePlayer(0);
      }
    };
    songDurationTimer.start();
  }


  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, "onStartCommand");
    if (intent.getAction().equals(ACTION_PLAY_NEXT)) {
      startPlayRandomSong();

    }

    return Service.START_STICKY;
  }

  public void onPrepared(MediaPlayer player) {
    Log.d(TAG, "onPrepared");
    player.start();
  }


  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    Log.d(TAG, "onError");
    player.seekTo(0);
    startPlayRandomSong();
    return true;
  }

  public void onCompletion(MediaPlayer _mediaPlayer) {
    Log.d(TAG, "onCompletion");
    startPlayRandomSong();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy");
    if (player != null) {
      player.release();
    }
  }

//  private Runnable mUpdateTime = new Runnable() {
//    public void run() {
//      int currentDuration;
//      if (player.isPlaying()) {
//        currentDuration = player.getCurrentPosition();
//        updatePlayer(currentDuration);
//      }
//    }
//  };


  private void updatePlayer(long currentDuration) {
    RemoteViews view = new RemoteViews(getPackageName(), R.layout.eco_music_app_widget);
    view.setTextViewText(R.id.tv_elapsed_time, "" + milliSecondsToTimer(currentDuration));
    ComponentName theWidget = new ComponentName(this, AppWidgetProvider.class);
    AppWidgetManager manager = AppWidgetManager.getInstance(this);
    manager.updateAppWidget(theWidget, view);
  }

  /**
   * Function to convert milliseconds time to Timer Format Hours:Minutes:Seconds
   */
  public String milliSecondsToTimer(long milliseconds) {
    String finalTimerString = "";
    String secondsString = "";

    // Convert total duration into time
    int hours = (int) (milliseconds / (1000 * 60 * 60));
    int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
    int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
    // Add hours if there
    if (hours > 0) {
      finalTimerString = hours + ":";
    }

    // Prepending 0 to seconds if it is one digit
    if (seconds < 10) {
      secondsString = "0" + seconds;
    } else {
      secondsString = "" + seconds;
    }

    finalTimerString = finalTimerString + minutes + ":" + secondsString;

    // return timer string
    return finalTimerString;
  }
}