package com.kimoji.whatsapp.cleaner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kaavu.whatsappcleaner.R;
import com.kimoji.whatsapp.cleaner.CleanerActivity.CounterClass;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WhatsAppImagesActivity extends Activity {

	ArrayList<WhatsppImages>mlist;
	ArrayList<WhatsppImages>mTemp;
	ArrayList<WhatsppImages>mTempProfile;
	ArrayList<WhatsppImages>mlistProfile;
	GridView mGridView;
	ImageView mImageViewCheck;

	//	LinearLayout mLinearLayoutDelete;
	TextView mTextViewTitle;
	//	File[] mfile;
	List<File> mFileList;
	String listType="";
	int imagecounter=0;
	long imagesize=0;

	int profileimagecounter=0;
	long profileimagesize=0;
	ImageView mImageView;
	CleanerActivity mCleanerActivity;
	//	int[] selected;
	ContactAdapter1 mContactAdapter;
	ImageLoader mImageLoader;
	String [] mString;
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

		setContentView(R.layout.activity_whats_app_images2);
		mCleanerActivity=new CleanerActivity();

		mSharePreference=new SharePreference(this);
		TAGS.itemDeleted=false;

		mImageLoader=TAGS.initImageLoader(this);

		mlist=new ArrayList<WhatsppImages>();
		mTemp=new ArrayList<WhatsppImages>();
		mString=new String[TAGS.mListImages.size()];
		mlistProfile=new ArrayList<WhatsppImages>();
		mTempProfile=new ArrayList<WhatsppImages>();

		//		final ArrayList<WhatsppImages>mList=(ArrayList<WhatsppImages>) getIntent().getSerializableExtra("IMAGES");
		listType=getIntent().getStringExtra("IMAGES");

		for(int i=0;i<TAGS.mListImages.size();i++)
		{
			TAGS.mListImages.get(i).setSelected(false);
		}
		for(int i=0;i<TAGS.mListProfileImages.size();i++)
		{
			TAGS.mListProfileImages.get(i).setSelected(false);
		}
		check=false;
		this.mlist=TAGS.mListImages;
		this.mlistProfile=TAGS.mListProfileImages;
		mContactAdapter=new ContactAdapter1(this, mlist,mImageLoader, "");

		mFileList=new ArrayList<File>();
		mGridView=(GridView) findViewById(R.id.gridview);
		mImageView=(ImageView) findViewById(R.id.imageview_delete);
		mImageViewCheck=(ImageView) findViewById(R.id.images_all);
		Drawable mDrawable=Utils.tintImage(this, R.drawable.delete_icon_white, R.color.app_light_color);
		mImageView.setImageDrawable(mDrawable);
		//		mLinearLayoutDelete=(LinearLayout) findViewById(R.id.layout_delete);
		mTextViewTitle=(TextView) findViewById(R.id.textview_title);


		if(listType.equals("images"))
		{
			mTextViewTitle.setText("WhatsApp Images");
			mGridView.setAdapter(new ContactAdapter1(this, mlist,mImageLoader, ""));
		}
		else
		{
			mTextViewTitle.setText("WhatsApp Profile Images");
			mGridView.setAdapter(new ContactAdapter1(this, mlistProfile, mImageLoader,""));
		}

		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				j=0;
				if(listType.equals("images"))
				{
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
						Utils.SetDiolog(WhatsAppImagesActivity.this, "No items selected to delete");
					}
				}
				else
				{
					for(int i=0;i<mlistProfile.size();i++) 
					{
						boolean m=mlistProfile.get(i).isSelected();
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
						Utils.SetDiolog(WhatsAppImagesActivity.this, "No items selected to delete");
					}
				}

				//			
			}
		});
		adRequest = new AdRequest.Builder().build();
		adView = (AdView)findViewById(R.id.adView_1);
		adView.loadAd(adRequest);

		interstitial = new InterstitialAd(WhatsAppImagesActivity.this);
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
			interstitial = new InterstitialAd(WhatsAppImagesActivity.this);
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
			public void onClick(View v) {
				
				if(listType.equals("images"))
				{
					for(int i=0;i<TAGS.mListImages.size();i++)
					{
						TAGS.mListImages.get(i).setSelected(true);
					}
					if(check)
					{
						check=false;
						WhatsAppImagesActivity.this.mlist=TAGS.mListImages;
					}
					else
					{
						check=true;
						for(int i=0;i<TAGS.mListImages.size();i++)
						{
							TAGS.mListImages.get(i).setSelected(false);
						}
						WhatsAppImagesActivity.this.mlist=TAGS.mListImages;
					}
					mGridView.setAdapter(new ContactAdapter1(WhatsAppImagesActivity.this, mlist,mImageLoader, ""));
				}
				else
				{
					for(int i=0;i<TAGS.mListProfileImages.size();i++)
					{
						TAGS.mListProfileImages.get(i).setSelected(true);
					}
					if(check)
					{
						check=false;
						WhatsAppImagesActivity.this.mlistProfile=TAGS.mListProfileImages;
					}
					else
					{
						check=true;
						for(int i=0;i<TAGS.mListProfileImages.size();i++)
						{
							TAGS.mListProfileImages.get(i).setSelected(false);
						}
						WhatsAppImagesActivity.this.mlistProfile=TAGS.mListProfileImages;
					}
					mGridView.setAdapter(new ContactAdapter1(WhatsAppImagesActivity.this, mlistProfile,mImageLoader, ""));
				}
			}
		});
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
	public void setCheckStatus()
	{
		check=true;
		int j=0;
		if(listType.equals("images"))
		{
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
		else
		{
			for(int i=0;i<mlistProfile.size();i++) 
			{
				boolean m=mlistProfile.get(i).isSelected();
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

		final Dialog mDialog = new Dialog(WhatsAppImagesActivity.this);
		LayoutInflater layoutInflater = LayoutInflater.from(WhatsAppImagesActivity.this);
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
					getSelectedFiles();
					if(mFileList.size()>0)
					{
						for(int i=0;i<mFileList.size();i++)
						{
							mFileList.get(i).delete();
						}
					}
				}

				if(listType.equals("images"))
				{
					mlist.removeAll(mTemp);
					mGridView.setAdapter(new ContactAdapter1(WhatsAppImagesActivity.this, mlist,mImageLoader, ""));
					mTemp.clear();
				}
				else
				{
					mlistProfile.removeAll(mTempProfile);
					mGridView.setAdapter(new ContactAdapter1(WhatsAppImagesActivity.this, mlistProfile, mImageLoader,""));
					mTempProfile.clear();
				}
				TAGS.refreshGallery(WhatsAppImagesActivity.this);
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
		profileimagecounter=0;
		profileimagesize=0;
		imagecounter=0;
		imagesize=0;
		mFileList=new ArrayList<File>();
		//		selected=new int[mlist.size()];
		if(listType.equals("images"))
		{
			for(int i=0;i<mlist.size();i++) 
			{
				boolean m=mlist.get(i).isSelected();
				if(m)
				{	

					File mfile=new File(mlist.get(i).getPath());
					imagecounter++;
					imagesize+=mfile.length();
					//					selected[i]=i;
					mFileList.add(mfile);
					mTemp.add(mlist.get(i));
					mString[i]=mfile.getAbsolutePath();
				}
			}
		}
		else
		{
			for(int i=0;i<mlistProfile.size();i++) 
			{
				boolean m=mlistProfile.get(i).isSelected();
				if(m)
				{	
					File mfile=new File(mlistProfile.get(i).getPath());
					mFileList.add(mfile);
					profileimagecounter++;
					profileimagesize+=mfile.length();
					mTempProfile.add(mlistProfile.get(i));
				}
			}
		}
	}

	@Override
	public void onBackPressed() {

		if(imagecounter>0)
		{
			imagesize/=1024;
			TAGS.imageCounter-=imagecounter;
			TAGS.imageSize-=imagesize;
		}
		else if(profileimagecounter>0)
		{
			profileimagesize/=1024;
			TAGS.profileCounter-=profileimagecounter;
			TAGS.profileSize-=profileimagesize;
		}
		if(TAGS.imageSize<0)
		{
			TAGS.imageSize=0;
		}
		if(TAGS.profileSize<0)
		{
			TAGS.profileSize=0;
		}
		//		refreshGallery();
		Intent mIntent=new Intent(WhatsAppImagesActivity.this,CleanerActivity.class);
		//			mIntent.putExtra("IMAGESDEL", "yes");
		startActivity(mIntent);

		super.onBackPressed();
	}
}