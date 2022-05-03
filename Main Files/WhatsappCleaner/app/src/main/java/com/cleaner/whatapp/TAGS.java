package com.cleaner.whatapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import java.util.ArrayList;

/**
 * Created by Tushar on 7/25/2017.
 */

public class TAGS {
    public static ArrayList<WhatsppVideos>mListVideos=new ArrayList<WhatsppVideos>();
    public static ArrayList<WhatsppImages>mListImages=new ArrayList<WhatsppImages>();
    public static ArrayList<WhatsppImages>mListProfileImages=new ArrayList<WhatsppImages>();
    public static ArrayList<WhatsppImages>mListVoice=new ArrayList<WhatsppImages>();
    public static ArrayList<WhatsppImages>mListAudio=new ArrayList<WhatsppImages>();
    public static ArrayList<WhatsppImages>mListWallpaper=new ArrayList<WhatsppImages>();
    public static ArrayList<WhatsppImages>mListDoc=new ArrayList<WhatsppImages>();

    public static long videoSize=0;
    public static long voiceSize=0;
    public static long profileSize=0;
    public static long imageSize=0;
    public static long audioSize=0;
    public static long wallpaperSize=0;
    public static long databaseSize=0;
    public static long videoCounter=0;
    public static long audioCounter=0;
    public static long voiceCounter=0;
    public static long profileCounter=0;
    public static long imageCounter=0;
    public static long wallpaperCounter=0;
    public static long databaseCounter=0;
    public static long docSize=0;
    public static long docCounter=0;

    public static boolean itemDeleted=false;
    public static void clearTags()
    {
        mListVideos.clear();
        mListImages.clear();
        mListProfileImages.clear();
        mListVoice.clear();
        mListAudio.clear();
        mListWallpaper.clear();
        mListDoc.clear();

        databaseSize=0;
        videoSize=0;
        audioSize=0;
        profileSize=0;
        imageSize=0;
        voiceSize=0;
        wallpaperSize=0;
        docSize =0;

        databaseCounter=0;
        videoCounter=0;
        audioCounter=0;
        profileCounter=0;
        imageCounter=0;
        voiceCounter=0;
        wallpaperCounter=0;
        docCounter = 0;
    }
    public static void refreshGallery(Context m)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
        {
            MediaScannerConnection.scanFile(m, new String[] { Environment.getExternalStorageDirectory().toString() }, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                @Override
                public void onScanCompleted(String arg0, Uri arg1) {
                    // TODO Auto-generated method stub

                }
            });
        }
        else
        {
            m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" +  Environment.getExternalStorageDirectory())));
        }

    }
}
