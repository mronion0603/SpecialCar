package com.lc.user;

import java.util.Timer;
import java.util.TimerTask;











import org.json.JSONException;
import org.json.JSONObject;

import com.lc.net.LoginNet;
import com.lc.net.LogincivilNet;
import com.lc.specialcar.MainActivity;
import com.lc.specialcar.R;
import com.lc.utils.CommonUtil;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Login2Activity extends Activity {

	//private TextView title;
	private Button backbt;
	private Button nextStep;
	private EditText phoneET,codeET;
	private String phoneNum="";
	LogincivilNet loginNet;
	
	// private RelativeLayout rls;
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
		
		backbt = (Button)findViewById(R.id.back);
		backbt.setVisibility(View.VISIBLE);
		backbt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
		
		nextStep = (Button)findViewById(R.id.NextStep);
		nextStep.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {			
				phoneNum=phoneET.getText().toString();
				loginNet.setHandler(handler);
				loginNet.setPhone(phoneNum);
				loginNet.setDevice(Global.DEVICE);
				loginNet.setPassword(codeET.getText().toString());
				loginNet.getDataFromServer();
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
		                 System.out.println((String)msg.obj);
		            	 if(result==Global.SUCCESS){
		            		String getauthn = jsonobj.getJSONObject("Data").getString("authn");
		            		MySharePreference.editStringValue(getApplication(),MySharePreference.PHONE,phoneNum);
		            		MySharePreference.editStringValue(getApplication(),MySharePreference.AUTHN,getauthn);
		            		Intent intent = new Intent();
		     				intent.setClass(getApplication(), MainActivity.class);
		     				startActivity(intent);
		    				finish();
		                 }else{
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
