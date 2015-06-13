package com.lc.specialcar;

import com.lc.utils.MySharePreference;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
 
/**
 * 
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入引导activity；否，则进入MainActivity
 * (3)5s后执行(2)操作
 * 
 * @author sz082093
 *
 */
public class SplashActivity extends Activity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
        setContentView(R.layout.activity_splash);
        boolean mFirst = isLogin();
        if(mFirst)
        	 mHandler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY,2000);
        else
        	 mHandler.sendEmptyMessageDelayed(SWITCH_LOGINACTIVITY,2000);
    }   
     
    //****************************************************************
    // 判断应用是否初次加载，读取SharedPreferences中的isfirst字段
    //****************************************************************
    /*
    private boolean isFirstEnter(){
    	 String isfirst = "false";
    	 SharedPreferences sharedata3;    //记录是否接收推送
         if(getSharedPreferences("common", 0)!=null){
  		   sharedata3 = getSharedPreferences("common", 0); 
  		   if(sharedata3.getString("isfirst",null)!=null){
  		      isfirst = sharedata3.getString("isfirst", null);
  		      System.out.println("isfirst:"+isfirst);
  		   }
  		}
        if(isfirst.equals("false")){
        	Editor sharedata2 = getSharedPreferences("common", 0).edit();  
        	sharedata2.putString("isfirst","true");
        	sharedata2.commit();
        	System.out.println("isfirst:"+isfirst);
        	return false;
        }else{
        	System.out.println("isfirst:"+isfirst);
        	return true;
        }
       
    }
     */
    private boolean isLogin(){
   	 boolean islogin = false;
     if( MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE)!=null){	   
        	islogin = true; 
     }
       	return islogin;
    }
    //*************************************************
    // Handler:跳转至不同页面
    //*************************************************
    private final static int SWITCH_MAINACTIVITY = 1000;
    private final static int SWITCH_GUIDACTIVITY = 1001;
    private final static int SWITCH_LOGINACTIVITY = 1002;
    @SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
            case SWITCH_MAINACTIVITY:
            { Intent mIntent = new Intent();
                mIntent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(mIntent);
            }   finish();
                break;
            case SWITCH_GUIDACTIVITY:
            	/*
                Intent mIntent = new Intent();
                mIntent.setClass(SplashActivity.this, GuideActivity.class);
                startActivity(mIntent);
                finish();
                */
                break;
            case SWITCH_LOGINACTIVITY:
            {  
            	Intent mIntent = new Intent();
                mIntent.setClass(SplashActivity.this, ChooseUserActivity.class);
                startActivity(mIntent);
                finish();
            }   
                break;
            }
            super.handleMessage(msg);
        }
    };
}