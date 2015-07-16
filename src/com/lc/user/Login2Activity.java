package com.lc.user;

import org.json.JSONException;
import org.json.JSONObject;

import com.lc.net.LogincivilNet;
import com.lc.progressbutton.CircularProgressButton;
import com.lc.specialcar.MainActivity;
import com.lc.specialcar.R;
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


public class Login2Activity extends Activity {

	//private TextView title;
	private TextView backbt;
	private CircularProgressButton nextStep;
	private EditText phoneET,codeET;
	private String phoneNum="";
	LogincivilNet loginNet;
	
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_login2);
        init();  
	}
	public void init(){
		loginNet = new LogincivilNet();
		ExitApplication.getInstance().addActivity(this);
		phoneET = (EditText)findViewById(R.id.PhoneNumber);
		codeET = (EditText)findViewById(R.id.inputCode);
		
		backbt = (TextView)findViewById(R.id.back);
		backbt.setVisibility(View.VISIBLE);
		backbt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
		
		nextStep = (CircularProgressButton)findViewById(R.id.circularButton1);
		nextStep.setIndeterminateProgressMode(true);
		nextStep.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {			
				phoneNum=phoneET.getText().toString();
				if(phoneNum.length()<=0){
					Toast.makeText(Login2Activity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
				}else {
					nextStep.setClickable(false);
					nextStep.setProgress(50);
					loginNet.setHandler(handler);
					loginNet.setPhone(phoneNum);
					loginNet.setDevice(Global.DEVICE);
					loginNet.setPassword(codeET.getText().toString());
					loginNet.getDataFromServer();
				}
			}
		});
		
	}
	
	public Handler handler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.CIVILLOGIN:{
	            	try {
		            	 JSONObject jsonobj = new JSONObject((String)msg.obj);  
		                 int result = jsonobj.getInt("ResultCode");
		            	 if(result==Global.SUCCESS){
		            		nextStep.setProgress(100);
		            		String getauthn = jsonobj.getJSONObject("Data").getString("authn");
		            		MySharePreference.editStringValue(getApplication(),MySharePreference.PHONE,phoneNum);
		            		MySharePreference.editStringValue(getApplication(),MySharePreference.AUTHN,getauthn);
		            		String getuuid = jsonobj.getJSONObject("Data").getString("uuid");     
		            		MySharePreference.editStringValue(getApplication(),MySharePreference.UUID,getuuid);
		            		//
		            		Intent intent = new Intent();
		     				intent.setClass(getApplication(), MainActivity.class);
		     				startActivity(intent);
		    				finish();
		                 }else{
		                	nextStep.setProgress(-1);
		                	nextStep.setClickable(true);
		                    Toast.makeText(Login2Activity.this, jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
		                 } 
	            	} catch (JSONException e) {
						e.printStackTrace();
					}  
	             break;
                }
            }
    }};

}
