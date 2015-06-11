package com.lc.user;

import java.util.Timer;
import java.util.TimerTask;







import com.lc.specialcar.R;
import com.lc.utils.CommonUtil;
import com.lc.utils.ExitApplication;

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


public class RegisterActivity extends Activity {

	private TextView title;
	private ImageView backbt;
	private Button nextStep,getCode;
	private EditText phoneET,codeET;
	private String phoneNum="";
	private String getCodeStr="";
	private Handler reghandler;
	private Timer timer = new Timer(); 
	private int recLen=30;
	private boolean startTimer = false;
	 private RelativeLayout rls;
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_register);
       
        init();
      
       
	}
	public void init(){
		ExitApplication.getInstance().addActivity(this);
		phoneET = (EditText)findViewById(R.id.PhoneNumber);
		codeET = (EditText)findViewById(R.id.inputCode);
		title = (TextView)findViewById(R.id.topTv);
		title.setText("注册");
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
		backbt = (ImageView)findViewById(R.id.ArrowHead);
		backbt.setVisibility(View.VISIBLE);
		
		getCode = (Button)findViewById(R.id.getCode);
		getCode.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				phoneNum= phoneET.getText().toString();
				if(phoneNum.length()<=0){
					Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
				}else if(CommonUtil.isMobileNO(phoneNum)){
					recLen=30;
					if(!startTimer){
					timer.schedule(task, 1000, 1000);       // timeTask  
					}
					
					startTimer = true;
				}else{
					Toast.makeText(RegisterActivity.this, "不是手机号", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		nextStep = (Button)findViewById(R.id.NextStep);
		nextStep.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {			

				finish();
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
                        getCode.setBackgroundResource(R.drawable.rectangle);
                    }  
                }  
            });  
        }  
    };  
    @Override
	  protected void onDestroy() {
	        super.onDestroy();
	        timer.cancel();
	    }

}
