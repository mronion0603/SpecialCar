package com.lc.specialcar;


import com.lc.specialcar.R;
import com.lc.user.Login2Activity;
import com.lc.user.LoginActivity;
import com.lc.utils.ExitApplication;
import com.lc.utils.MyApplication;
import com.lc.utils.MySharePreference;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ChooseUserActivity extends Activity {
	private Button login1,login2;
	private long waitTime = 3000;  //退出按钮等待时间
    private long touchTime = 0;    //退出按钮记录按下时间    
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_chooseuser); 
        init();
       
	}
	public void init(){
		ExitApplication.getInstance().addActivity(this);
		Bundle extras = getIntent().getExtras();
	    if(extras != null){
	       if(extras.containsKey("getoff")){
	    	   new AlertDialog.Builder(ChooseUserActivity.this)
		    	 .setTitle("警告") 
		    	 .setMessage("该账号在别的设备登录，您已被强制下线！")
		    	 .setPositiveButton("确定",null)
		    	 .show();
	       } 
	    }
	    
		login1 = (Button)findViewById(R.id.Login1);
		login1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				MySharePreference.editStringValue(getApplication(),MySharePreference.USER_TYPE,"1");
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), Login2Activity.class);
				startActivity(intent);
			}
		});
		login2 = (Button)findViewById(R.id.Login2);
		login2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				MySharePreference.editStringValue(getApplication(),MySharePreference.USER_TYPE,"2");
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
			}
		});
	}
	@Override  
    public void onBackPressed() {  
        long currentTime = System.currentTimeMillis();  
        if((currentTime-touchTime)>=waitTime) {  
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();  
            touchTime = currentTime;  
        }else {  
        	ExitApplication.getInstance().exit();
           // finish();  
        }  
    }  
}
