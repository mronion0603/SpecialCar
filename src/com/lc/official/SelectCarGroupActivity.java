package com.lc.official;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.lc.innercity.CarInfoActivity;
import com.lc.innercity.SelectCarActivity;
import com.lc.innercity.SendDealActivity;
import com.lc.intercity.SignUpActivity;
import com.lc.net.AddInnerNet;
import com.lc.net.AddOfficeNet;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SelectCarGroupActivity extends Activity implements OnClickListener {
    TextView tvTitle,righttext;
    ImageView ivleft,ivselect1,ivselect2,ivselect3;
    private RelativeLayout rls;
    private RelativeLayout select1;
    private RelativeLayout select2;
    private RelativeLayout select3;
    boolean flag1,flag2,flag3;
    Button btsearch;
    private Button plus,minus,plus2,minus2,plus3,minus3;
    private TextView amount,amount2,amount3;
    private AddOfficeNet addInnerNet = new AddOfficeNet();
	String vMoneystr="0";	
	String voucherNumstr="";
	String getname="",getlat="",getlont="",getaddress="",getphone="",gettime="",gettimelong="0",getdemand="";
	String economy="0",common="0",business="0";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);    
		setContentView(R.layout.official_selectgroup);
		init();
	}

	public void init(){
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			getname = extras.getString("username");
			getlat = extras.getString("sLatitude");
			getlont = extras.getString("sLongitude");
			getaddress = extras.getString("StartAddress");
			getphone = extras.getString("phone");
			gettime = extras.getString("starttime");
			gettimelong = extras.getString("timelong");
			getdemand= extras.getString("commont");
        }
		
		ExitApplication.getInstance().addActivity(this);
		flag1 = true;
		flag2 = false;
		flag3 = false;
		
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("选择车型");
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("计费规则");
		righttext.setOnClickListener(this);
		ivselect1 = (ImageView) findViewById(R.id.ivselect);
		ivselect2 = (ImageView) findViewById(R.id.ivselect2);
		ivselect3 = (ImageView) findViewById(R.id.ivselect3);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		select1 = (RelativeLayout) findViewById(R.id.select1);
		select1.setOnClickListener(this);
		select2 = (RelativeLayout) findViewById(R.id.select2);
		select2.setOnClickListener(this);
		select3 = (RelativeLayout) findViewById(R.id.select3);
		select3.setOnClickListener(this);
		btsearch = (Button) findViewById(R.id.Search);
		ButtonEffect.setButtonStateChangeListener(btsearch);
		btsearch.setOnClickListener(this);
		plus = (Button) findViewById(R.id.plus);
		minus = (Button) findViewById(R.id.minus);
		amount = (TextView)findViewById(R.id.amount);
		plus.setOnClickListener(this);
		minus.setOnClickListener(this);
		plus2 = (Button) findViewById(R.id.plus2);
		minus2 = (Button) findViewById(R.id.minus2);
		amount2 = (TextView)findViewById(R.id.amount2);
		plus2.setOnClickListener(this);
		minus2.setOnClickListener(this);
		plus3 = (Button) findViewById(R.id.plus3);
		minus3 = (Button) findViewById(R.id.minus3);
		amount3 = (TextView)findViewById(R.id.amount3);
		plus3.setOnClickListener(this);
		minus3.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
			//finish();
			Intent intent = new Intent();
			intent.setClass(SelectCarGroupActivity.this,SelectCarActivity.class);
			startActivity(intent);
			break;
		case R.id.Search:{	
			addInnerNet.setHandler(mHandler);
			addInnerNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
            addInnerNet.setDevice(Global.DEVICE);
            addInnerNet.setComment(getdemand);
            addInnerNet.setRiderName(getname);  
            addInnerNet.setRiderPhone(getphone);
            addInnerNet.setsLatitude(getlat);
            addInnerNet.setsLongitude(getlont);
            addInnerNet.setStartAddress(getaddress);
            addInnerNet.setStartTime(gettime);
            addInnerNet.setServiceTypeId("32");
            if(flag1)
            addInnerNet.setEconomy(amount.getText().toString());
            else
            addInnerNet.setEconomy(economy);
            if(flag2)
            addInnerNet.setCommon(amount2.getText().toString());
            else
            addInnerNet.setCommon(common);
            if(flag3)
            addInnerNet.setBusiness(amount3.getText().toString());
            else
            addInnerNet.setBusiness(business);
            addInnerNet.setUseCarTime(gettimelong);
            addInnerNet.getDataFromServer();
			
		}	break;
		case R.id.select1:
			if(flag1){
				flag1=false;
				ivselect1.setVisibility(View.GONE);
			}else{
				ivselect1.setVisibility(View.VISIBLE);				
				flag1=true;
			}
			break;
		case R.id.select2:
			if(flag2){
				flag2=false;
				ivselect2.setVisibility(View.GONE);
			}else{
				ivselect2.setVisibility(View.VISIBLE);
				flag2=true;	
			}
			break;
		case R.id.select3:
			if(flag3){
				flag3=false;
				ivselect3.setVisibility(View.GONE);
			}else{
				ivselect3.setVisibility(View.VISIBLE);
				flag3=true;	
			}
			break;
		case R.id.plus:{
			String content = amount.getText().toString();
			int count = Integer.parseInt(content);
			count++;
			if(count>1){
				//minus.setImageResource(R.drawable.minus);
			}
			amount.setText(count+"");}
			break;
        case R.id.minus:{
        	String content2 = amount.getText().toString();
			int count2 = Integer.parseInt(content2);
			if(count2>1){
				count2--;
			}
			if(count2==1){
				//minus.setImageResource(R.drawable.minus2);
			}
			amount.setText(count2+"");}
			break;
        case R.id.plus2:{
			String content = amount2.getText().toString();
			int count = Integer.parseInt(content);
			count++;
			if(count>1){
				//minus.setImageResource(R.drawable.minus);
			}
			amount2.setText(count+"");}
			break;
        case R.id.minus2:{
        	String content2 = amount2.getText().toString();
			int count2 = Integer.parseInt(content2);
			if(count2>1){
				count2--;
			}
			if(count2==1){
				//minus.setImageResource(R.drawable.minus2);
			}
			amount2.setText(count2+"");}
			break;
        case R.id.plus3:{
			String content = amount3.getText().toString();
			int count = Integer.parseInt(content);
			count++;
			if(count>1){
				//minus.setImageResource(R.drawable.minus);
			}
			amount3.setText(count+"");}
			break;
        case R.id.minus3:{
        	String content2 = amount3.getText().toString();
			int count2 = Integer.parseInt(content2);
			if(count2>1){
				count2--;
			}
			if(count2==1){
				//minus.setImageResource(R.drawable.minus2);
			}
			amount3.setText(count2+"");}
			break;
		default:
			break;
		}
	}
	
	  @SuppressLint("HandlerLeak")
		private Handler mHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch(msg.what) {
		            case Global.ADDINNER: {
		            	//Log.d("SC",(String) msg.obj );
						try {
							parseInner((String) msg.obj);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
	            }
	    }};
	    private void parseInner(String str)throws Exception{ 
	    	//System.out.println("返回:"+str);
	    	JSONObject jsonobj = new JSONObject(str); 
	    	int result = jsonobj.getInt("ResultCode");
	   	    if(result==Global.SUCCESS){
	   	    	/*
	   	    	 notifyDriverInnerNet.setHandler(mHandler);
	   	    	 notifyDriverInnerNet.setOrderNum(jsonobj.getJSONObject("Data").getString("orderNum"));
	   	    	 notifyDriverInnerNet.setDriverNum(driveridStr);
	   	    	 notifyDriverInnerNet.getDataFromServer();
	   	    	*/
	   	    	Intent intent2 = new Intent();
				intent2.setClass(SelectCarGroupActivity.this,SignUpActivity.class);
				startActivity(intent2);
				 //ivSearch.setProgress(0);
				 //ivSearch.setClickable(true);
	        }else{
	          // ivSearch.setProgress(50);
	         //  ivSearch.setClickable(true);
	           Toast.makeText(SelectCarGroupActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	        } 
	    }
}
