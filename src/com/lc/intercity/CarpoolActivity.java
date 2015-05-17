package com.lc.intercity;

import com.lc.setting.ButtonEffect;
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




public class CarpoolActivity extends Activity implements OnClickListener {
	
    TextView tvTitle;
    Button ivSearch;
    ImageView ivleft;
    private RelativeLayout rls;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_carpool);
		init();
		
	}

	public void init(){
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("拼车");
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
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
		case R.id.Search:
			Intent intent = new Intent();
			intent.putExtra("title", "拼车");
			intent.setClass(getApplication(), SignUpActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	
}
