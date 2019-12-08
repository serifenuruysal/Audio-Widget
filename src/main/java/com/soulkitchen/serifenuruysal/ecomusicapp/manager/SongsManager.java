package com.soulkitchen.serifenuruysal.ecomusicapp.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.soulkitchen.serifenuruysal.ecomusicapp.model.AudioModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by S.Nur Uysal on 2019-12-08.
 */
public class SongsManager {

  public SongsManager() {

  }

  public List<AudioModel> getAllAudioFromDevice(final Context context) {

    final List<AudioModel> tempAudioList = new ArrayList<>();

    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//    Uri uri = Media.INTERNAL_CONTENT_URI;
    String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM,
        MediaStore.Audio.ArtistColumns.ARTIST,};
    Cursor c = context.getContentResolver().query(uri,
        projection,
        null,
        null,
        null);

    if (c != null) {
      while (c.moveToNext()) {

        AudioModel audioModel = new AudioModel();
        String path = c.getString(0);
        String album = c.getString(1);
        String artist = c.getString(2);

        String name = path.substring(path.lastIndexOf("/") + 1);

        audioModel.setaName(name);
        audioModel.setaAlbum(album);
        audioModel.setaArtist(artist);
        audioModel.setaPath(path);

        Log.e("Name :" + name, " Album :" + album);
        Log.e("Path :" + path, " Artist :" + artist);

        tempAudioList.add(audioModel);
      }
      c.close();
    }

    return tempAudioList;
  }
}