package com.lc.user;

import com.lc.specialcar.ChooseUserActivity;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;
import com.lc.utils.MySharePreference;

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





public class AgreementActivity extends Activity implements OnClickListener {
	TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_agreement);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("用户协议");
		
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		default:
			break;
		}
	}

}
