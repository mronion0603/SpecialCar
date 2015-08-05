package com.lc.intercity;

import com.lc.specialcar.MainActivity;
import com.lc.specialcar.R;
import com.lc.specialcar.SplashActivity;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SignUpActivity extends Activity implements OnClickListener {
	private final static int FINISH = 1000;
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
		ExitApplication.getInstance().addActivity(this);
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
		mHandler.sendEmptyMessageDelayed(FINISH,3000);

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.rlslidemenu:
			Intent intent = new Intent();
	        setResult(RESULT_OK, intent); 
			finish();
			break;
			
		default:
			break;
		}
	}

	public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
            case FINISH:
            {   Intent intent = new Intent();
	            setResult(RESULT_OK, intent); 
            }   finish();
                break;
            }
        }
	};
}
