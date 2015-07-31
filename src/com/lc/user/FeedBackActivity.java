package com.lc.user;
import org.json.JSONObject;

import com.lc.net.FeedBackNet;
import com.lc.progressbutton.CircularProgressButton;
import com.lc.specialcar.R;


import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBackActivity extends Activity implements OnClickListener {
	private TextView tvTitle;
	private ImageView ivleft;
    private RelativeLayout rls;
    FeedBackNet feedBackNet = new FeedBackNet();
    CircularProgressButton submit;
    EditText etcontent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_feedback);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("意见反馈");
		etcontent = (EditText) findViewById(R.id.feedbackET);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		submit=(CircularProgressButton) findViewById(R.id.Search);
		submit.setOnClickListener(this);
		submit.setIndeterminateProgressMode(true);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.Search:{
			String content = etcontent.getText().toString();
			if(content==null|content.length()<=0){
			   Toast.makeText(FeedBackActivity.this,"内容不能为空", Toast.LENGTH_LONG).show();
			}else{
			   submit.setClickable(false);
			   submit.setProgress(50);	
			   feedBackNet.setHandler(mHandler);
			   feedBackNet.setDevice(Global.DEVICE);
			   feedBackNet.setContent(content);
			   feedBackNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
			   feedBackNet.addOrderServer();
			}
		}break;	
		default:
			break;
		}
	 }
	 @SuppressLint("HandlerLeak")
		private Handler mHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch(msg.what) { 
		            case Global.ADDFEEDBACK:{
		            	try{
							parseJSON((String)msg.obj);
						} catch (Exception e) {	
							e.printStackTrace();
						}      	
		            break;
	                }
	            }
	    }};
	    private void parseJSON(String str)throws Exception{  
	    	JSONObject jsonobj = new JSONObject(str); 
	    	int result = jsonobj.getInt("ResultCode");
	   	    if(result==Global.SUCCESS){
	   	      Toast.makeText(FeedBackActivity.this,"提交成功", Toast.LENGTH_LONG).show();
	   	      finish();
	        }else{
	          submit.setClickable(true);
		      submit.setProgress(0);	
	          Toast.makeText(FeedBackActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	        } 
	   }
}
