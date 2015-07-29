package com.lc.user;


import org.json.JSONObject;

import com.lc.net.GetCardNet;
import com.lc.net.UpdateNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CardActivity extends Activity implements OnClickListener {
	TextView tvTitle,tv1,tv2;
    ImageView ivleft;
    private RelativeLayout rls;
    GetCardNet getCardNet = new GetCardNet();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_card);
		init();
		
	}
	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("公务卡");
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		tv1 = (TextView) findViewById(R.id.arrow2);
		tv2 = (TextView) findViewById(R.id.arrow3);
		getCardNet.setHandler(mHandler);
		getCardNet.setDevice(Global.DEVICE);
		getCardNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		getCardNet.getCodeFromServer();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		
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
	   	    	tv2.setText(jsonobj2.getString("deptName"));
	        }else{
	          Toast.makeText(CardActivity.this,"已是最新版本，不需要更新", Toast.LENGTH_LONG).show();
	        } 
	   }
	
}
