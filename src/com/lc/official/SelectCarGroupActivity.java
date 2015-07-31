package com.lc.official;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.innercity.SelectCarActivity;
import com.lc.intercity.SignUpActivity;
import com.lc.net.AddOfficeNet;
import com.lc.net.GetCarNet;
import com.lc.progressbutton.CircularProgressButton;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ConnectUrl;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;
import com.lidroid.xutils.BitmapUtils;

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
import android.widget.ImageView.ScaleType;


public class SelectCarGroupActivity extends Activity implements OnClickListener {
    TextView tvTitle,righttext;
    ImageView ivleft,ivselect1,ivselect2,ivselect3,car1,car2,car3;;
    private RelativeLayout rls;
    private RelativeLayout select1;
    private RelativeLayout select2;
    private RelativeLayout select3;
    boolean flag1,flag2,flag3;
    CircularProgressButton btsearch;
    private Button plus,minus,plus2,minus2,plus3,minus3;
    private TextView amount,amount2,amount3;
    private AddOfficeNet addInnerNet = new AddOfficeNet();
	String vMoneystr="0";	
	String voucherNumstr="";
	String getname="",getlat="",getlont="",getaddress="",getphone="",gettime="",gettimelong="0",getdemand="";
	String economy="0",common="0",business="0";
	TextView type1,type2,type3,price1,price2,price3;
	GetCarNet getcarnet = new GetCarNet();
	List<HashMap<String,String>> list =new ArrayList<HashMap<String,String>>();
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
		car1 = (ImageView) findViewById(R.id.iv1);
		car2 = (ImageView) findViewById(R.id.iv2);
		car3 = (ImageView) findViewById(R.id.iv3);
		price1 = (TextView) findViewById(R.id.price1);
		price2 = (TextView) findViewById(R.id.price2);
		price3 = (TextView) findViewById(R.id.price3);
		type1 = (TextView) findViewById(R.id.type1);
		type2 = (TextView) findViewById(R.id.type2);
		type3 = (TextView) findViewById(R.id.type3);
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
		btsearch = (CircularProgressButton) findViewById(R.id.Search);
		ButtonEffect.setButtonStateChangeListener(btsearch);
		btsearch.setIndeterminateProgressMode(true);
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
		getcarnet.setHandler(mHandler);
		getcarnet.setDevice(Global.DEVICE);
		getcarnet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		getcarnet.getDataFromServer();
		
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
			btsearch.setClickable(false);
			btsearch.setProgress(50);
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
            addInnerNet.setCartype("1");
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
						try {
							parseInner((String) msg.obj);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
		            case Global.GETCARTYPE:{
		            	try {
							parseJSON((String)msg.obj);
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
	   	    	Intent intent2 = new Intent();
				intent2.setClass(SelectCarGroupActivity.this,SignUpActivity.class);
				startActivity(intent2);
				btsearch.setProgress(0);
				btsearch.setClickable(true);
				finish();
	        }else{
	        	btsearch.setProgress(50);
	        	btsearch.setClickable(true);
	           Toast.makeText(SelectCarGroupActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	        } 
	    }
	    private void parseJSON(String str) throws Exception {
			//System.out.println(str);
			JSONObject jsonobj = new JSONObject(str);
			if (jsonobj.getInt("ResultCode") == Global.SUCCESS) {
				JSONArray jsonarray = jsonobj.getJSONArray("Data");
				for (int x = 0; x < jsonarray.length(); x++) {
					JSONObject jsonobj2 = (JSONObject) jsonarray.get(x);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("carTypeId", jsonobj2.getString("carTypeId"));
					map.put("bascMoney", jsonobj2.getString("bascMoney"));
					map.put("mileageMoney", jsonobj2.getString("mileageMoney"));
					map.put("timeMoney", jsonobj2.getString("timeMoney"));
					map.put("carDesc", jsonobj2.getString("carDesc"));
					map.put("carImg", jsonobj2.getString("carImg"));
					map.put("inMileage", jsonobj2.getString("inMileage"));
					map.put("inTime", jsonobj2.getString("inTime"));
					map.put("officalMoney", jsonobj2.getString("officalMoney"));
				    list.add(map);
				}/*
				BitmapUtils bitmapUtils = new BitmapUtils(SelectCarGroupActivity.this);
				car1.setScaleType(ScaleType.FIT_XY);//铺满
				car2.setScaleType(ScaleType.FIT_XY);
				car3.setScaleType(ScaleType.FIT_XY);
			    bitmapUtils.display(car1, ConnectUrl.commonurl0+list.get(0).get("carImg"));
			    bitmapUtils.display(car2, ConnectUrl.commonurl0+list.get(1).get("carImg"));
			    bitmapUtils.display(car3, ConnectUrl.commonurl0+list.get(2).get("carImg"));
				*/
				type1.setText("经济型");
				type2.setText("普通型");
				type3.setText("商务型");
				price1.setText("¥"+list.get(0).get("officalMoney"));
				price2.setText("¥"+list.get(1).get("officalMoney"));
				price3.setText("¥"+list.get(2).get("officalMoney"));
			}else{
				 Toast.makeText(SelectCarGroupActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
			}
		}
}
