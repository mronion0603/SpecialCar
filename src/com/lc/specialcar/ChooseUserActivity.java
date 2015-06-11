package com.lc.specialcar;


import com.lc.specialcar.R;
import com.lc.user.Login2Activity;
import com.lc.user.LoginActivity;
import com.lc.utils.ExitApplication;
import com.lc.utils.MySharePreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChooseUserActivity extends Activity {
	private Button login1,login2;
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_chooseuser); 
        init();
	}
	public void init(){
		ExitApplication.getInstance().addActivity(this);
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
}
