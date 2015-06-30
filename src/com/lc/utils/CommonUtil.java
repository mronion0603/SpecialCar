package com.lc.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtil {
	
	public static boolean isMobileNO(String paramString){
		return Pattern.compile("^(\\+86)?((13[0-9])|(15[^4,\\D])|(18[0-4,5-9]))\\d{8}$").matcher(paramString).matches();
	}
	
	 @SuppressLint("SdCardPath")
	public  static void saveBitmap(Bitmap bm,String picName) {
		  //Log.e(TAG, "保存图片");
		  File destDir = new File("/sdcard/special/");
		  if (!destDir.exists()) {
		  destDir.mkdirs();
		  }
		  File f = new File("/sdcard/special/", picName);
		  if (f.exists()) {
		   f.delete();
		  }
		  try {
		   FileOutputStream out = new FileOutputStream(f);
		   bm.compress(Bitmap.CompressFormat.PNG, 90, out);
		   out.flush();
		   out.close();
		   //Log.i(TAG, "已经保存");
		  } catch (FileNotFoundException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }

		 }
	@SuppressLint("SimpleDateFormat")
	public static  String dateminus(String datestr){
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间 
		 String    str    =    df.format(curDate); 
		 try{
		   Date d1 = df.parse(datestr); 
		   Date d2 = df.parse(str);
		   long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
		   long days = diff / (1000 * 60 * 60 * 24);

		   //long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
		  // long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
		   System.out.println(""+days+"天");
		   return ""+days+"天";
		 }
		 catch (Exception e){
			 return ""+0+"天";
		 }
		 
		
	 }
	
	
	public  static boolean isNetworkConnected(Context context) { 
		if (context != null) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
		.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null) { 
		return mNetworkInfo.isAvailable(); 
		} 
		} 
		return false; 
		}
}
