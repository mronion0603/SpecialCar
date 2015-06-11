package com.lc.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.lc.innercity.AddressActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.TypeAddressActivity;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class BalanceActivity extends Activity implements OnClickListener {
	 TextView tvTitle,righttext;
	 ImageView ivleft;
	 
	 private RelativeLayout rls;
	 private RelativeLayout detail;
	 Button charge;
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_balance);
        init();

	}
	void init(){
		ExitApplication.getInstance().addActivity(this);
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
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
}
