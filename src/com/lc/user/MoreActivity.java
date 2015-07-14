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

public class MoreActivity extends Activity implements OnClickListener {
	TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;
    Button ivSearch;
    private RelativeLayout rl1,rl2,rl3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_more);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("更多");
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		rl1 = (RelativeLayout) findViewById(R.id.morenotify);
		rl1.setOnClickListener(this);
		rl2 = (RelativeLayout) findViewById(R.id.usecardate);
		rl2.setOnClickListener(this);
		rl3 = (RelativeLayout) findViewById(R.id.usecardate2);
		rl3.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.morenotify:
		{	
			Intent intent = new Intent();
			intent.setClass(getApplication(), AgreementActivity.class);
			startActivity(intent);
		}	break;
		case R.id.usecardate:
		{	Intent intent = new Intent();
		    intent.setClass(getApplication(), AboutActivity.class);
		    startActivity(intent);
		}	break;
		case R.id.usecardate2:
		{	Intent intent = new Intent();
	        intent.setClass(getApplication(), FeedBackActivity.class);
	        startActivity(intent);
		}	break;
		case R.id.Search:
			MySharePreference.clearPersonal(getApplication());
			finish();
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ChooseUserActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
