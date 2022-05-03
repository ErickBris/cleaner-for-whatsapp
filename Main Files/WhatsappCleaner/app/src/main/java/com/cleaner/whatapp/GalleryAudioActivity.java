package com.cleaner.whatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;

import com.cleaner.whatapp.whatsappaudio.AudioDownload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryAudioActivity extends AppCompatActivity implements Animation.AnimationListener {
    String myAppName;
    Toolbar mToolbar;
    ViewPager mviewPager;
    AudioDownload tab2Videos;
    public static MenuItem delete;
    public static MenuItem share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_mygallery);
        myAppName = getResources().getString(R.string.app_name);
        tab2Videos = new AudioDownload();
        mviewPager = (ViewPager) findViewById(R.id.viewpager_audio);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        GalleryAudioActivity.this.setTitle("WhatsApp Audios");
        String path= Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Audio/Sent";
        File file = new File(path);
        try{
            if(file.exists()){
                deleteRecursive(file);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        viewPagerSetup(mviewPager);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        File oldFolderaudio = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent3");
        File newFolderaudio = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Audio","Sent");
        boolean successaudio = oldFolderaudio.renameTo(newFolderaudio);
        finish();
    }
    private void viewPagerSetup(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(tab2Videos, "VIDEOS");
        viewPager.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mygallery, menu);
        delete = menu.findItem(R.id.action_delete);
        share = menu.findItem(R.id.action_share);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.action_delete) {
            if (mviewPager.getCurrentItem() == 0) {
                Log.i("images", "" + tab2Videos.mShareVideos);
                for (int i = 0; i < tab2Videos.mShareVideos.size(); i++) {
                    if (!tab2Videos.mShareVideos.get(i).equalsIgnoreCase("null")) {
                        File file = new File(tab2Videos.mShareVideos.get(i));
                        boolean deleted = file.delete();
                    }
                }
                tab2Videos.refresh();
            }
        } else if (itemId == R.id.action_share) {
            if (mviewPager.getCurrentItem() == 0) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Downloaded using " + myAppName + " android application");
                sharingIntent.setType("audio/*");
                ArrayList<Uri> files = new ArrayList<Uri>();

                for (String path : tab2Videos.mShareVideos) {
                    File file = new File(path);
                    Uri uri = Uri.fromFile(file);
                    files.add(uri);
                }
                sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(sharingIntent, "Share audios using"));
            }
        }
        return super.onOptionsItemSelected(item);
    }
    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
    }


}
