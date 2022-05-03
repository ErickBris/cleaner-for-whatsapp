package com.kimoji.whatsapp.cleaner;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kaavu.whatsappcleaner.R;
import com.kimoji.whatsapp.cleaner.inapp.IabHelper;
import com.kimoji.whatsapp.cleaner.inapp.IabResult;
import com.kimoji.whatsapp.cleaner.inapp.Inventory;
import com.kimoji.whatsapp.cleaner.inapp.Purchase;

public class CleanerActivity extends Activity  implements OnClickListener{

//	ImageView mImageViewDeleteAudio;
//	ImageView mImageViewDeleteVideo;
//	ImageView mImageViewDeleteImages;
//	ImageView mImageViewDeleteProfile;
//	ImageView mImageViewDeleteVoice;
//	ImageView mImageViewDeleteWallPaper;

	TextView mTextViewAudioSize;
	TextView mTextViewVideoSize;
	TextView mTextViewImageSize;
	TextView mTextViewProfileSize;
	TextView mTextViewVoiceSize;
	TextView mTextViewWallpaperSize;
	TextView mTextViewDatabaseSize;

	RelativeLayout mRelativeLayoutAudio;
	RelativeLayout mRelativeLayoutVideo;
	RelativeLayout mRelativeLayoutImages;
	RelativeLayout mRelativeLayoutProfile;
	RelativeLayout mRelativeLayoutVoice;
	RelativeLayout mRelativeLayoutWallpaper;
	RelativeLayout mRelativeLayoutDatabase;
	RelativeLayout mRelativeLayoutBuy;
	Intent mIntent;
	String type="";
	List<File>mFileListVoice;
	List<File>mFileListVideo;
	List<File>mFileListWallpaper;
	List<File>mFileListImages;
	List<File>mFileListProfile;
	List<File>mFileListAudio;
	List<File>mFileListDatabases;
	String imagepath="";
	String imagepathSent="";

	String databasePath="";
	String videoPath="";
	String videoPathSent="";

	String audioPath="";
	String audioPathSent="";
	String voicePath="";
	String voicePathSent="";
	String profilePath="";
	String wallpaperPath="";
	ProgressDialog mDialog;
	ArrayList<WhatsppImages>mListImages;
	ArrayList<WhatsppVideos>mListVideos;
	ArrayList<WhatsppImages>mListProfile;
	ArrayList<WhatsppImages>mListVoice;
	ArrayList<WhatsppImages>mListAudio;
	ArrayList<WhatsppImages>mListWallpaper;
	ArrayList<WhatsppImages>mListDatabase;
	ImageView mImageViewProfile;
	ImageView mImageViewImages;
	ImageView mImageViewAudio;
	ImageView mImageViewVideos;
	ImageView mImageViewVoice;
	ImageView mImageViewWallpaper;
	ImageView mImageViewDatabase;
	Drawable mDrawable;
	static boolean firstTime=false;
	SharePreference mSharePreference;
	AlertDialog alertDialog;
	InterstitialAd interstitial;
	AdRequest adRequest;
	AdView adView; 
	CounterClass mtimer;
	private String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo35ZDbHywopdPA1nqfV1FutYMMu+Uv92+KOC0rXWXZXPxI6rEvhbt6kCNzNsxxy+rWYHy48Xdv4aKJujwjVcUyV8d1Vjz7jtfwjkZA3NmqxrBuZLfVa+MkDxh2A+NTF54awJj17xkgplpTSQkyi3mO81fHUBqDXNShwtrGlXGwSMW4cSsvQy87nxMOmmgCt/srSD9V6R9c8WO0T4bWUvxCFrd0n7FMxyJGsRETadpskUAdRKtE4PFSVkAbBalMAaVFte9tRdqlYAu3Fl9GAn+sR4Geq3xCy8jcMtkBVUc6O4aAEDos2gP2YnrlaokdFGNu7XisHvL1sYxQqtp+LydQIDAQAB";

	IabHelper mHelper;
	private String SKU_ITEM_00 = "com.buy"; // get profile
	private static final int RC_REQUEST = 10001;
	boolean isInAppBilling = false;
	private boolean setup_successed = false;
	private String payload = "";
	boolean isBoughtItem1 = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);

		mFileListAudio=new ArrayList<File>();
		mFileListProfile=new ArrayList<File>();
		mFileListImages=new ArrayList<File>();
		mFileListWallpaper=new ArrayList<File>();
		mFileListVideo=new ArrayList<File>();
		mFileListVoice=new ArrayList<File>();
		mFileListDatabases=new ArrayList<File>();

		mSharePreference=new SharePreference(this);
		initIABSetup();
		mImageViewProfile=(ImageView) findViewById(R.id.imageview_profile);
		mImageViewImages=(ImageView) findViewById(R.id.imageview_gallery);
		mImageViewAudio=(ImageView) findViewById(R.id.imageview_audio);
		mImageViewVideos=(ImageView) findViewById(R.id.imageview_video);
		mImageViewVoice=(ImageView) findViewById(R.id.imageview_voice);
		mImageViewWallpaper=(ImageView) findViewById(R.id.imageview_wallpaper);
		mImageViewDatabase=(ImageView) findViewById(R.id.imageview_database);

		mDrawable=Utils.tintImage(this, R.drawable.placeholder_profile, R.color.dark_pink);
		mImageViewProfile.setImageDrawable(mDrawable);

		mDrawable=Utils.tintImage(this, R.drawable.ic_action_volume_on, R.color.dark_pink);
		mImageViewAudio.setImageDrawable(mDrawable);

		mDrawable=Utils.tintImage(this, R.drawable.ic_action_video, R.color.dark_pink);
		mImageViewVideos.setImageDrawable(mDrawable);

		mDrawable=Utils.tintImage(this, R.drawable.voice, R.color.dark_pink);
		mImageViewVoice.setImageDrawable(mDrawable);

		mDrawable=Utils.tintImage(this, R.drawable.wallpaper, R.color.dark_pink);
		mImageViewWallpaper.setImageDrawable(mDrawable);

		mDrawable=Utils.tintImage(this, R.drawable.ic_database, R.color.dark_pink);
		mImageViewDatabase.setImageDrawable(mDrawable);


//		mImageViewDeleteAudio=(ImageView) findViewById(R.id.imageview_delete_audio);
//		mImageViewDeleteProfile=(ImageView) findViewById(R.id.imageview_delete_profile);
//		mImageViewDeleteImages=(ImageView) findViewById(R.id.imageview_delete_images);
//		mImageViewDeleteVoice=(ImageView) findViewById(R.id.imageview_delete_voice);
//		mImageViewDeleteVideo=(ImageView) findViewById(R.id.imageview_delete_video);
//		mImageViewDeleteWallPaper=(ImageView) findViewById(R.id.imageview_delete_wallpaper);

		mTextViewAudioSize=(TextView) findViewById(R.id.textview_audio_size);
		mTextViewProfileSize=(TextView) findViewById(R.id.textview_profile_size);
		mTextViewImageSize=(TextView) findViewById(R.id.textview_image_size);
		mTextViewVoiceSize=(TextView) findViewById(R.id.textview_voice_size);
		mTextViewVideoSize=(TextView) findViewById(R.id.textview_video_size);
		mTextViewWallpaperSize=(TextView) findViewById(R.id.textview_wallpaper_size);
		mTextViewDatabaseSize=(TextView) findViewById(R.id.textview_database);


		mRelativeLayoutBuy=(RelativeLayout) findViewById(R.id.layout_buy);
		mRelativeLayoutAudio=(RelativeLayout) findViewById(R.id.layout_audio1);
		mRelativeLayoutProfile=(RelativeLayout) findViewById(R.id.layout_profile);
		mRelativeLayoutImages=(RelativeLayout) findViewById(R.id.layout_images1);
		mRelativeLayoutVoice=(RelativeLayout) findViewById(R.id.layout_voice);
		mRelativeLayoutVideo=(RelativeLayout) findViewById(R.id.layout_video);
		mRelativeLayoutWallpaper=(RelativeLayout) findViewById(R.id.layout_wallpaper);
		mRelativeLayoutDatabase=(RelativeLayout) findViewById(R.id.layout_database);

//		mImageViewDeleteAudio.setOnClickListener(this);
//		mImageViewDeleteProfile.setOnClickListener(this);
//		mImageViewDeleteImages.setOnClickListener(this);
//		mImageViewDeleteVoice.setOnClickListener(this);
//		mImageViewDeleteVideo.setOnClickListener(this);
//		mImageViewDeleteWallPaper.setOnClickListener(this);

		mRelativeLayoutBuy.setOnClickListener(this);
		mRelativeLayoutDatabase.setOnClickListener(this);
		mRelativeLayoutAudio.setOnClickListener(this);
		mRelativeLayoutProfile.setOnClickListener(this);
		mRelativeLayoutImages.setOnClickListener(this);
		mRelativeLayoutVoice.setOnClickListener(this);
		mRelativeLayoutVideo.setOnClickListener(this);
		mRelativeLayoutWallpaper.setOnClickListener(this);

		databasePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Databases";
		wallpaperPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WallPaper";

		imagepathSent=Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Images/Sent";
		imagepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Images";

		voicePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Voice Notes";
		voicePathSent=Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Voice Notes/Sent";

		audioPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Audio";
		audioPathSent=Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Audio/Sent";

		videoPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Video";
		videoPathSent= Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Video/Sent";

		profilePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Profile Pictures";



		adRequest = new AdRequest.Builder().build();
		adView = (AdView)findViewById(R.id.adView_1);
		adView.loadAd(adRequest);

		interstitial = new InterstitialAd(CleanerActivity.this);
		interstitial.setAdUnitId(getResources().getString(R.string.irres_id));
		interstitial.loadAd(adRequest);

	
		if(firstTime)
		{
			maintainCalulation();
			Log.e("first", "first if "+firstTime);
		}
		else
		{
			firstTime=true;
			Log.e("first", "first else "+firstTime);
			new UploadFeed().execute();
		}
		if(mSharePreference.isPuchaseItem())
		{
			adView.setVisibility(View.GONE);
			adView.destroy();


			// Create ad request
			adRequest = new AdRequest.Builder().build();
			interstitial = new InterstitialAd(CleanerActivity.this);
			interstitial.setAdUnitId("123456");
			// Begin loading your interstitial
			interstitial.loadAd(adRequest);
		}
		else
		{
//			mtimer = new CounterClass(100000000, 40000);
//			mtimer.start();

		}
		//		getFileList();


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
	public void initIABSetup() {

		mHelper = new IabHelper(this, base64EncodedPublicKey);
		mHelper.enableDebugLogging(true);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {

				if (!result.isSuccess()) {
					Log.e("setup billing", "Setup Billing is failed");

					setup_successed = false;
					return;
				}
				setup_successed = true;
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

			// Toast.makeText(BackupActivity.this, "Query Invetory Finished",
			// Toast.LENGTH_LONG).show();
			// Log.d(TAG, "Query inventory finished.");
			if (result.isFailure() || !setup_successed) {
				// Toast.makeText(BackupActivity.this,
				// "Failed to query inventory. Please Retry it later.",
				// Toast.LENGTH_LONG).show();
				return;
			}
			List<Purchase> purchases = inventory.getAllPurchases();// new
			// ArrayList<Purchase>();
			if (purchases != null && purchases.size() > 0) 
			{
				if (inventory.hasPurchase(SKU_ITEM_00)) {
					purchases.add(inventory.getPurchase(SKU_ITEM_00));
					if(!mSharePreference.isPuchaseItem())
					{
						mSharePreference.setPuchaseItem(true);
					}
					isBoughtItem1 = true;
				}
				

			}
			if (purchases.size() > 0) {
				isInAppBilling = true;
			}
		}
	};
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d("IAB", "Purchase finished: " + result + ", purchase: " + purchase);

			// if we were disposed of in the meantime, quit.

			Log.e("in purchase", "purchase");
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				Log.e("in purchase", "purchase falied");
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				Utils.SetDiolog(CleanerActivity.this,"Error purchasing. Authenticity verification failed.");
				Log.e("in purchase", "purchase verify failed");
				return;
			}
			Log.d("IAB", "Purchase successful.");
			Log.e("in purchase", "purchase mtesting");
			if (purchase.getSku().equals(SKU_ITEM_00)) 
			{
				Log.e("in purchase", "purchase has item1");
				//				isBoughtItem1 = true;
				mSharePreference.setPuchaseItem(true);
				//				sharedPreferences.edit().putBoolean(DB_ITEM_1, isBoughtItem1).commit();
				//				saveSetting();

				runOnUiThread(new Runnable() 
				{
					@Override
					public void run() 
					{
						Log.e("in purchase", "purchase loading list1");
						
						adView.setVisibility(View.GONE);
						adView.destroy();


						// Create ad request
						adRequest = new AdRequest.Builder().build();
						interstitial = new InterstitialAd(CleanerActivity.this);
						interstitial.setAdUnitId("123456");
						// Begin loading your interstitial
						interstitial.loadAd(adRequest);
					}
				});
			} 
			


			else {
				Log.e("in purchase", "purchase loading list1 filled");
			}
			
		}
	};
	//	public void displayInterstitial() {
	//		// If Ads are loaded, show Interstitial else show nothing.
	//		runOnUiThread(new Runnable() {
	//			
	//			@Override
	//			public void run() {
	//				if (interstitial.isLoaded()) {
	//					interstitial.show();
	//				}
	//				
	//			}
	//		});
	//		
	//	}
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		return true;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			mHelper.queryInventoryAsync(mGotInventoryListener);
		}
	}
	public void maintainCalulation()
	{
		mTextViewAudioSize.setText("Files: "+TAGS.audioCounter+" / "+TAGS.audioSize/1024+" MB");
		mTextViewImageSize.setText("Files: "+TAGS.imageCounter+" / "+TAGS.imageSize/1024+" MB");
		mTextViewProfileSize.setText("Files: "+TAGS.profileCounter+" / "+TAGS.profileSize/1024+" MB");
		mTextViewVideoSize.setText("Files: "+TAGS.videoCounter+" / "+TAGS.videoSize/1024+" MB");
		mTextViewVoiceSize.setText("Files: "+TAGS.voiceCounter+" / "+TAGS.voiceSize/1024+" MB");
		mTextViewWallpaperSize.setText("Files: "+TAGS.wallpaperCounter+" / "+TAGS.wallpaperSize/1024+" MB");
		mTextViewDatabaseSize.setText("Files: "+TAGS.databaseCounter+" / "+TAGS.databaseSize/1024+" MB");
		if(TAGS.audioSize<1024)
		{
			mTextViewAudioSize.setText("Files: "+TAGS.audioCounter+" / "+TAGS.audioSize+" KB");
		}
		if(TAGS.videoSize<1024)
		{
			mTextViewVideoSize.setText("Files: "+TAGS.videoCounter+" / "+TAGS.videoSize+" KB");
		}
		if(TAGS.imageSize<1024)
		{
			mTextViewImageSize.setText("Files: "+TAGS.imageCounter+" / "+TAGS.imageSize+" KB");
		}
		if(TAGS.profileSize<1024)
		{
			mTextViewProfileSize.setText("Files: "+TAGS.profileCounter+" / "+TAGS.profileSize+" KB");
		}
		if(TAGS.voiceSize<1024)
		{
			mTextViewVoiceSize.setText("Files: "+TAGS.voiceCounter+" / "+TAGS.voiceSize+" KB");
		}
		if(TAGS.wallpaperSize<1024)
		{
			mTextViewWallpaperSize.setText("Files: "+TAGS.wallpaperCounter+" / "+TAGS.wallpaperSize+" KB");
		}
		if(TAGS.databaseSize<1024)
		{
			mTextViewDatabaseSize.setText("Files: "+TAGS.databaseCounter+" / "+TAGS.databaseSize+" KB");
		}
	}
	public class UploadFeed extends AsyncTask<Void,Void, Void>
	{

		String response = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mDialog=Utils.SetProgressBar(mDialog,CleanerActivity.this);
			TAGS.clearTags();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			getWhatsAppVidoes(videoPath,true);
			getWhatsAppVidoes(videoPathSent,false);

			getWhatsImages(imagepath,true);
			getWhatsImages(imagepathSent,false);

			getWhatsVoice(voicePath,true);
			getWhatsVoice(voicePathSent,false);

			getWhatsAudio(audioPath,true);
			getWhatsAudio(audioPathSent,false);

			getWhatsProfileImages(profilePath);
			getWhatsAppWallpapers(wallpaperPath);

			getdatebasefile(databasePath);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			mDialog.dismiss();
			mTextViewAudioSize.setText("Files: "+TAGS.audioCounter+" / "+TAGS.audioSize/1024+" MB");
			mTextViewImageSize.setText("Files: "+TAGS.imageCounter+" / "+TAGS.imageSize/1024+" MB");
			mTextViewProfileSize.setText("Files: "+TAGS.profileCounter+" / "+TAGS.profileSize/1024+" MB");
			mTextViewVideoSize.setText("Files: "+TAGS.videoCounter+" / "+TAGS.videoSize/1024+" MB");
			mTextViewVoiceSize.setText("Files: "+TAGS.voiceCounter+" / "+TAGS.voiceSize/1024+" MB");
			mTextViewWallpaperSize.setText("Files: "+TAGS.wallpaperCounter+" / "+TAGS.wallpaperSize/1024+" MB");
			mTextViewDatabaseSize.setText("Files: "+TAGS.databaseCounter+" / "+TAGS.databaseSize/1024+" MB");
			if(TAGS.audioSize<1024)
			{
				mTextViewAudioSize.setText("Files: "+TAGS.audioCounter+" / "+TAGS.audioSize+" KB");
			}
			if(TAGS.videoSize<1024)
			{
				mTextViewVideoSize.setText("Files: "+TAGS.videoCounter+" / "+TAGS.videoSize+" KB");
			}
			if(TAGS.imageSize<1024)
			{
				mTextViewImageSize.setText("Files: "+TAGS.imageCounter+" / "+TAGS.imageSize+" KB");
			}
			if(TAGS.profileSize<1024)
			{
				mTextViewProfileSize.setText("Files: "+TAGS.profileCounter+" / "+TAGS.profileSize+" KB");
			}
			if(TAGS.voiceSize<1024)
			{
				mTextViewVoiceSize.setText("Files: "+TAGS.voiceCounter+" / "+TAGS.voiceSize+" KB");
			}
			if(TAGS.wallpaperSize<1024)
			{
				mTextViewWallpaperSize.setText("Files: "+TAGS.wallpaperCounter+" / "+TAGS.wallpaperSize+" KB");
			}
			if(TAGS.databaseSize<1024)
			{
				mTextViewDatabaseSize.setText("Files: "+TAGS.databaseCounter+" / "+TAGS.databaseSize+" KB");
			}
			getFileList();
			//			for(int i=0;i<TAGS.mListWallpaper.size();i++)
			//			{
			//				Log.e("", TAGS.mListWallpaper.get(i).getPath());
			//			}
			//			Intent mIntent=new Intent(MainActivity.this,CleanerActivity.class);
			//			startActivity(mIntent);

			//			Log.e("size","size b4:"+TAGS.mListImages.size());
			//			int size=removeDuplicates(TAGS.mListImages);
			//			Log.e("size","size"+size);

		}

	}
	public  void getWhatsAppVidoes(String path,boolean first) {

		//		mDialog=Utils.SetProgressBar(mDialog, MainActivity.this);
		File files = new File(path);

		FileFilter filter = new FileFilter() {

			private final List<String> exts = Arrays.asList("3gp", "mp4", "mkv");

			@Override
			public boolean accept(File pathname) 
			{
				String ext;
				String path = pathname.getPath();
				ext = path.substring(path.lastIndexOf(".") + 1);
				return exts.contains(ext);
			}
		};

		final File [] filesFound = files.listFiles(filter);
		mListVideos = new ArrayList<WhatsppVideos>();
		if (filesFound != null && filesFound.length > 0) 
		{
			for (File file : filesFound) 
			{
				WhatsppVideos vd=new WhatsppVideos();
				vd.setPath(file.getAbsolutePath());
				vd.setSize(file.length()/1024);
				TAGS.videoSize+=file.length()/1024;
				vd.setBitmap(ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND));
				mListVideos.add(vd);
				TAGS.videoCounter++;
			}
		}
		if(first)
		{
			TAGS.mListVideos=mListVideos;
		}
		else
		{
			TAGS.mListVideos.addAll(mListVideos);
		}
		Log.e("size","video size: "+TAGS.videoSize);
		//	    Intent mIntent=new Intent(MainActivity.this,WhatsAppVideoActivity.class);
		//		mIntent.putExtra("VIDEOS", mListVideos);
		//		startActivity(mIntent);
	}


	public void getWhatsImages(String path,boolean first) {

		File files = new File(path);

		FileFilter filter = new FileFilter() {

			private final List<String> exts = Arrays.asList("jpeg", "jpg", "png", "bmp", "gif");


			@Override
			public boolean accept(File pathname) 
			{
				String ext;
				String path = pathname.getPath();
				ext = path.substring(path.lastIndexOf(".") + 1);
				return exts.contains(ext);
			}
		};

		final File [] filesFound = files.listFiles(filter);
		mListImages = new ArrayList<WhatsppImages>();
		if (filesFound != null && filesFound.length > 0) 
		{
			for (File file : filesFound) 
			{
				WhatsppImages ld=new WhatsppImages();
				ld.setName(file.getName());
				ld.setPath(file.getAbsolutePath());
				ld.setSize(file.length()/1024);
				TAGS.imageSize+=file.length()/1024;
				TAGS.imageCounter++;
				mListImages.add(ld);
			}
		}
		if(first)
		{
			TAGS.mListImages=mListImages;
		}
		else
		{
			TAGS.mListImages.addAll(mListImages);
		}

	}
	public int removeDuplicates(ArrayList<WhatsppImages> strings) 
	{

		int size = strings.size();
		int duplicates = 0;

		// not using a method in the check also speeds up the execution
		// also i must be less that size-1 so that j doesn't
		// throw IndexOutOfBoundsException
		for (int i = 0; i < size - 1; i++) {
			// start from the next item after strings[i]
			// since the ones before are checked
			for (int j = i + 1; j < size; j++) {
				// no need for if ( i == j ) here
				if (!strings.get(j).getName().equals(strings.get(i).getName()))
					continue;
				duplicates++;
				strings.remove(j);
				// decrease j because the array got re-indexed
				j--;
				// decrease the size of the array
				size--;
			} // for j
		} // for i

		return duplicates;

	}
	public void getWhatsProfileImages(String path) {

		File files = new File(path);

		FileFilter filter = new FileFilter() {

			private final List<String> exts = Arrays.asList("jpeg", "jpg", "png", "bmp", "gif");
			//	        private final List<String> exts = Arrays.asList("3gp", "mp4");

			@Override
			public boolean accept(File pathname) 
			{
				String ext;
				String path = pathname.getPath();
				ext = path.substring(path.lastIndexOf(".") + 1);
				return exts.contains(ext);
			}
		};

		final File [] filesFound = files.listFiles(filter);
		mListProfile = new ArrayList<WhatsppImages>();
		if (filesFound != null && filesFound.length > 0) {
			for (File file : filesFound) 
			{
				WhatsppImages ld=new WhatsppImages();
				ld.setPath(file.getAbsolutePath());
				ld.setSize(file.length()/1024);
				TAGS.profileSize+=file.length()/1024;
				mListProfile.add(ld);
				TAGS.profileCounter++;
			}
		}
		TAGS.mListProfileImages=mListProfile;
	}
	public void getWhatsAppWallpapers(String path) {

		File files = new File(path);

		FileFilter filter = new FileFilter() {

			private final List<String> exts = Arrays.asList("jpeg", "jpg", "png", "bmp", "gif");
			//	        private final List<String> exts = Arrays.asList("3gp", "mp4");

			@Override
			public boolean accept(File pathname) 
			{
				String ext;
				String path = pathname.getPath();
				ext = path.substring(path.lastIndexOf(".") + 1);
				return exts.contains(ext);
			}
		};

		final File [] filesFound = files.listFiles(filter);
		mListWallpaper = new ArrayList<WhatsppImages>();
		if (filesFound != null && filesFound.length > 0) {
			for (File file : filesFound) 
			{
				WhatsppImages ld=new WhatsppImages();
				ld.setPath(file.getAbsolutePath());
				ld.setSize(file.length()/1024);
				TAGS.wallpaperSize+=file.length()/1024;
				mListWallpaper.add(ld);
				TAGS.wallpaperCounter++;
			}
		}
		TAGS.mListWallpaper=mListWallpaper;
	}

	public void getWhatsVoice(String path,boolean first) {

		//		mDialog=Utils.SetProgressBar(mDialog, MainActivity.this);
		File files = new File(path);

		FileFilter filter = new FileFilter() {

			private final List<String> exts = Arrays.asList("aac", "m4a", "amr");
			//	        private final List<String> exts = Arrays.asList("3gp", "mp4");

			@Override
			public boolean accept(File pathname) 
			{
				String ext;
				String path = pathname.getPath();
				ext = path.substring(path.lastIndexOf(".") + 1);
				return exts.contains(ext);
			}
		};

		final File [] filesFound = files.listFiles(filter);
		mListVoice = new ArrayList<WhatsppImages>();
		if (filesFound != null && filesFound.length > 0) {
			for (File file : filesFound) 
			{
				WhatsppImages ld=new WhatsppImages();
				ld.setPath(file.getAbsolutePath());
				ld.setSize(file.length()/1024);
				TAGS.voiceSize+=file.length()/1024;
				mListVoice.add(ld);
				TAGS.voiceCounter++;
			}
		}
		if(first)
		{
			TAGS.mListVoice=mListVoice;
		}
		else
		{
			TAGS.mListVoice.addAll(mListVoice);
		}
	}
	public void getdatebasefile(String path)
	{
		File files = new File(path);

		FileFilter filter = new FileFilter() {

			private final List<String> exts = Arrays.asList("crypt8");
			//	        private final List<String> exts = Arrays.asList("3gp", "mp4");

			@Override
			public boolean accept(File pathname) 
			{
				String ext;
				String path = pathname.getPath();
				ext = path.substring(path.lastIndexOf(".") + 1);
				return exts.contains(ext);
			}
		};

		final File [] filesFound = files.listFiles(filter);
		mListDatabase = new ArrayList<WhatsppImages>();
		if (filesFound != null && filesFound.length > 0) {
			for (File file : filesFound) 
			{
				WhatsppImages ld=new WhatsppImages();
				ld.setPath(file.getAbsolutePath());
				ld.setSize(file.length()/1024);
				mListDatabase.add(ld);
				TAGS.databaseSize+=file.length()/1024;
				TAGS.databaseCounter++;
			}
		}
	}
	public void getWhatsAudio(String path,boolean first) {

		//		mDialog=Utils.SetProgressBar(mDialog, MainActivity.this);
		File files = new File(path);

		FileFilter filter = new FileFilter() {

			private final List<String> exts = Arrays.asList("aac", "m4a", "amr", "mp3");
			//	        private final List<String> exts = Arrays.asList("3gp", "mp4");

			@Override
			public boolean accept(File pathname) 
			{
				String ext;
				String path = pathname.getPath();
				ext = path.substring(path.lastIndexOf(".") + 1);
				return exts.contains(ext);
			}
		};

		final File [] filesFound = files.listFiles(filter);
		mListAudio = new ArrayList<WhatsppImages>();
		if (filesFound != null && filesFound.length > 0) {
			for (File file : filesFound) 
			{
				WhatsppImages ld=new WhatsppImages();
				ld.setPath(file.getAbsolutePath());
				ld.setSize(file.length()/1024);
				TAGS.audioSize+=file.length()/1024;
				mListAudio.add(ld);
				TAGS.audioCounter++;
			}
		}
		if(first)
		{
			TAGS.mListAudio=mListAudio;
		}
		else
		{
			TAGS.mListAudio.addAll(mListAudio);
		}

	}

	//	private void initImageLoader() {
	//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
	//                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
	//                .bitmapConfig(Bitmap.Config.RGB_565).build();
	//        
	//        
	//        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
	//                CleanerActivity.this).defaultDisplayImageOptions(defaultOptions).memoryCache(
	//                new WeakMemoryCache());
	//
	//        ImageLoaderConfiguration config = builder.build();
	//        mImageLoader = ImageLoader.getInstance();
	//        mImageLoader.init(config);
	//    }

	private void getFileList() 
	{
		for(int i=0;i<TAGS.mListImages.size();i++)
		{
			File mFile=new File(TAGS.mListImages.get(i).getPath());
			mFileListImages.add(mFile);
		}
		for(int i=0;i<TAGS.mListProfileImages.size();i++)
		{
			File mFile=new File(TAGS.mListProfileImages.get(i).getPath());
			mFileListProfile.add(mFile);
		}
		for(int i=0;i<TAGS.mListWallpaper.size();i++)
		{
			File mFile=new File(TAGS.mListWallpaper.get(i).getPath());
			mFileListWallpaper.add(mFile);
		}
		for(int i=0;i<TAGS.mListAudio.size();i++)
		{
			File mFile=new File(TAGS.mListAudio.get(i).getPath());
			mFileListAudio.add(mFile);
		}
		for(int i=0;i<TAGS.mListVoice.size();i++)
		{
			File mFile=new File(TAGS.mListVoice.get(i).getPath());
			mFileListVoice.add(mFile);
		}
		for(int i=0;i<TAGS.mListVideos.size();i++)
		{
			File mFile=new File(TAGS.mListVideos.get(i).getPath());
			mFileListVideo.add(mFile);
		}
		for(int i=0;i<mListDatabase.size();i++)
		{
			File mFile=new File(mListDatabase.get(i).getPath());
			mFileListDatabases.add(mFile);
		}
		//		Log.e("file size",String.valueOf(mFileListImages.size()));
	}


	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {

		case R.id.layout_buy:
			
			if (setup_successed) {
				if (!mSharePreference.isPuchaseItem()) {
					mHelper.launchPurchaseFlow(CleanerActivity.this, SKU_ITEM_00, RC_REQUEST,
							mPurchaseFinishedListener, payload);
				}
				else
				{
					Utils.SetDiolog(CleanerActivity.this, "You have already purchased");
				}
			} else {
				Utils.SetDiolog(CleanerActivity.this, "You can't use this purchase.");
			}
			
			break;
		case R.id.layout_database:
			if(TAGS.databaseCounter>0)
			{
				showAlert("Database Files");
			}
			else
			{
				Utils.SetDiolog(CleanerActivity.this, "No Database available");
			}
			break;
		case R.id.imageview_delete_audio:
			if(TAGS.audioCounter>0)
			{
				showAlert("Audio Files");
			}
			else
			{
				Utils.SetDiolog(CleanerActivity.this, "No Audio files available");
			}
			break;
		case R.id.imageview_delete_profile:
			if(TAGS.profileCounter>0)
			{
				showAlert("Profile Pictures");
			}
			else
			{
				Utils.SetDiolog(CleanerActivity.this, "No profile images available");
			}

			break;
		case R.id.imageview_delete_images:
			if(TAGS.imageCounter>0)
			{
				showAlert("Image Files");
			}
			else
			{
				Utils.SetDiolog(CleanerActivity.this, "No images available");
			}

			break;
		case R.id.imageview_delete_voice:
			if(TAGS.voiceCounter>0)
			{
				showAlert("Voice Notes");
			}
			else
			{
				Utils.SetDiolog(CleanerActivity.this, "No voice files available");
			}
			break;
		case R.id.imageview_delete_video:
			if(TAGS.videoCounter>0)
			{
				showAlert("Video Files");
			}
			else
			{
				Utils.SetDiolog(CleanerActivity.this, "No vidoes available");
			}
			break;
		case R.id.imageview_delete_wallpaper:
			if(TAGS.wallpaperCounter>0)
			{
				showAlert("WallPapers");
			}
			else
			{
				Utils.SetDiolog(CleanerActivity.this, "No wallpapers available");
			}
			break;
		case R.id.layout_images1:
			mIntent=new Intent(CleanerActivity.this,WhatsAppImagesActivity.class);
			mIntent.putExtra("IMAGES", "images");
			startActivity(mIntent);
			finish();
			break;
		case R.id.layout_video:
			mIntent=new Intent(CleanerActivity.this,WhatsAppVideoActivity.class);
			startActivity(mIntent);
			finish();
			break;
		case R.id.layout_profile:
			mIntent=new Intent(CleanerActivity.this,WhatsAppImagesActivity.class);
			mIntent.putExtra("IMAGES", "profileimages");
			startActivity(mIntent);
			finish();
			break;
		case R.id.layout_audio1:
			mIntent=new Intent(CleanerActivity.this,WhatsAppVoiceActivity.class);
			mIntent.putExtra("AUDIO", "audio");
			startActivity(mIntent);
			finish();
			break;
		case R.id.layout_voice:
			mIntent=new Intent(CleanerActivity.this,WhatsAppVoiceActivity.class);
			mIntent.putExtra("AUDIO", "voice");
			startActivity(mIntent);
			finish();
			break;
		case R.id.layout_wallpaper:
			mIntent=new Intent(CleanerActivity.this,WallPaperActivity.class);
			mIntent.putExtra("AUDIO", "voice");
			startActivity(mIntent);
			finish();
			break;

		default:
			break;
		}
	}

	public void showAlert(final String type)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("Are you sure you want to delete all "+ type+"?");
		alertDialogBuilder.setPositiveButton("YES", 
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				if(type.equals("Audio Files"))
				{
					Log.e("", "audio");
					for(int i=0;i<mFileListAudio.size();i++)
					{
						boolean del=mFileListAudio.get(i).delete();
						//						Log.e("", String.valueOf(i)+String.valueOf(del));
					}
					TAGS.refreshGallery(CleanerActivity.this);
					TAGS.mListAudio.clear();
					TAGS.audioCounter=0;
					TAGS.audioSize=0;
					mTextViewAudioSize.setText("Files: "+TAGS.audioCounter+" / "+TAGS.audioSize+" KB");
				}
				else if(type.equals("Video Files"))
				{
					Log.e("", "video");
					for(int i=0;i<mFileListVideo.size();i++)
					{
						boolean del=mFileListVideo.get(i).delete();
						//						Log.e("", String.valueOf(i)+String.valueOf(del));
					}
					TAGS.refreshGallery(CleanerActivity.this);
					TAGS.mListVideos.clear();
					TAGS.videoCounter=0;
					TAGS.videoSize=0;
					mTextViewVideoSize.setText("Files: "+TAGS.videoCounter+" / "+TAGS.videoSize+" KB");
				}
				else if(type.equals("Voice Notes"))
				{
					Log.e("", "voice");
					for(int i=0;i<mFileListVoice.size();i++)
					{
						boolean del=mFileListVoice.get(i).delete();
						//						Log.e("", String.valueOf(i)+String.valueOf(del));
					}
					TAGS.refreshGallery(CleanerActivity.this);
					TAGS.mListVoice.clear();
					TAGS.voiceCounter=0;
					TAGS.voiceSize=0;
					mTextViewVoiceSize.setText("Files: "+TAGS.voiceCounter+" / "+TAGS.voiceSize+" KB");
				}
				else if(type.equals("Image Files"))
				{
					for(int i=0;i<mFileListImages.size();i++)
					{
						boolean del=mFileListImages.get(i).delete();
						//						Log.e("", String.valueOf(i)+String.valueOf(del));
					}
					TAGS.refreshGallery(CleanerActivity.this);
					TAGS.mListImages.clear();
					TAGS.imageCounter=0;
					TAGS.imageSize=0;
					mTextViewImageSize.setText("Files: "+TAGS.imageCounter+" / "+TAGS.imageSize+" KB");
				}
				else if(type.equals("Profile Pictures"))
				{
					Log.e("", "profile");
					for(int i=0;i<mFileListProfile.size();i++)
					{
						boolean del=mFileListProfile.get(i).delete();
						//						Log.e("", String.valueOf(i)+String.valueOf(del));
					}
					TAGS.refreshGallery(CleanerActivity.this);
					TAGS.mListProfileImages.clear();
					TAGS.profileCounter=0;
					TAGS.profileSize=0;
					mTextViewProfileSize.setText("Files: "+TAGS.profileCounter+" / "+TAGS.profileSize+" KB");
				}
				else if(type.equals("Wallpapers "))
				{
					Log.e("", "wallpaper");
					for(int i=0;i<mFileListWallpaper.size();i++)
					{
						boolean del=mFileListWallpaper.get(i).delete();
						//						Log.e("", String.valueOf(i)+String.valueOf(del));
					}
					TAGS.refreshGallery(CleanerActivity.this);
					TAGS.mListWallpaper.clear();
					TAGS.wallpaperCounter=0;
					TAGS.wallpaperSize=0;
					mTextViewProfileSize.setText("Files: "+TAGS.wallpaperCounter+" / "+TAGS.wallpaperSize+" KB");
				}
				else if(type.equals("Database Files"))
				{
					Log.e("", "database");
					for(int i=0;i<mFileListDatabases.size();i++)
					{
						boolean del=mFileListDatabases.get(i).delete();
						Log.e("", String.valueOf(i)+String.valueOf(del));
					}
					TAGS.refreshGallery(CleanerActivity.this);
					mFileListDatabases.clear();
					TAGS.databaseCounter=0;
					TAGS.databaseSize=0;
					mTextViewDatabaseSize.setText("Files: "+TAGS.databaseCounter+" / "+TAGS.databaseSize+" KB");
				}

			}
		});
		alertDialogBuilder.setNegativeButton("CANCEL", 
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//		if(TAGS.itemDeleted)
		//		{
		//			Log.e("TAGS", "TAGS"+TAGS.itemDeleted);
		//			new UploadFeed().execute();
		//		}

	}
	@Override
	public void onBackPressed() {

		if(!mSharePreference.isRatingChanged())
		{
			show_alert();
		}
		else
		{
			super.onBackPressed();
		}
	}
	public void show_alert() {

		alertDialog = new AlertDialog.Builder(this).create();

		alertDialog.setCancelable(false);
		alertDialog.setTitle("Rate App ");

		alertDialog.setMessage("We are constantly working to improve this app for you.\nIf you like this app,Please \nTake a moment to rate it.\nThank you for your support !!");

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Later", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				alertDialog.dismiss();
				finish();


			} }); 

		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Needs work", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				mSharePreference.setRatingChanged(true);
				final Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("plain/text");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"codeagent0@gmail.com"});
				intent.putExtra(Intent.EXTRA_SUBJECT, "improvement");
				intent.putExtra(Intent.EXTRA_TEXT, "");
				startActivity(intent);

			}}); 

		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Love it!", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				mSharePreference.setRatingChanged(true);
				//					Intent web=new Intent(MainActivity.this,webview.class);
				//					startActivity(web);

				final String appName = getPackageName();
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));

				}

			}});


		alertDialog.show();
	}
	public void refreshDelete()
	{
		if(TAGS.imageSize<1024)
		{
			mTextViewImageSize.setText("Files: "+TAGS.imageCounter+" / "+TAGS.imageSize+" KB");
		}
	}
}
