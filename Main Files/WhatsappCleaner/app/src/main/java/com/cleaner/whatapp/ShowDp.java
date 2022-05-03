package com.cleaner.whatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.Arrays;

public class ShowDp extends AppCompatActivity {
    public static final String A2 = "e58461";
    String myAppName, path;
    Toolbar toolbar;
    int mPosition;
    DpAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    File[] imageFiles;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dp_view);
        myAppName = getResources().getString(R.string.app_name);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPosition = getIntent().getIntExtra("pos", 0);
        CreateFileArray();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setTitle("" + ((int) mPosition + 1) + "/" + imageFiles.length);
        }
        mCustomPagerAdapter = new DpAdapter(ShowDp.this, imageFiles);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setCurrentItem(mPosition, true);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                path = imageFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                if (toolbar != null) {
                    toolbar.setTitle("" + ((int) mPosition + 1) + "/" + imageFiles.length);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }
    private void CreateFileArray() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getApplicationContext(), "Error! No SDCARD Found!", Toast.LENGTH_LONG).show();
        } else {
            file = new File(Environment.getExternalStorageDirectory(), StaticVariables.rootDir + "/" + StaticVariables.dpDir + "/");
            file.mkdirs();
        }
        if (file.isDirectory()) {
            imageFiles = file.listFiles();
            Arrays.sort(imageFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_image, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        switch (itemId) {
            case R.id.action_delete:
                path = imageFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                File file = new File(path);
                boolean deleted = file.delete();
                CreateFileArray();
                if (imageFiles.length != 0) {
                    mCustomPagerAdapter = new DpAdapter(ShowDp.this, imageFiles);
                    mViewPager.setAdapter(mCustomPagerAdapter);
                    mViewPager.setCurrentItem(mPosition, true);
                    path = imageFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                    if (toolbar != null) {
                        toolbar.setTitle("" + ((int) mPosition + 1) + "/" + imageFiles.length);
                    }
                } else {
                    finish();
                }
                break;
            case R.id.action_share:
                path = imageFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "#" + myAppName);
                sharingIntent.setType("image/JPEG");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
