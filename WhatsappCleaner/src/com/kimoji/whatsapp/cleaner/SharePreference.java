package com.kimoji.whatsapp.cleaner;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharePreference {

	public static String SHAREPREFERENCE = "UserDetail";
	SharedPreferences mSharedPreferences;
	SharedPreferences.Editor mEditor;

	Context mContext;

	public String mStringGroups="";

	public static String GROUPS = "groups";

	public static String USERNAME = "username";
	public static String USEREMAIL = "useremail";
	public static String USERPASSWORD = "userpassword";
	public static String USERID = "userid";
	public static String COMMENTADDED = "comment";
	public static String RATINGCHANGED = "ratingchanged";
	public static String FACEBOOKTOKEN="facebooktoken";
	public static String PURCHASE="purchase";
	public static String PURCHASE_50="purchase50";
	public static String PURCHASE_100="purchase100";
	
	public String mStringUserName="";
	public String mStringUserEmail="";
	public String mStringUserPassword="";
	public String mStringUserId="";
	public String mStringFacebookToken="";
	public String mStringPurchaseItem="";
	public boolean commentAdded=false;
	public boolean ratingChanged=false;

	public boolean isPuchaseItem() {
		return mSharedPreferences.getBoolean(PURCHASE, false);
	}
	public void setPuchaseItem(boolean puchaseItem) {
		open_editor();
		mEditor.putBoolean(PURCHASE, puchaseItem).commit();
		this.puchaseItem = puchaseItem;
	}
	public boolean puchaseItem=false;
	
	public String getmStringFacebookToken() {
		return mSharedPreferences.getString(FACEBOOKTOKEN, "");
	}
	public void setmStringFacebookToken(String mStringFacebookToken) 
	{
		open_editor();
		mEditor.putString(FACEBOOKTOKEN, mStringFacebookToken).commit();
		this.mStringFacebookToken = mStringFacebookToken;
	}
	public boolean isRatingChanged() {
		return mSharedPreferences.getBoolean(RATINGCHANGED, false);
	}
	public void setRatingChanged(boolean ratingChanged) {
		open_editor();
		mEditor.putBoolean(RATINGCHANGED, ratingChanged).commit();
		this.ratingChanged = ratingChanged;
	}
	public boolean isCommentAdded() {
		return mSharedPreferences.getBoolean(COMMENTADDED, false);
	}
	public void setCommentAdded(boolean commentAdded)
	{
		open_editor();
		mEditor.putBoolean(COMMENTADDED, commentAdded).commit();
		this.commentAdded = commentAdded;
	}
	public String getmStringUserId() {
		return mSharedPreferences.getString(USERID, "");
	}
	public void setmStringUserId(String mStringUserId) 
	{
		open_editor();
		mEditor.putString(USERID, mStringUserId).commit();
		this.mStringUserId = mStringUserId;
	}
	//	public String getmStringGroups() 
	//	{
	//		return mSharedPreferences.getString(GROUPS, "");
	//	}
	//	public void setmStringGroups(String mStringGroups) 
	//	{
	//		open_editor();
	//		mEditor.putString(GROUPS, mStringGroups);
	//		this.mStringGroups = mStringGroups;
	//	}
	public  SharePreference(Context con) 
	{
		mContext=con;
		mSharedPreferences=con.getSharedPreferences(SHAREPREFERENCE, 0);

	}
	public String getmStringUserName() 
	{
//		String username="";
//		try 
//		{
//			username = URLEncoder.encode(mSharedPreferences.getString(USERNAME, ""), "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		return mSharedPreferences.getString(USERNAME, "");
	}
	public void setmStringUserName(String mStringUserName) 
	{
		open_editor();
		mEditor.putString(USERNAME, mStringUserName).commit();
		this.mStringUserName = mStringUserName;
	}
	public String getmStringUserEmail() 
	{
		return mSharedPreferences.getString(USEREMAIL, "");
	}
	public void setmStringUserEmail(String mStringUserEmail) 
	{
		open_editor();
		mEditor.putString(USEREMAIL, mStringUserEmail).commit();
		this.mStringUserEmail = mStringUserEmail;
	}
	public String getmStringUserPassword() 
	{
		return mSharedPreferences.getString(USERPASSWORD, "");
	}
	public void setmStringUserPassword(String mStringUserPassword) 
	{
		open_editor();
		mEditor.putString(USERPASSWORD, mStringUserPassword).commit();
		this.mStringUserPassword = mStringUserPassword;
	}
	public void open_editor() {
		// TODO Auto-generated method stub
		mEditor=mSharedPreferences.edit();
	}

	public void clearData() {
		// TODO Auto-generated method stub
		mEditor=mSharedPreferences.edit();
		mEditor.clear();
		mEditor.commit();
	}
	
	
	
	public boolean isPuchaseItem50() {
		return mSharedPreferences.getBoolean(PURCHASE_50, false);
	}
	public void setPuchaseItem50(boolean puchaseItem) {
		open_editor();
		mEditor.putBoolean(PURCHASE_50, puchaseItem).commit();
//		this.puchaseItem = puchaseItem;
	}
	
	
	public boolean isPuchaseItem100() {
		return mSharedPreferences.getBoolean(PURCHASE_100, false);
	}
	public void setPuchaseItem100(boolean puchaseItem) {
		open_editor();
		mEditor.putBoolean(PURCHASE_100, puchaseItem).commit();
//		this.puchaseItem = puchaseItem;
	}

}
