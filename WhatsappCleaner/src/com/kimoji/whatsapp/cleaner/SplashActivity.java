package com.kimoji.whatsapp.cleaner;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import com.kaavu.whatsappcleaner.R;

@SuppressWarnings("deprecation")
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_splash);
		//getSupportActionBar().hide();
		Timer mTimer=new Timer();
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {

				Intent mIntent=new Intent(SplashActivity.this,CleanerActivity.class);
				startActivity(mIntent);
				finish();
			}
		}, 4000);
	}

	
}
