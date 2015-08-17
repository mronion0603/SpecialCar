package com.lc.user;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.lc.innercity.AddressActivity;
import com.lc.innercity.BillingRuleActivity;
import com.lc.innercity.CarDemandActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.ModifyNameActivity;
import com.lc.net.AddMoneyNet;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TimePicker;




public class ChargeCardActivity extends Activity implements OnClickListener {
	
    TextView feeRule,txdate;
 
    Button ivSearch;
    private EditText etnumber,etpassword;
    private ImageView imAddress;
    AddMoneyNet  addMoney = new AddMoneyNet();
	//RadioGroup group;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.userinfo_chargecard);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		etnumber = (EditText) findViewById(R.id.etNumber);
		etpassword = (EditText) findViewById(R.id.code);
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
	    ButtonEffect.setButtonStateChangeListener(ivSearch);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.Search:
			addMoney.setHandler(mHandler);
			addMoney.setDevice(Global.DEVICE);
			addMoney.setCardNum(etnumber.getText().toString());
			addMoney.setPassword(etnumber.getText().toString());
			addMoney.setAuth(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
			addMoney.getDataFromServer();
			
			
			break;
		
		
		default:
			break;
		}
	}
	
	 @SuppressLint("HandlerLeak")
		private Handler mHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch(msg.what) { 
		            case Global.ADDMONEY:{
		            	try {
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
	   	    	Intent intent = new Intent();
				intent.setClass(getApplication(), ChargeSuccessActivity.class);
				startActivity(intent);
	   	        finish();
	        }else{
	          Toast.makeText(ChargeCardActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	        } 
	   }
	
}
