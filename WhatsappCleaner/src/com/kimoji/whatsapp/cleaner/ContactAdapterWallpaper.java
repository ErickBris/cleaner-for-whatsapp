package com.kimoji.whatsapp.cleaner;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import com.kaavu.whatsappcleaner.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ContactAdapterWallpaper extends BaseAdapter{

	ImageView imgIcon ;
	ArrayList<WhatsppImages> myList = new ArrayList<WhatsppImages>(); 
	LayoutInflater inflater; 
	Context context;
	
	WallPaperActivity activity;
	String fragmentName;
	ImageLoader mImageLoader;

	public ContactAdapterWallpaper(Activity activity,ArrayList<WhatsppImages> myList2,ImageLoader mImageLoader2, String string) {

		this.mImageLoader=mImageLoader2;
		this.myList = myList2; 
		this.activity=(WallPaperActivity) activity;
		this.fragmentName=string;
		inflater = LayoutInflater.from(activity);
	}
	@Override
	public int getCount() {
		return myList.size();
	}

	public WhatsppImages getItem(int position) {

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
				public void onClick(View v) 
				{	
					
					activity.displayInterstitial();
					final int pos=(Integer) v.getTag();
					Timer mTimer=new Timer();
					mTimer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_VIEW);
							
							intent.setDataAndType(Uri.parse("file://" + myList.get(pos).getPath()), "image/*");
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
			
//			mViewHolder.mImageView.setImageURI(myList.get(position).getPath());
//			mViewHolder.mImageView.setImageURI(Uri.parse(myList.get(position).getPath()));
//			
//			  final String uri =Uri.parse(myList.get(position).getPath()).toString();
//			    final String decoded = Uri.decode(uri);
			mImageLoader.displayImage("file://"+myList.get(position).getPath(),
				     mViewHolder.mImageView);
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
		
//		mViewHolder.mImageView.setImageBitmap(myList.get(position).getPhoto());
//		mViewHolder.mCheckBox.setChecked(myList.get(position).isSelected());
	
//		mViewHolder.mCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() 
//        {           
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
//            {
//                myList.get(position).setSelected(isChecked);
//            }
//        });
		
		return convertView;
	}
	private class MyViewHolder
	{ 
		ImageView mImageView;
		ImageView mImageViewCheck;
	} 
}
