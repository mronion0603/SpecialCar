package com.lc.urgent;


import com.lc.specialcar.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class UrgentHomeActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,righttext;
    ImageView ivleft;
    Button ivSearch;
    private RelativeLayout rls;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.urgent_home);
		init();
		
	}

	public void init(){
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("市内预约");
		righttext = (TextView) findViewById(R.id.righttext2);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("武汉");
		righttext.setOnClickListener(this);

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
		case R.id.righttext2:
			//Intent intent = new Intent();
			//intent.setClass(UrgentHomeActivity.this,BillingRuleActivity.class);
			//startActivity(intent);
			break;
		case R.id.Search:
			
			break;
			
		default:
			break;
		}
	}

	   
}
