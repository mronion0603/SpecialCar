package com.lc.specialcar;


import com.lc.specialcar.R;
import com.lc.user.Login2Activity;
import com.lc.user.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;



public class ChooseUserActivity extends Activity {

	//private TextView title;
	//private ImageView backbt;
	private Button login1,login2;
	 //private RelativeLayout rls;
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_chooseuser);
       
        init();
      
       
	}
	public void init(){
		
		/*title = (TextView)findViewById(R.id.topTv);
		title.setText("用户类型");
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
		backbt = (ImageView)findViewById(R.id.ArrowHead);
		backbt.setVisibility(View.VISIBLE);
		*/
		login1 = (Button)findViewById(R.id.Login1);
		login1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Editor sharedata2 = getSharedPreferences("userinfo", 0).edit();  
			    sharedata2.putString("usertype","1");    	//1表示公务员
				sharedata2.commit();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), Login2Activity.class);
				startActivity(intent);
				
				
			}
		});
		login2 = (Button)findViewById(R.id.Login2);
		login2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				Editor sharedata2 = getSharedPreferences("userinfo", 0).edit();  
			    sharedata2.putString("usertype","2");    	//2表示普通用户
				sharedata2.commit();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
				
			}
		});
		
	}
	
	

	
	
    @Override
	  protected void onDestroy() {
	        super.onDestroy();
	        
	    }

}
