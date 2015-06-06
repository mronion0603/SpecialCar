package com.lc.user;

import java.util.Timer;
import java.util.TimerTask;






import com.lc.setting.CommonUtil;
import com.lc.specialcar.MainActivity;
import com.lc.specialcar.R;

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
	private String getCodeStr="";
	//private Handler reghandler;
	
	// private RelativeLayout rls;
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_login2);
       
        init();
      
       
	}
	public void init(){
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

				Intent intent = new Intent();
				intent.setClass(getApplication(), MainActivity.class);
				startActivity(intent);
			}
		});
		
	}
	

    @Override
	  protected void onDestroy() {
	        super.onDestroy();
	        
	    }

}
