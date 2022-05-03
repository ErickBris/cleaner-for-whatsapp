package com.kimoji.whatsapp.cleaner;

import java.io.File;
import java.util.ArrayList;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.kaavu.whatsappcleaner.R;

public class WhatsAppVideoAdapter extends BaseAdapter{

	ImageView imgIcon ;
	ArrayList<WhatsppVideos> myList = new ArrayList<WhatsppVideos>(); 
	LayoutInflater inflater; 
	Context context;
	WhatsAppVideoActivity activity;
	String fragmentName;

	public WhatsAppVideoAdapter(Activity context2,ArrayList<WhatsppVideos> myList2,String string) {

		this.myList = myList2; 
		this.activity=(WhatsAppVideoActivity) context2;
		this.fragmentName=string;
		inflater = LayoutInflater.from(activity);
	}
	@Override
	public int getCount() {
		return myList.size();
	}

	public WhatsppVideos getItem(int position) {

		return myList.get(position);
	}


	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		final MyViewHolder mViewHolder;   
		if(convertView == null) 
		{ 	

			mViewHolder = new MyViewHolder(); 
			convertView = inflater.inflate(R.layout.myprofile_grid2, null);

			mViewHolder.mImageView=(ImageView)convertView.findViewById(R.id.imageview);
			mViewHolder.mImageViewCheck=(ImageView)convertView.findViewById(R.id.image_check);
			mViewHolder.mImageView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) 
				{
					int pos=(Integer) v.getTag();
					if(!myList.get(pos).isSelected())
					{
						myList.get(pos).setSelected(true);
						mViewHolder.mImageViewCheck.setVisibility(View.VISIBLE);
						activity.setCheckStatus();
					}
					else
					{
						myList.get(pos).setSelected(false);
						mViewHolder.mImageViewCheck.setVisibility(View.GONE);
						activity.setCheckStatus();
					}
					Log.e("button clicked", "yes");
					return true;
				}
			});
			mViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) 
				{	

					activity.displayInterstitial();
					
					final int pos=(Integer) v.getTag();
					Timer mTimer=new Timer();
					mTimer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myList.get(pos).getPath()));
							intent.setDataAndType(Uri.parse(myList.get(pos).getPath()), "video/mp4");
							activity.startActivity(intent);
							
						}
					}, 1000);
					
				}
			});
			convertView.setTag(mViewHolder);
		} 
		else
		{
			mViewHolder = (MyViewHolder) convertView.getTag();

		}
		mViewHolder.mImageView.setTag(position);
		if(!myList.get(position).getPath().equalsIgnoreCase(""))
		{
			mViewHolder.mImageView.setImageBitmap(myList.get(position).getBitmap());
		}
		else
		{
			mViewHolder.mImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ab_background_textured_example));
		}
		if(myList.get(position).isSelected())
		{
			mViewHolder.mImageViewCheck.setVisibility(View.VISIBLE);
		}
		else
		{
			mViewHolder.mImageViewCheck.setVisibility(View.GONE);
		}


		return convertView;
	}
	private class MyViewHolder
	{ 
		TextView mTextViewName;
		ImageView mImageView;
		ImageView mImageViewCheck;
	} 
}
