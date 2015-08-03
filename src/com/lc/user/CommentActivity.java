package com.lc.user;
import org.json.JSONObject;

import com.lc.net.AddCommentNet;
import com.lc.progressbutton.CircularProgressButton;
import com.lc.specialcar.R;


import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends Activity implements OnClickListener {
	private TextView tvTitle,tvscore;
	private ImageView ivleft;
    private RelativeLayout rls;
    AddCommentNet addCommentNet = new AddCommentNet();
    CircularProgressButton submit;
    EditText etcontent;
    RatingBar rb;
    String orderNum="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_comment);
		init();
	}

	public void init(){
		Bundle extras = getIntent().getExtras();
	    if(extras != null){
	    	orderNum = extras.getString("OrderNum");
	    }
		ExitApplication.getInstance().addActivity(this);
		rb= (RatingBar) findViewById(R.id.ratingBar1);
		tvscore = (TextView) findViewById(R.id.tvscore);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("评价");
		etcontent = (EditText) findViewById(R.id.feedbackET);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		submit=(CircularProgressButton) findViewById(R.id.Search);
		submit.setOnClickListener(this);
		submit.setIndeterminateProgressMode(true);
		rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){
			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				tvscore.setText(String.valueOf(rb.getRating())+"分");
			}
		});
		float score = 5.0f;
		rb.setRating(score);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.Search:{
			String content = etcontent.getText().toString();
			if(content==null|content.length()<=0){
			   Toast.makeText(CommentActivity.this,"内容不能为空", Toast.LENGTH_LONG).show();
			}else{
			   submit.setClickable(false);
			   submit.setProgress(50);	
			   addCommentNet.setHandler(mHandler);
			   addCommentNet.setDevice(Global.DEVICE);
			   addCommentNet.setcontent(content);
			   addCommentNet.setorderNum(orderNum);
			   addCommentNet.setAuth(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
			   addCommentNet.setScore(String.valueOf(rb.getRating()));
			   addCommentNet.getDataFromServer();
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
		            case Global.ADDSCORE:{
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
	   	      Toast.makeText(CommentActivity.this,"提交成功", Toast.LENGTH_LONG).show();
	   	      Intent intent = new Intent();
    	      setResult(RESULT_OK, intent); 
	   	      finish();
	        }else{
	          submit.setClickable(true);
		      submit.setProgress(0);	
	          Toast.makeText(CommentActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	        } 
	   }
}
