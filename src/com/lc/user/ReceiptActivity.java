package com.lc.user;

import org.json.JSONObject;

import com.lc.net.AddReceiptNet;
import com.lc.net.GetMaxReceiptNet;
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
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiptActivity extends Activity implements OnClickListener {
	CircularProgressButton confirm;
    TextView tvTitle,tvmax;
    ImageView ivleft,ivRight;
    private RelativeLayout rls;
    EditText etphone,etname,etcompany,etnum,etaddress,etcontent;
    AddReceiptNet addReceiptNet = new AddReceiptNet();
    GetMaxReceiptNet getMaxReceiptNet = new GetMaxReceiptNet();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_receipt);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("开发票");
		tvmax= (TextView) findViewById(R.id.MaxNum);
		confirm= (CircularProgressButton) findViewById(R.id.Search);
		confirm.setOnClickListener(this);
		confirm.setIndeterminateProgressMode(true);
		etphone = (EditText) findViewById(R.id.phonenumber);
		etname = (EditText) findViewById(R.id.Name);
		etcompany = (EditText) findViewById(R.id.Company);
		etnum = (EditText) findViewById(R.id.Num);
		etaddress = (EditText) findViewById(R.id.Address);
		etcontent = (EditText) findViewById(R.id.Content);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		ivRight = (ImageView) findViewById(R.id.topButton3);
		ivRight.setOnClickListener(this);
		//ivRight.setVisibility(View.VISIBLE);
		etphone.setText(MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
		String username = MySharePreference.getStringValue(getApplication(), MySharePreference.USERNAME);
		if(username!=null){
			etname.setText(username);
		}	
		getMaxReceiptNet.setHandler(mHandler);
		getMaxReceiptNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		getMaxReceiptNet.setDevice(Global.DEVICE);
		getMaxReceiptNet.getCodeFromServer();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topButton3:{
			Intent intent = new Intent();
			intent.setClass(getApplication(), ReceiptHelpActivity.class);
			startActivity(intent);
		}break;
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.Search:
			confirm.setClickable(false);
			confirm.setProgress(50);
			String name = etname.getText().toString();
			String phone = etphone.getText().toString();
			String address = etaddress.getText().toString();
			String company = etcompany.getText().toString();
			String num = etnum.getText().toString();
			String content = etcontent.getText().toString();
			addReceiptNet.setHandler(mHandler);
			addReceiptNet.setAuth(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
			addReceiptNet.setDevice(Global.DEVICE);
			addReceiptNet.setacceptName(name);
			addReceiptNet.setAddress(address);
			addReceiptNet.setphoneNum(phone);
			addReceiptNet.setbillHead(company);
			addReceiptNet.setcontent(content);
			addReceiptNet.setmoney(num);
			addReceiptNet.getDataFromServer();
			break;
		default:
			break;
		}
	}
	 @SuppressLint("HandlerLeak")
		private Handler mHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch(msg.what) { 
		            case Global.ADDRECEIPT:{
		            	try {
							parseJSON((String)msg.obj);
						} catch (Exception e) {	
							e.printStackTrace();
						}      	
		            break;
	                }
		            case Global.GETMAXRECEIPT:{
		            	try {
							parseMax((String)msg.obj);
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
	   	      Toast.makeText(ReceiptActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	   	      finish();
	        }else{
	          Toast.makeText(ReceiptActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	          confirm.setClickable(true);
	          confirm.setProgress(-1);
	        } 
	   }
	    private void parseMax(String str)throws Exception{ 
	    	JSONObject jsonobj = new JSONObject(str); 
	    	int result = jsonobj.getInt("ResultCode");
	   	    if(result==Global.SUCCESS){
	   	    	String money = jsonobj.getJSONObject("Data").getString("subMoney");
	   	    	money = "<font color='#e0aa35'><b>" + money+ "</b></font>"
	                       +"<font color='black'><b>" + "元"+ "</b></font>";
	   	    	tvmax.setText(Html.fromHtml(money));
	        }else{
	         // Toast.makeText(ReceiptActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	        } 
	    }
}