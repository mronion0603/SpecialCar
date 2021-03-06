package com.lc.user;


import org.json.JSONObject;

import com.lc.net.GetCardNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Card2Activity extends Activity implements OnClickListener {
	TextView tvTitle,tv1,tv2,tvdeal;
    ImageView ivleft;
    private RelativeLayout rls;
    GetCardNet getCardNet = new GetCardNet();
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_card2);
		init();
		
	}
	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("绑定信用卡");
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		tvdeal = (TextView) findViewById(R.id.deal);
		tvdeal.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
		tvdeal.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.deal:{
		Intent intent = new Intent();
		intent.setClass(getApplication(), DealActivity.class );
		startActivity(intent);
		}break;
		default:
			break;
		}
	}
	 @SuppressLint("HandlerLeak")
		private Handler mHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch(msg.what) { 
		            case Global.GETCARD:{
		            	try{
							parseJSON((String)msg.obj);
						} catch (Exception e) {	
							e.printStackTrace();
						}      	
		            break;
	                }
		           
	            }
	    }};
	    private void parseJSON(String str)throws Exception{  
	    	System.out.println(str);
	    	JSONObject jsonobj = new JSONObject(str); 
	    	int result = jsonobj.getInt("ResultCode");
	   	    if(result==Global.SUCCESS){
	   	    	JSONObject jsonobj2 = jsonobj.getJSONObject("Data");
	   	    	tv1.setText(jsonobj2.getString("company"));
	   	    	//tv2.setText(jsonobj.getString("company"));
	        }else{
	          Toast.makeText(Card2Activity.this,"已是最新版本，不需要更新", Toast.LENGTH_LONG).show();
	        } 
	   }
	
}
