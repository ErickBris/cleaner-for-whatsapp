package com.cleaner.whatapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class MainAudioAdapter extends PagerAdapter {
    Activity mContext;
    LayoutInflater mLayoutInflater;
    File[] mFiles;
    MainAudioAdapter(Activity context, File[] files) {
        mContext = context;
        this.mFiles = files;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mFiles.length;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_item_audio, container, false);
        ImageView videoItem = (ImageView) itemView.findViewById(R.id.videoItem);
        videoItem.setImageResource(R.drawable.ic_audio);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri VideoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", mFiles[position]);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setDataAndType(VideoURI, "audio/*");
                try {
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_LONG).show();
                }
            }
        });
        container.addView(itemView);
        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

