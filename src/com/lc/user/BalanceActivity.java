package com.lc.user;

import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BalanceActivity extends Activity implements OnClickListener {
	public final static int PAY=1;
	 TextView tvTitle,righttext,tvaccount;
	 ImageView ivleft;
	 
	 private RelativeLayout rls;
	 private RelativeLayout detail;
	 Button charge;
	 String getBalance="";
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_balance);
        init();

	}
	void init(){
		Bundle extras = getIntent().getExtras();
	    if(extras != null){
	    	getBalance = extras.getString("account");
		    if(extras.containsKey("back")){
		    	System.out.println("***BalanceActivity");
		    	Intent intent = new Intent();
				setResult(RESULT_OK, intent); 
	            finish();   
		    } 
	    }
		ExitApplication.getInstance().addActivity(this);
		tvaccount = (TextView) findViewById(R.id.account);
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("余额");
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		detail = (RelativeLayout) findViewById(R.id.moresafty);
		detail.setOnClickListener(this);
		charge = (Button) findViewById(R.id.Search);
		charge.setOnClickListener(this);
		tvaccount.setText(getBalance+"元");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.moresafty:
			Intent intent = new Intent();
			intent.setClass(getApplication(), BalanceDetailActivity.class);
			startActivity(intent);
			break;
		case R.id.Search:
			Intent intent2 = new Intent();
			intent2.setClass(getApplication(), ChargeActivity.class);
			startActivityForResult(intent2,PAY);
			break;
		default:
			break;
		}
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if (requestCode == PAY && resultCode == RESULT_OK) {        	 
            System.out.println("*****");
        	Intent intent = new Intent();
			setResult(RESULT_OK, intent); 
            finish();
        }  
    }
}
