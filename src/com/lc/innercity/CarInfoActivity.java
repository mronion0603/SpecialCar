package com.lc.innercity;

import com.lc.popupwindow.AddressPopupWindow;
import com.lc.popupwindow.TimePopupWindow;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CarInfoActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
	public static final int REQUSET_ADDRESS = 2;
	public static final int REQUSET_ADDRESS2 = 3;
    TextView tvTitle,righttext,feeRule,tvname,tvphone,tvstartAddress,tvendAddress,tvdate;
    ImageView ivleft;
    Button ivSearch;
    private RelativeLayout rls,rlusecar,rlselectcar,rldate,rlmodifyname,rlstartaddress,rlgetoffaddress;
    private ImageView imAddress,getoffAddress;
	private View originview; 
	GroupAdapter groupAdapter;
    AddressPopupWindow menuWindow;	//自定义的弹出框类
    TimePopupWindow timepWindow;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_innercity_carinfo);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_innercity_carinfo, null);  
		tvstartAddress = (TextView) findViewById(R.id.AddressStart);
		tvendAddress = (TextView) findViewById(R.id.AddressEnd);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("市内预约");
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("计费规则");
		righttext.setOnClickListener(this);
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		rlusecar = (RelativeLayout) findViewById(R.id.usecar);
		rlusecar.setOnClickListener(this);
		rlselectcar = (RelativeLayout) findViewById(R.id.selectcar);
		rlselectcar.setOnClickListener(this);
		rlmodifyname = (RelativeLayout) findViewById(R.id.rlmodifyname);
		rlmodifyname.setOnClickListener(this);
		rlstartaddress= (RelativeLayout) findViewById(R.id.startaddress);
		rlstartaddress.setOnClickListener(this);
		rlgetoffaddress= (RelativeLayout) findViewById(R.id.getoffaddress);
		rlgetoffaddress.setOnClickListener(this);
		rldate = (RelativeLayout) findViewById(R.id.usecardate);
		rldate.setOnClickListener(this);
		rldate.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if (event.getAction() == MotionEvent.ACTION_DOWN) { 
					 //selectTimeWindow(originview);		
					 timepWindow = new TimePopupWindow(CarInfoActivity.this,itemsOnClick);
					 timepWindow.showAsDropDown(originview, 0, 0); 
			     }         
			     return true; 
			}
	    }); 
		tvdate = (TextView) findViewById(R.id.tvDate);
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		feeRule=(TextView)findViewById(R.id.FeeRule);
		String ruleStr1 = "13";
		String ruleStr2 = "元+";
		String ruleStr3 = "1.5";
		String ruleStr4 = "元/公里+";
		String ruleStr5 = "0.24";
		String ruleStr6 = "元/分钟";
		
		String newMessageInfo = "<font color='black'><b>" + ruleStr1+ "</b></font>"
		                       +"<font color='gray'><b>" + ruleStr2+ "</b></font>"
		                       +"<font color='black'><b>" + ruleStr3+ "</b></font>"
		                       +"<font color='gray'><b>" + ruleStr4+ "</b></font>"
		                       +"<font color='black'><b>" + ruleStr5+ "</b></font>"
		                       +"<font color='gray'><b>" + ruleStr6+ "</b></font>";
		feeRule.setText(Html.fromHtml(newMessageInfo));   
		imAddress = (ImageView) findViewById(R.id.star);
		imAddress.setOnClickListener(this);
		getoffAddress = (ImageView) findViewById(R.id.star3);
		getoffAddress.setOnClickListener(this);
	}
	//为弹出窗口实现监听类
    private OnItemClickListener  itemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				String address = menuWindow.getItemStr(arg2);
				Message message = Message.obtain();  
			    message.obj = address;  
				message.what = Global.ADDRESS_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				menuWindow.dismiss();   
		}
    };
    private OnItemClickListener  itemOnClick2 = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				String address = menuWindow.getItemStr(arg2);
				Message message = Message.obtain();  
			    message.obj = address;  
				message.what = Global.ADDRESS_END_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				menuWindow.dismiss();   
		}
    };
	//为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.comfirm:
				String time = timepWindow.getTime();
				Message message = Message.obtain();  
			    message.obj = time;  
				message.what = Global.TIME_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				timepWindow.dismiss();   
				break;
			case R.id.canceltime:	
				timepWindow.dismiss();   
				break;
			default:
				break;
			}	
		}
    };
    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
	            case Global.TIME_MESSAGE:{
	            	String getdate = (String)msg.obj;
	            	tvdate.setText(getdate);
	            break;
                }
	            case Global.ADDRESS_MESSAGE:{
	            	String getaddress = (String)msg.obj;
	            	tvstartAddress.setText(getaddress);
	            break;
                }
	            case Global.ADDRESS_END_MESSAGE:{
	            	String getaddress = (String)msg.obj;
	            	tvendAddress.setText(getaddress);
	            break;
                }
            }
    }};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
			Intent intent = new Intent();
			intent.setClass(CarInfoActivity.this,BillingRuleActivity.class);
			startActivity(intent);
			break;
		case R.id.star:	
		{	menuWindow = new AddressPopupWindow(CarInfoActivity.this,itemOnClick);//实例化AddressPopupWindow
			menuWindow.showAsDropDown(originview, 0, 0); //显示窗口
		}	break;
		case R.id.star3:
		{	menuWindow = new AddressPopupWindow(CarInfoActivity.this,itemOnClick2);//实例化AddressPopupWindow
			menuWindow.showAsDropDown(originview, 0, 0); //显示窗口
		}	break;
		case R.id.Search:
			Intent intent2 = new Intent();
			intent2.setClass(CarInfoActivity.this,SendDealActivity.class);
			startActivity(intent2);
			break;
		case R.id.usecar:
			Intent intent3 = new Intent();
			intent3.setClass(CarInfoActivity.this,CarDemandActivity.class);
			startActivity(intent3);
			break;
		case R.id.selectcar:
			Intent intent4 = new Intent();
			intent4.setClass(CarInfoActivity.this,SelectCarActivity.class);
			startActivity(intent4);
			break;
		case R.id.rlmodifyname:
			Intent intent5 = new Intent();
			intent5.setClass(CarInfoActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
			break;
		case R.id.startaddress:
			Intent intent6 = new Intent();
			intent6.setClass(CarInfoActivity.this,AddressActivity.class);
			startActivityForResult(intent6, REQUSET_ADDRESS);  
			break;
		case R.id.getoffaddress:
			Intent intent7 = new Intent();
			intent7.setClass(CarInfoActivity.this,AddressActivity.class);
			startActivityForResult(intent7, REQUSET_ADDRESS2);  
			break;
		default:
			break;
		}
	}
	
		//重写的结果返回方法  
	    @Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        super.onActivityResult(requestCode, resultCode, data);  
	        if (requestCode == REQUSET_NAMEPHONE && resultCode == RESULT_OK) {
	        	String name ="";
	        	String phone ="";
	        	  Bundle extras = data.getExtras();
	              if(extras != null){
	            	  name = extras.getString("name");
	            	  phone = extras.getString("phone");
	            	  tvphone.setText(phone);
	            	  tvname.setText(name);
	              }
	        }  
	        if (requestCode == REQUSET_ADDRESS && resultCode == RESULT_OK) {
	        	String address ="";
	        	  Bundle extras = data.getExtras();
	              if(extras != null){
	            	  address = extras.getString("address");
	            	  tvstartAddress.setText(address);
	              }
	        }  
	        if (requestCode == REQUSET_ADDRESS2 && resultCode == RESULT_OK) {
	        	String address ="";
	        	  Bundle extras = data.getExtras();
	              if(extras != null){
	            	  address = extras.getString("address");
	            	  tvendAddress.setText(address);
	              }
	        }  
	    }  
}
