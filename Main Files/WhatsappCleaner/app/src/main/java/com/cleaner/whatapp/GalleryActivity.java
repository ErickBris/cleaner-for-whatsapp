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

import com.cleaner.whatapp.whatsappimages.ImagesDownload;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements Animation.AnimationListener {
    String myAppName;
    Toolbar mToolbar;
    ViewPager mviewPager;
    ImagesDownload tab1Images;
    public static MenuItem delete;
    public static MenuItem share;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;
    AdRequest adRequest, adRequestint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygallery);
        myAppName = getResources().getString(R.string.app_name);
        tab1Images = new ImagesDownload();
        mviewPager = (ViewPager) findViewById(R.id.viewpager);
        GalleryActivity.this.setTitle("WhatsApp Images");
        String path= Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Images/Sent";
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
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("C69095F3C24675F5F8C9B5031B0E6EEB")
                .build();
        mAdView.loadAd(adRequest);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (mToolbar != null) {
//            mToolbar.setTitle("Whatsapp Images");
//            setSupportActionBar(mToolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
//        }
        viewPagerSetup(mviewPager);
        loadInterstitialAd();
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
        File oldFolder = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent");
        File newFolder = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Images","Sent");
        boolean success = oldFolder.renameTo(newFolder);
        showInterstitial();
        finish();
    }
    private void loadInterstitialAd() {
        adRequestint = new AdRequest.Builder().addTestDevice("C69095F3C24675F5F8C9B5031B0E6EEB").build();
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(adRequestint);
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    private void viewPagerSetup(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(tab1Images, "IMAGES");
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
                Log.i("images", "" + tab1Images.getDeleteImage());
                for (int i = 0; i < tab1Images.mShareImages.size(); i++) {
                    if (!tab1Images.mShareImages.get(i).equalsIgnoreCase("null")) {
                        File file = new File(tab1Images.mShareImages.get(i));
                        boolean deleted = file.delete();
                    }
                }
                tab1Images.refresh();
            }
        } else if (itemId == R.id.action_share) {
            if (mviewPager.getCurrentItem() == 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_TEXT, "Downloaded using " + myAppName + " android application");
                intent.setType("image/jpeg");
                ArrayList<Uri> files = new ArrayList<Uri>();

                for (String path : tab1Images.mShareImages) {
                    File file = new File(path);
                    Uri uri = Uri.fromFile(file);
                    files.add(uri);
                }

                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                startActivity(Intent.createChooser(intent, "Share images using"));
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
