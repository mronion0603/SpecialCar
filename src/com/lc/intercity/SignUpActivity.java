package com.lc.intercity;

import com.lc.specialcar.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class SignUpActivity extends Activity implements OnClickListener {
	
    TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_signup);
		init();
		
	}

	public void init(){
		String getTitle="";
		Bundle extras = getIntent().getExtras();
        if(extras != null){
        	getTitle = extras.getString("title");
        }
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText(getTitle);
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
