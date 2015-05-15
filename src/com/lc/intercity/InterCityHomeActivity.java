package com.lc.intercity;

import com.lc.specialcar.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;




public class InterCityHomeActivity extends Activity implements OnClickListener {
	
    TextView tvTitle;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_home);
		init();
		
	}

	public void init(){
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("城际合约");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	
}
