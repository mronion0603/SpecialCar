package com.lc.innercity;


import org.json.JSONException;
import org.json.JSONObject;

import com.lc.net.CancelInnerNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CancelOrderActivity extends Activity implements OnClickListener {
    TextView tvTitle,righttext;
    ImageView ivleft;
    private RelativeLayout rls;
    String orderNum="";
    CancelInnerNet cancelInnerNet = new CancelInnerNet();
    EditText etreason;
    String serTypeId="市内约租";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);    
		setContentView(R.layout.innercity_cancelorder);
		init();
	}

	public void init(){
		Bundle extras = getIntent().getExtras();
	    if(extras != null){
	    	orderNum = extras.getString("OrderNum");
	    	if(extras.containsKey("SerTypeId")){
	    		serTypeId = extras.getString("SerTypeId");
	    	}
	    }
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("取消订单");
		etreason= (EditText) findViewById(R.id.feedbackET);
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("提交");
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
		case R.id.righttext:
			cancelInnerNet.setHandler(mHandler);
			cancelInnerNet.setOrderNum(orderNum);
			cancelInnerNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
			cancelInnerNet.setReason(etreason.getText().toString());
			if(serTypeId.equals("城际约租")){
				System.out.println("ssss");
				cancelInnerNet.getCancelCityServer();
			}else{
			    cancelInnerNet.getDataFromServer();
			}
			break;
		default:
			break;
		}
	}
	
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
            case Global.CANCEL_INNER:{
            	JSONObject jsonobj;
				try {
					jsonobj = new JSONObject((String)msg.obj);
					int result = jsonobj.getInt("ResultCode");
	           	    if(result==Global.SUCCESS){
	           	      Toast.makeText(CancelOrderActivity.this, "取消订单成功", Toast.LENGTH_LONG).show();
	           	      Intent intent = new Intent();
	           	      setResult(RESULT_OK, intent); 
	           	      finish();
	                }else{
	                   Toast.makeText(CancelOrderActivity.this, jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	                } 
				} catch (JSONException e) {
					e.printStackTrace();
				}  
                
       	
                break;
                }
            }}
            };
}
