package com.kimoji.whatsapp.cleaner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kaavu.whatsappcleaner.R;
import com.kimoji.whatsapp.cleaner.WhatsAppVoiceActivity.CounterClass;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WallPaperActivity extends Activity {

	ArrayList<WhatsppImages>mlistWallpaper;
	ArrayList<WhatsppImages>mlistTempWallpaper;
	GridView mGridView;
	ImageView mImageViewDelete;
	ImageView mImageViewCheck;
	TextView mTextViewTitle;
	//	File[] mfile;
	List<File> mFileList;
	List<String> mStringFiles;
	String listType="";
	int wallpaperCounter=0;
	long wallpaperSize=0;
	//	String [] mString;

	int j=0;
	SharePreference mSharePreference;
	ImageLoader mImageLoader;
	InterstitialAd interstitial;
	AdRequest adRequest;
	AdView adView; 
	CounterClass mtimer;
	boolean check=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_wall_paper);
		mlistWallpaper=new ArrayList<WhatsppImages>();
		mlistTempWallpaper=new ArrayList<WhatsppImages>();
		mSharePreference=new SharePreference(this);
		for(int i=0;i<TAGS.mListWallpaper.size();i++)
		{
			TAGS.mListWallpaper.get(i).setSelected(false);
		}
		this.mlistWallpaper=TAGS.mListWallpaper;
		TAGS.itemDeleted=false;
		check=false;
		mImageLoader=TAGS.initImageLoader(this);
		mFileList=new ArrayList<File>();
		mStringFiles=new ArrayList<String>();
		mGridView=(GridView) findViewById(R.id.gridview_wallpaper);
		mImageViewDelete=(ImageView) findViewById(R.id.imageview_wallpaper_delete);
		Drawable mDrawable=Utils.tintImage(this, R.drawable.delete_icon_white, R.color.app_light_color);
		mImageViewDelete.setImageDrawable(mDrawable);
		mImageViewCheck=(ImageView) findViewById(R.id.images_wallpaper);
		mTextViewTitle=(TextView) findViewById(R.id.textview_title);
		mGridView.setAdapter(new ContactAdapterWallpaper(this, mlistWallpaper,mImageLoader,""));


		mImageViewDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				j=0;
				for(int i=0;i<mlistWallpaper.size();i++) 
				{
					boolean m=mlistWallpaper.get(i).isSelected();
					if(m)
					{	
						j++;
					}
				}
				if(j>0)
				{
					deleteItem();
				}
				else
				{
					Utils.SetDiolog(WallPaperActivity.this, "No items selected to delete");
				}

			}
		});
		mImageViewCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

					for(int i=0;i<TAGS.mListWallpaper.size();i++)
					{
						TAGS.mListWallpaper.get(i).setSelected(true);
					}
					if(check)
					{
						check=false;
						WallPaperActivity.this.mlistWallpaper=TAGS.mListWallpaper;
					}
					else
					{
						check=true;
						for(int i=0;i<TAGS.mListWallpaper.size();i++)
						{
							TAGS.mListWallpaper.get(i).setSelected(false);
						}
						WallPaperActivity.this.mlistWallpaper=TAGS.mListWallpaper;
					}
						
					mGridView.setAdapter(new ContactAdapterWallpaper(WallPaperActivity.this, mlistWallpaper,mImageLoader,""));
			}
		});
		adRequest = new AdRequest.Builder().build();
		adView = (AdView)findViewById(R.id.adView_1);
		adView.loadAd(adRequest);

		interstitial = new InterstitialAd(WallPaperActivity.this);
		interstitial.setAdUnitId(getResources().getString(R.string.irres_id));
		interstitial.loadAd(adRequest);

		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				// Call displayInterstitial() function
				displayInterstitial();
			}
		});
		if(mSharePreference.isPuchaseItem())
		{
			adView.setVisibility(View.GONE);
			adView.destroy();


			// Create ad request
			adRequest = new AdRequest.Builder().build();
			interstitial = new InterstitialAd(WallPaperActivity.this);
			interstitial.setAdUnitId("123456");
			// Begin loading your interstitial
			interstitial.loadAd(adRequest);
		}
		else
		{
			mtimer = new CounterClass(100000000, 40000);
			mtimer.start();

		}

	}
	public void setCheckStatus()
	{
		check=true;
		int j=0;
		for(int i=0;i<mlistWallpaper.size();i++) 
		{
			boolean m=mlistWallpaper.get(i).isSelected();
			if(m)
			{	
				j++;
			}
		}
		if(j>0)
		{
			mImageViewCheck.setVisibility(View.VISIBLE);
		}
		else
		{
			mImageViewCheck.setVisibility(View.GONE);
		}

	}
	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		Log.e("call", "call");
		interstitial.loadAd(adRequest);
		if (interstitial.isLoaded()) {
			Log.e("call", "call in");
			interstitial.show();
		}

	}
	public class CounterClass extends CountDownTimer {
		public CounterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {

		}

		@SuppressLint("NewApi")
		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override
		public void onTick(long millisUntilFinished) {

			interstitial.loadAd(adRequest);
			interstitial.show();
		}
	}
	public  void deleteItem() 
	{

		final Dialog mDialog = new Dialog(WallPaperActivity.this);
		LayoutInflater layoutInflater = LayoutInflater.from(WallPaperActivity.this);
		View promptView = layoutInflater.inflate(R.layout.dialog_change1, null);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(promptView);

		TextView mTextView=(TextView) promptView.findViewById(R.id.text_title);
		mTextView.setText("Are you sure you want to delete "+j+" image files?");
		mDialog.findViewById(R.id.logout_textview_yes).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				if(mFileList!=null)
				{
					if(mFileList!=null)
					{
						getSelectedFiles();
						if(mFileList.size()>0)
						{
							for(int i=0;i<mlistTempWallpaper.size();i++)
							{
								mFileList.get(i).delete();
							}
						}
					}
					mlistWallpaper.removeAll(mlistTempWallpaper);
					mGridView.setAdapter(new ContactAdapterWallpaper(WallPaperActivity.this, mlistWallpaper,mImageLoader,""));
					mlistTempWallpaper.clear();
					TAGS.refreshGallery(WallPaperActivity.this);
				}
				TAGS.itemDeleted=true;
				mDialog.dismiss();
			}
		});
		mDialog.findViewById(R.id.logout_textview_no).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}
	public void getSelectedFiles()
	{
		wallpaperCounter=0;
		wallpaperSize=0;
		mFileList=new ArrayList<File>();
		mStringFiles=new ArrayList<String>();
		for(int i=0;i<mlistWallpaper.size();i++) 
		{
			boolean m=mlistWallpaper.get(i).isSelected();
			if(m)
			{	
				File mfile=new File(mlistWallpaper.get(i).getPath());
				mFileList.add(mfile);
				wallpaperCounter++;
				wallpaperSize+=mfile.length();
				mStringFiles.add(mfile.getAbsolutePath());
				mlistTempWallpaper.add(mlistWallpaper.get(i));
				//					mString[i]=mfile.getAbsolutePath();
			}
		}
		Log.e("size", "size "+mFileList.size())	;
	}

	@Override
	public void onBackPressed() {

		if(wallpaperCounter>0)
		{
			wallpaperSize/=1024;
			TAGS.wallpaperCounter-=wallpaperCounter;
			TAGS.wallpaperSize-=wallpaperSize;
		}
		if(TAGS.wallpaperSize<0)
		{
			TAGS.wallpaperSize=0;
		}
		Intent mIntent=new Intent(WallPaperActivity.this,CleanerActivity.class);
		startActivity(mIntent);

		super.onBackPressed();
	}

	//	public void refreshGallery()
	//	{
	//		String[] projection = { MediaStore.Images.Media._ID };
	//
	//		// Match on the file path
	//		String selection = MediaStore.Images.Media.DATA + " = ?";
	////		String[] selectionArgs = new String[] { mFileList.getAbsolutePath() };
	//
	//		// Query for the ID of the media matching the file path
	//		Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	//		ContentResolver contentResolver = getContentResolver();
	//		Cursor c = contentResolver.query(queryUri, projection, selection, mString, null);
	//		if (c.moveToFirst()) {
	//		    // We found the ID. Deleting the item via the content provider will also remove the file
	//		    long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
	//		    Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
	//		    contentResolver.delete(deleteUri, null, null);
	//		} else {
	//		    // File not found in media store DB
	//		}
	//		c.close();
	//	}
}
