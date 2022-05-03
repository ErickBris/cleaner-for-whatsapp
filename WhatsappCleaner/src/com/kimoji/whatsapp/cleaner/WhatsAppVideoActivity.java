package com.kimoji.whatsapp.cleaner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kaavu.whatsappcleaner.R;
import com.kimoji.whatsapp.cleaner.CleanerActivity.CounterClass;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
public class WhatsAppVideoActivity extends Activity {

	ArrayList<WhatsppVideos>mlist;
	ArrayList<WhatsppVideos>mlistTemp;
	GridView mGridView;
	ImageView mImageView;
	ImageView mImageViewCheck;
	List<File> mFileList;
	//	LinearLayout mLinearLayoutDelete;
	int videoCounter=0;
	int videoSize=0;
	int j=0;
	SharePreference mSharePreference;
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

		setContentView(R.layout.activity_whats_app_video);

		TAGS.itemDeleted=false;
		check=false;
		mlist=new ArrayList<WhatsppVideos>();
		mlistTemp=new ArrayList<WhatsppVideos>();


		for(int i=0;i<TAGS.mListVideos.size();i++)
		{
			TAGS.mListVideos.get(i).setSelected(false);
		}
		mlist=TAGS.mListVideos;
		mFileList=new ArrayList<File>();
		mSharePreference=new SharePreference(this);
		mGridView=(GridView) findViewById(R.id.gridview_videos);
		mImageViewCheck=(ImageView) findViewById(R.id.images_videos);
		mGridView.setAdapter(new WhatsAppVideoAdapter(this, mlist, ""));
		
		
		//		mGridView.setOnItemClickListener(new OnItemClickListener() {
		//
		//			@Override
		//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		//					long arg3) 
		//			{
		//				
		//				Intent tostart = new Intent(Intent.ACTION_VIEW);
		//				tostart.setDataAndType(Uri.parse(mlist.get(arg2).getPath()), "video/*");
		//				 if (tostart.resolveActivity(getPackageManager()) != null) {
		//				        startActivity(tostart);
		//				    }
		//				
		//			}
		//		});
		mImageView=(ImageView) findViewById(R.id.imageview_delete_video);
		Drawable mDrawable=Utils.tintImage(this, R.drawable.delete_icon_white, R.color.app_light_color);
		mImageView.setImageDrawable(mDrawable);
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				j=0;
				for(int i=0;i<mlist.size();i++) 
				{
					boolean m=mlist.get(i).isSelected();
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
					Utils.SetDiolog(WhatsAppVideoActivity.this, "No items selected to delete");
				}
			}
		});
		adRequest = new AdRequest.Builder().build();
		adView = (AdView)findViewById(R.id.adView_1);
		adView.loadAd(adRequest);

		interstitial = new InterstitialAd(WhatsAppVideoActivity.this);
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
			interstitial = new InterstitialAd(WhatsAppVideoActivity.this);
			interstitial.setAdUnitId("123456");
			// Begin loading your interstitial
			interstitial.loadAd(adRequest);
		}
		else
		{
			mtimer = new CounterClass(100000000, 40000);
			mtimer.start();

		}
		mImageViewCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
					for(int i=0;i<TAGS.mListVideos.size();i++)
					{
						TAGS.mListVideos.get(i).setSelected(true);
					}
					Log.e("value", "value first "+check);
					if(check)
					{
						Log.e("value", "value if "+check);
						check=false;
						WhatsAppVideoActivity.this.mlist=TAGS.mListVideos;
					}
					else
					{
						Log.e("value", "value else "+check);
						check=true;
						for(int i=0;i<TAGS.mListVideos.size();i++)
						{
							TAGS.mListVideos.get(i).setSelected(false);
						}
						WhatsAppVideoActivity.this.mlist=TAGS.mListVideos;
					}
					mGridView.setAdapter(new WhatsAppVideoAdapter(WhatsAppVideoActivity.this, mlist, ""));
			}
		});

	}
	public void setCheckStatus()
	{
		check=true;
		int j=0;
		for(int i=0;i<mlist.size();i++) 
		{
			boolean m=mlist.get(i).isSelected();
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

		final Dialog mDialog = new Dialog(WhatsAppVideoActivity.this);
		LayoutInflater layoutInflater = LayoutInflater.from(WhatsAppVideoActivity.this);
		View promptView = layoutInflater.inflate(R.layout.dialog_change1, null);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(promptView);

		TextView mTextView=(TextView) promptView.findViewById(R.id.text_title);
		mTextView.setText("Are you sure you want to delete "+j+" video files?");

		mDialog.findViewById(R.id.logout_textview_yes).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				if(mFileList!=null)
				{
					getSelectedFiles();
					if(mFileList.size()>0)
					{
						for(int i=0;i<mFileList.size();i++)
						{
							mFileList.get(i).delete();
						}
					}
				}
				mlist.removeAll(mlistTemp);
				mGridView.setAdapter(new WhatsAppVideoAdapter(WhatsAppVideoActivity.this, mlist, ""));
				mlistTemp.clear();
				TAGS.refreshGallery(WhatsAppVideoActivity.this);
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
		videoCounter=0;
		videoSize=0;
		mFileList=new ArrayList<File>();
		for(int i=0;i<mlist.size();i++) 
		{
			boolean m=mlist.get(i).isSelected();
			if(m)
			{	
				File mfile=new File(mlist.get(i).getPath());
				mFileList.add(mfile);
				videoCounter++;
				videoSize+=mfile.length();
				mlistTemp.add(mlist.get(i));
			}
		}
	}
	@Override
	public void onBackPressed() {

		if(videoCounter>0)
		{
			videoSize/=1024;
			TAGS.videoCounter-=videoCounter;
			TAGS.videoSize-=videoSize;
		}
		if(TAGS.videoSize<0)
		{
			TAGS.videoSize=0;
		}
		Intent mIntent=new Intent(WhatsAppVideoActivity.this,CleanerActivity.class);
		startActivity(mIntent);

		super.onBackPressed();
	}
}