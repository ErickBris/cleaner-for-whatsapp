package com.kimoji.whatsapp.cleaner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kaavu.whatsappcleaner.R;
import com.kimoji.whatsapp.cleaner.WhatsAppVideoActivity.CounterClass;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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


public class WhatsAppVoiceActivity extends Activity {

	ArrayList<WhatsppImages>mlistVoice;
	ArrayList<WhatsppImages>mlistAudio;
	
	ArrayList<WhatsppImages>mlistTempVoice;
	ArrayList<WhatsppImages>mlistTempAudio;
	
	GridView mGridView;
	ImageView mImageView;
	ImageView mImageViewCheck;
	List<File> mFileList;
//	LinearLayout mLinearLayoutDelete;
	String listType="";
	TextView mTextViewTitle;
	
	int voiceCounter=0;
	long voiceSize=0;
	int audioCounter=0;
	long audioSize=0;
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
		
		setContentView(R.layout.activity_whats_app_voice);
		mlistVoice=new ArrayList<WhatsppImages>();
		mlistTempVoice=new ArrayList<WhatsppImages>();
		
		mSharePreference=new SharePreference(this);
		TAGS.itemDeleted=false;
		check=false;
		
		mlistAudio=new ArrayList<WhatsppImages>();
		mlistTempAudio=new ArrayList<WhatsppImages>();
		
		for(int i=0;i<TAGS.mListAudio.size();i++)
		{
			TAGS.mListAudio.get(i).setSelected(false);
		}
		
		for(int i=0;i<TAGS.mListVoice.size();i++)
		{
			TAGS.mListVoice.get(i).setSelected(false);
		}
		
		mlistVoice=TAGS.mListVoice;
		mlistAudio=TAGS.mListAudio;
		
		mFileList=new ArrayList<File>();

		mGridView=(GridView) findViewById(R.id.gridview_voice);
		mImageView=(ImageView) findViewById(R.id.images_voice);
		listType=getIntent().getStringExtra("AUDIO");
		mTextViewTitle=(TextView) findViewById(R.id.textview_title_voice);
		if(listType.equals("audio"))
		{
			mTextViewTitle.setText("WhatsApp Audio");
			mGridView.setAdapter(new WhatsAppAudioAdapter(this, mlistAudio, ""));
		}
		else
		{
			mTextViewTitle.setText("WhatsApp Voice Notes");
			mGridView.setAdapter(new WhatsAppAudioAdapter(this, mlistVoice, ""));
		}
//		mGridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) 
//			{
//				Intent intent = new Intent();  
//				intent.setAction(android.content.Intent.ACTION_VIEW);  
//				File file = new File(mlist.get(arg2).getPath());  
//				intent.setDataAndType(Uri.fromFile(file), "audio/*");  
//				startActivity(intent);

//				Intent tostart = new Intent(Intent.ACTION_VIEW);
//				tostart.setDataAndType(Uri.parse(mlist.get(arg2).getPath()), "audio/*");
//				startActivity(tostart);

//			}
//		});
		mImageViewCheck=(ImageView) findViewById(R.id.images_voice);
		mImageView=(ImageView) findViewById(R.id.imageview_delete_audio);
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				j=0;
				if(listType.equals("audio"))
				{
					for(int i=0;i<mlistAudio.size();i++) 
					{
						boolean m=mlistAudio.get(i).isSelected();
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
						Utils.SetDiolog(WhatsAppVoiceActivity.this, "No items selected to delete");
					}
				}
				else
				{
					for(int i=0;i<mlistVoice.size();i++) 
					{
						boolean m=mlistVoice.get(i).isSelected();
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
						Utils.SetDiolog(WhatsAppVoiceActivity.this, "No items selected to delete");
					}
				}
			}
		});
		adRequest = new AdRequest.Builder().build();
		adView = (AdView)findViewById(R.id.adView_1);
		adView.loadAd(adRequest);

		interstitial = new InterstitialAd(WhatsAppVoiceActivity.this);
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
			interstitial = new InterstitialAd(WhatsAppVoiceActivity.this);
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
				
				if(listType.equals("audio"))
				{
					for(int i=0;i<TAGS.mListAudio.size();i++)
					{
						TAGS.mListAudio.get(i).setSelected(true);
					}
					if(check)
					{
						check=false;
						WhatsAppVoiceActivity.this.mlistAudio=TAGS.mListAudio;
					}
					else
					{
						check=true;
						for(int i=0;i<TAGS.mListAudio.size();i++)
						{
							TAGS.mListAudio.get(i).setSelected(false);
						}
						WhatsAppVoiceActivity.this.mlistAudio=TAGS.mListAudio;
					}
					mGridView.setAdapter(new WhatsAppAudioAdapter(WhatsAppVoiceActivity.this, mlistAudio, ""));
				}
				else
				{
					for(int i=0;i<TAGS.mListVoice.size();i++)
					{
						TAGS.mListVoice.get(i).setSelected(true);
					}
					if(check)
					{
						check=false;
						WhatsAppVoiceActivity.this.mlistVoice=TAGS.mListVoice;
					}
					else
					{
						check=true;
						for(int i=0;i<TAGS.mListVoice.size();i++)
						{
							TAGS.mListVoice.get(i).setSelected(false);
						}
						WhatsAppVoiceActivity.this.mlistVoice=TAGS.mListVoice;
					}
					mGridView.setAdapter(new WhatsAppAudioAdapter(WhatsAppVoiceActivity.this, mlistVoice, ""));
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
		if(listType.equals("audio"))
		{
			for(int i=0;i<mlistAudio.size();i++) 
			{
				boolean m=mlistAudio.get(i).isSelected();
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
			for(int i=0;i<mlistVoice.size();i++) 
			{
				boolean m=mlistVoice.get(i).isSelected();
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

		final Dialog mDialog = new Dialog(WhatsAppVoiceActivity.this);
		LayoutInflater layoutInflater = LayoutInflater.from(WhatsAppVoiceActivity.this);
		View promptView = layoutInflater.inflate(R.layout.dialog_change1, null);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(promptView);
		
		TextView mTextView=(TextView) promptView.findViewById(R.id.text_title);
		mTextView.setText("Are you sure you want to delete "+j+" audio files?");
		
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
				if(listType.equals("audio"))
				{
					mlistAudio.removeAll(mlistTempAudio);
					mGridView.setAdapter(new WhatsAppAudioAdapter(WhatsAppVoiceActivity.this, mlistAudio, ""));
					mlistTempAudio.clear();
					
				}
				else
				{
					mlistVoice.removeAll(mlistTempVoice);
					mGridView.setAdapter(new WhatsAppAudioAdapter(WhatsAppVoiceActivity.this, mlistVoice, ""));
					mlistTempVoice.clear();
				}
				TAGS.refreshGallery(WhatsAppVoiceActivity.this);
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
		audioCounter=0;
		audioSize=0;
		voiceCounter=0;
		voiceSize=0;
		mFileList=new ArrayList<File>();
		if(listType.equals("audio"))
		{
			for(int i=0;i<mlistAudio.size();i++) 
			{
				boolean m=mlistAudio.get(i).isSelected();
				if(m)
				{	
					File mfile=new File(mlistAudio.get(i).getPath());
					mFileList.add(mfile);
					audioCounter++;
					audioSize+=mfile.length();
					mlistTempAudio.add(mlistAudio.get(i));
				}
			}
		}
		else
		{
			for(int i=0;i<mlistVoice.size();i++) 
			{
				boolean m=mlistVoice.get(i).isSelected();
				if(m)
				{	
					File mfile=new File(mlistVoice.get(i).getPath());
					mFileList.add(mfile);
					voiceCounter++;
					voiceSize+=mfile.length();
					mlistTempVoice.add(mlistVoice.get(i));
				}
			}
		}
	}
	@Override
	public void onBackPressed() {

		if(audioCounter>0)
		{
			audioSize/=1024;
			TAGS.audioCounter-=audioCounter;
			TAGS.audioSize-=audioSize;
		}
		else if(voiceCounter>0)
		{
			voiceSize/=1024;
			TAGS.voiceCounter-=voiceCounter;
			TAGS.voiceSize-=voiceSize;
		}
		if(TAGS.voiceSize<0)
		{
			TAGS.voiceSize=0;
		}
		if(TAGS.audioSize<0)
		{
			TAGS.audioSize=0;
		}
		Intent mIntent=new Intent(WhatsAppVoiceActivity.this,CleanerActivity.class);
		//			mIntent.putExtra("IMAGESDEL", "yes");
		startActivity(mIntent);

		super.onBackPressed();
	}
}