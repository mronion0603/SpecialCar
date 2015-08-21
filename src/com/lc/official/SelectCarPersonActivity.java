package com.lc.official;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.innercity.BillingRuleActivity;
import com.lc.innercity.SelectCarActivity;
import com.lc.innercity.SendDealActivity;
import com.lc.intercity.SignUpActivity;
import com.lc.net.AddOfficePersonNet;
import com.lc.net.GetCarNet;
import com.lc.progressbutton.CircularProgressButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectCarPersonActivity extends Activity implements OnClickListener {
	public static final int SIGNUP =2;
    TextView tvTitle,righttext;
    ImageView ivleft,ivselect1,ivselect2,ivselect3,ivselect4,ivselect5,car1,car2,car3,car4,car5;
    CircularProgressButton btsearch;
    private RelativeLayout rls;
    private RelativeLayout select1;
    private RelativeLayout select2;
    private RelativeLayout select3;
    private RelativeLayout select4;
    private RelativeLayout select5;
    boolean flag1,flag2,flag3,flag4,flag5;
    private AddOfficePersonNet addInnerNet = new AddOfficePersonNet();
    String getname="",getlat="",getlont="",getaddress="",getphone="",gettime="",gettimelong="0",getdemand="";
	String cartype ="1";
	GetCarNet getcarnet = new GetCarNet();
	List<HashMap<String,String>> list =new ArrayList<HashMap<String,String>>();
	TextView type1,type2,type3,type4,type5,price1,price2,price3,price4,price5,cardis1,cardis2,cardis3,cardis4,cardis5;
	private LinearLayout ll1;
	private ProgressBar pb; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);    
		setContentView(R.layout.official_selectperson);
		init();
	}

	public void init(){
		ll1 = (LinearLayout) findViewById(R.id.ll1);
		ll1.setVisibility(View.GONE);
		pb = (ProgressBar)findViewById(R.id.progress); 
		pb.setProgress(0);  
		pb.setIndeterminate(true);
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
		car1 = (ImageView) findViewById(R.id.im1);
		car2 = (ImageView) findViewById(R.id.im2);
		car3 = (ImageView) findViewById(R.id.im3);
		car4 = (ImageView) findViewById(R.id.im4);
		car5 = (ImageView) findViewById(R.id.im5);
		type1 = (TextView) findViewById(R.id.type1);
		type2 = (TextView) findViewById(R.id.type2);
		type3 = (TextView) findViewById(R.id.type3);
		type4 = (TextView) findViewById(R.id.type4);
		type5 = (TextView) findViewById(R.id.type5);
		price1 = (TextView) findViewById(R.id.price1);
		price2 = (TextView) findViewById(R.id.price2);
		price3 = (TextView) findViewById(R.id.price3);
		price4 = (TextView) findViewById(R.id.price4);
		price5 = (TextView) findViewById(R.id.price5);
		cardis1 = (TextView) findViewById(R.id.cardis1);
		cardis2 = (TextView) findViewById(R.id.cardis2);
		cardis3 = (TextView) findViewById(R.id.cardis3);
		cardis4 = (TextView) findViewById(R.id.cardis4);
		cardis5 = (TextView) findViewById(R.id.cardis5);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("选择车型");
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("计费规则");
		righttext.setOnClickListener(this);
		ivselect1 = (ImageView) findViewById(R.id.ivselect);
		ivselect2 = (ImageView) findViewById(R.id.ivselect2);
		ivselect3 = (ImageView) findViewById(R.id.ivselect3);
		ivselect4 = (ImageView) findViewById(R.id.ivselect4);
		ivselect5 = (ImageView) findViewById(R.id.ivselect5);
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
		select4 = (RelativeLayout) findViewById(R.id.select4);
		select4.setOnClickListener(this);
		select5 = (RelativeLayout) findViewById(R.id.select5);
		select5.setOnClickListener(this);
		btsearch = (CircularProgressButton) findViewById(R.id.Search);
		btsearch.setIndeterminateProgressMode(true);
		ButtonEffect.setButtonStateChangeListener(btsearch);
		btsearch.setOnClickListener(this);
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
			intent.setClass(SelectCarPersonActivity.this,BillingRuleActivity.class);
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
            addInnerNet.setServiceTypeId("3");
            String gettype = "1";
            if(flag1){
            addInnerNet.setRealMoney(list.get(0).get("officalMoney"));
            gettype=list.get(0).get("carTypeId");
            }else if(flag2){
            addInnerNet.setRealMoney(list.get(1).get("officalMoney"));
            gettype=list.get(1).get("carTypeId");
            }else if(flag3){
            addInnerNet.setRealMoney(list.get(2).get("officalMoney"));
            gettype=list.get(2).get("carTypeId");
            }else if(flag4){
            addInnerNet.setRealMoney(list.get(3).get("officalMoney"));
            gettype=list.get(3).get("carTypeId");
            }else if(flag5){
            addInnerNet.setRealMoney(list.get(4).get("officalMoney"));
            gettype=list.get(4).get("carTypeId");
            }
               
            addInnerNet.setUseCarTime(gettimelong);
            addInnerNet.setCartype(gettype);
            addInnerNet.getDataFromServer();
		}break;
		case R.id.select1:
			if(flag1){
				
			}else{
				ivselect1.setVisibility(View.VISIBLE);
				ivselect2.setVisibility(View.GONE);
				ivselect3.setVisibility(View.GONE);
				ivselect4.setVisibility(View.GONE);
				ivselect5.setVisibility(View.GONE);
				flag1=true;
				flag2=false;
				flag3=false;
				flag4=false;
				flag5=false;
			}
			break;
		case R.id.select2:
			if(flag2){
				
			}else{
				ivselect2.setVisibility(View.VISIBLE);
				ivselect3.setVisibility(View.GONE);
				ivselect1.setVisibility(View.GONE);
				ivselect4.setVisibility(View.GONE);
				ivselect5.setVisibility(View.GONE);
				flag2=true;
				flag3=false;
				flag1=false;
				flag4=false;
				flag5=false;
			}
			break;
		case R.id.select3:
			if(flag3){
				
			}else{
				ivselect3.setVisibility(View.VISIBLE);
				ivselect1.setVisibility(View.GONE);
				ivselect2.setVisibility(View.GONE);
				ivselect4.setVisibility(View.GONE);
				ivselect5.setVisibility(View.GONE);
				flag3=true;
				flag1=false;
				flag2=false;
				flag4=false;
				flag5=false;
			}
			break;
		case R.id.select4:
			if(flag4){
				
			}else{
				ivselect3.setVisibility(View.GONE);
				ivselect1.setVisibility(View.GONE);
				ivselect2.setVisibility(View.GONE);
				ivselect4.setVisibility(View.VISIBLE);
				ivselect5.setVisibility(View.GONE);
				flag3=false;
				flag1=false;
				flag2=false;
				flag4=true;
				flag5=false;
			}
			break;
			case R.id.select5:
				if(flag5){
					
				}else{
					ivselect3.setVisibility(View.GONE);
					ivselect1.setVisibility(View.GONE);
					ivselect2.setVisibility(View.GONE);
					ivselect4.setVisibility(View.GONE);
					ivselect5.setVisibility(View.VISIBLE);
					flag3=false;
					flag1=false;
					flag2=false;
					flag4=false;
					flag5=true;
				}
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
	    	System.out.println(str);
	    	JSONObject jsonobj = new JSONObject(str); 
	    	int result = jsonobj.getInt("ResultCode");
	   	    if(result==Global.SUCCESS){
	   	    	Intent intent2 = new Intent();
				intent2.setClass(SelectCarPersonActivity.this,SendDealActivity.class);
				startActivity(intent2);
				//btsearch.setProgress(0);
				btsearch.setClickable(true);
				finish();
	        }else{
	           btsearch.setProgress(0);
	           btsearch.setClickable(true);
	           Toast.makeText(SelectCarPersonActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	        } 
	    }
	   
	    private void parseJSON(String str) throws Exception {
			System.out.println(str);
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
					String officalmoney = jsonobj2.getString("officalMoney");	
					map.put("officalMoney", (int)(Double.parseDouble(officalmoney))+"");
					map.put("overMileage", jsonobj2.getString("overMileage"));
					map.put("overTime", jsonobj2.getString("overTime"));
				    list.add(map);
				}
				/*
				BitmapUtils bitmapUtils = new BitmapUtils(SelectCarPersonActivity.this);
				car1.setScaleType(ScaleType.FIT_XY);//铺满
				car2.setScaleType(ScaleType.FIT_XY);
				car3.setScaleType(ScaleType.FIT_XY);
			    bitmapUtils.display(car1, ConnectUrl.commonurl0+list.get(0).get("carImg"));
			    bitmapUtils.display(car2, ConnectUrl.commonurl0+list.get(1).get("carImg"));
			    bitmapUtils.display(car3, ConnectUrl.commonurl0+list.get(2).get("carImg"));
			    */
				pb.setVisibility(View.GONE);
				ll1.setVisibility(View.VISIBLE);
				
				type1.setText(getApplication().getString(R.string.type1));
				type2.setText(getApplication().getString(R.string.type2));
				type3.setText(getApplication().getString(R.string.type3));
				type4.setText(getApplication().getString(R.string.type4));
				type5.setText(getApplication().getString(R.string.type5));
				price1.setText("¥"+list.get(0).get("officalMoney"));
				price2.setText("¥"+list.get(1).get("officalMoney"));
				price3.setText("¥"+list.get(2).get("officalMoney"));
				price4.setText("¥"+list.get(3).get("officalMoney"));
				price5.setText("¥"+list.get(4).get("officalMoney"));
				cardis1.setText("套餐包含1天100公里"+"\n"+"超出按¥"+list.get(0).get("overMileage")+"/分钟+¥"+list.get(0).get("overTime")+"/公里计费");
				cardis2.setText("套餐包含1天100公里"+"\n"+"超出按¥"+list.get(1).get("overMileage")+"/分钟+¥"+list.get(1).get("overTime")+"/公里计费");
				cardis3.setText("套餐包含1天100公里"+"\n"+"超出按¥"+list.get(2).get("overMileage")+"/分钟+¥"+list.get(2).get("overTime")+"/公里计费");
				cardis4.setText("套餐包含1天100公里"+"\n"+"超出按¥"+list.get(3).get("overMileage")+"/分钟+¥"+list.get(3).get("overTime")+"/公里计费");
				cardis5.setText("套餐包含1天100公里"+"\n"+"超出按¥"+list.get(4).get("overMileage")+"/分钟+¥"+list.get(4).get("overTime")+"/公里计费");
			}else{
				 Toast.makeText(SelectCarPersonActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
			}
		}
}
