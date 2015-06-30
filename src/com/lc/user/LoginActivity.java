package com.lc.user;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.lc.net.GetCode;
import com.lc.net.LoginNet;
import com.lc.specialcar.MainActivity;
import com.lc.specialcar.R;
import com.lc.utils.CommonUtil;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {

	//private TextView title;
	private TextView backbt;
	private Button nextStep,getCode;
	private EditText phoneET,codeET;
	private String phoneNum="";
	private String getCodeStr="";

	private Timer timer = new Timer(); 
	private int recLen=30;
	private boolean startTimer = false;
	GetCode getCodeNet;
	LoginNet loginNet;
	// private RelativeLayout rls;
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_login);
       
        init();
	}
	public void init(){
		getCodeNet = new GetCode();
		loginNet = new LoginNet();
		ExitApplication.getInstance().addActivity(this);
		phoneET = (EditText)findViewById(R.id.PhoneNumber);
		codeET = (EditText)findViewById(R.id.inputCode);
		
		backbt = (TextView)findViewById(R.id.back);
		backbt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {			
				finish();
			}
		});
		getCode = (Button)findViewById(R.id.getCode);
		getCode.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				phoneNum= phoneET.getText().toString();
				if(phoneNum.length()<=0){
					Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
				}else {
					recLen=30;
					if(!startTimer){
					timer.schedule(task, 1000, 1000);       // timeTask  
					}
					getCodeNet.setHandler(handler);
					getCodeNet.setPhone(phoneNum);
					getCodeNet.getCodeFromServer();
					startTimer = true;
				}//else{
				//	Toast.makeText(LoginActivity.this, "不是手机号", Toast.LENGTH_SHORT).show();
				//}
			}
		});
		nextStep = (Button)findViewById(R.id.NextStep);
		nextStep.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				getCodeStr = codeET.getText().toString();
				loginNet.setHandler(handler);
				loginNet.setPhone(phoneNum);
				loginNet.setDevice(Global.DEVICE);
				loginNet.setAuthCode(getCodeStr);
				loginNet.getDataFromServer();
			}
		});
		
	}
	
	TimerTask task = new TimerTask() {  
        @Override  
        public void run() {  
  
            runOnUiThread(new Runnable() {      // UI thread  
                @Override  
                public void run() {  
                    recLen--;  
                    getCode.setText(""+recLen);  
                    getCode.setClickable(false);
                    getCode.setBackgroundResource(R.drawable.btn2);
                    if(recLen < 0){  
                        //timer.cancel();  
                        // timetv.setVisibility(View.GONE);  
                        getCode.setText("获取验证码");
                        getCode.setClickable(true);
                        getCode.setBackgroundResource(R.drawable.rectangleright);
                    }  
                }  
            });  
        }  
    };  
    
	public Handler handler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.GETCODE:{
	            	try {
		            	 JSONObject jsonobj = new JSONObject((String)msg.obj);  
		                 int result = jsonobj.getInt("ResultCode");
		            	 if(result==Global.SUCCESS){
		                   Toast.makeText(LoginActivity.this,  jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
		                 }else{
		                    Toast.makeText(LoginActivity.this,  jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
		                 } 
	            	} catch (JSONException e) {
						e.printStackTrace();
					}  
	             break;
                }
	            case Global.LOGIN:{
	            	try {
		            	 JSONObject jsonobj = new JSONObject((String)msg.obj);  
		                 int result = jsonobj.getInt("ResultCode");
		                 System.out.println((String)msg.obj);
		            	 if(result==Global.SUCCESS){
		            		String getauthn = jsonobj.getJSONObject("Data").getString("authn");
		            		//System.out.println("getauthn"+getauthn);
		            		MySharePreference.editStringValue(getApplication(),MySharePreference.PHONE,phoneNum);
		            		MySharePreference.editStringValue(getApplication(),MySharePreference.AUTHN,getauthn);
		            		//System.out.println("getauthn2"+MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		            		Intent intent = new Intent();
		     				intent.setClass(getApplication(), MainActivity.class);
		     				startActivity(intent);
		    				finish();
		                 }else{
		                    Toast.makeText(LoginActivity.this, jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
		                 } 
	            	} catch (JSONException e) {
						e.printStackTrace();
					}  
	             break;
                }
            }
    }};
    @Override
	  protected void onDestroy() {
	        super.onDestroy();
	        timer.cancel();
	    }

}
