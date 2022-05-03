package com.kimoji.whatsapp.cleaner;

import java.util.ArrayList;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

public class TAGS {

	public static ArrayList<WhatsppVideos>mListVideos=new ArrayList<WhatsppVideos>();
	public static ArrayList<WhatsppImages>mListImages=new ArrayList<WhatsppImages>();
	public static ArrayList<WhatsppImages>mListProfileImages=new ArrayList<WhatsppImages>();
	public static ArrayList<WhatsppImages>mListVoice=new ArrayList<WhatsppImages>();
	public static ArrayList<WhatsppImages>mListAudio=new ArrayList<WhatsppImages>();
	public static ArrayList<WhatsppImages>mListWallpaper=new ArrayList<WhatsppImages>();
	
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
	static ImageLoader mImageLoader;
	
	public static boolean itemDeleted=false;
	public static void clearTags()
	{
		mListVideos.clear();
		mListImages.clear();
		mListProfileImages.clear();
		mListVoice.clear();
		mListAudio.clear();
		mListWallpaper.clear();
		
		databaseSize=0;
		videoSize=0;      
		audioSize=0;      
		profileSize=0;    
		imageSize=0;      
		voiceSize=0;
		wallpaperSize=0;
		
		databaseCounter=0;
		videoCounter=0;   
		audioCounter=0;   
		profileCounter=0; 
		imageCounter=0;
		voiceCounter=0;
		wallpaperCounter=0;
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
	
	public static ImageLoader initImageLoader(Context mContext) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                mContext).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(config);
        return mImageLoader;
    }
	
}
