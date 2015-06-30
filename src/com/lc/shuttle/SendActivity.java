package com.lc.shuttle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.innercity.AddressActivity;
import com.lc.innercity.CarDemandActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.ModifyNameActivity;
import com.lc.net.GetAddressNet;
import com.lc.popupwindow.AddressPopupWindow;
import com.lc.popupwindow.TimePopupWindow;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class SendActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
	public static final int REQUSET_ADDRESS = 2;
    TextView feeRule,txdate,tvname,tvphone,tvendaddress;
    Button ivSearch;
    private RelativeLayout rlusecar,rldate,rlmodifyname,rlstartaddress;
    private ImageView imAddress;
	private View originview; 
	GroupAdapter groupAdapter;
	AddressPopupWindow menuWindow;	//自定义的弹出框类
	TimePopupWindow timepWindow;
	GetAddressNet getaddressnet = new GetAddressNet();
	private List<HashMap<String , Object>> groups1= new ArrayList<HashMap<String , Object>>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shuttle_send);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_innercity_carinfo, null);  
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		tvendaddress = (TextView) findViewById(R.id.getoffaddress);
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		
		rlusecar = (RelativeLayout) findViewById(R.id.usecar);
		rlusecar.setOnClickListener(this);
		
		rlmodifyname = (RelativeLayout) findViewById(R.id.rlmodifyname);
		rlmodifyname.setOnClickListener(this);
		rlstartaddress= (RelativeLayout) findViewById(R.id.startaddress);
		rlstartaddress.setOnClickListener(this);
		
		rldate = (RelativeLayout) findViewById(R.id.usecardate);
		rldate.setOnClickListener(this);
		rldate.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if (event.getAction() == MotionEvent.ACTION_DOWN) { 
					 timepWindow = new TimePopupWindow(SendActivity.this,itemsOnClick);
					 timepWindow.showAsDropDown(originview, 0, 0);		   
			     }         
			     return true; 
			}
	       
	    }); 
		txdate = (TextView) findViewById(R.id.txdate);
		
		imAddress = (ImageView) findViewById(R.id.Star);
		imAddress.setOnClickListener(this);
		
		  getaddressnet.setHandler(mHandler);
	        getaddressnet.setDevice(Global.DEVICE);
	        getaddressnet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
	        getaddressnet.getCodeFromServer();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
	
		case R.id.Star:
			menuWindow = new AddressPopupWindow(SendActivity.this,itemOnClick,groups1);//实例化AddressPopupWindow
			menuWindow.showAsDropDown(originview, 0, 0); //显示窗口
			break;
		
		case R.id.Search:
			

			break;
		case R.id.usecar:
			Intent intent3 = new Intent();
			intent3.setClass(SendActivity.this,CarDemandActivity.class);
			startActivity(intent3);
			break;
		
		case R.id.rlmodifyname:
			Intent intent5 = new Intent();
			intent5.setClass(SendActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
			break;
		case R.id.startaddress:
			Intent intent6 = new Intent();
			intent6.setClass(SendActivity.this,AddressActivity.class);
			startActivityForResult(intent6, REQUSET_ADDRESS); 
			break;
		
		default:
			break;
		}
	}
	//为弹出窗口实现监听类
    private OnItemClickListener  itemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				String address = menuWindow.getItemStr2(arg2);
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
	            	txdate.setText(getdate);
	            break;
                }
	            case Global.ADDRESS_END_MESSAGE:{
	            	String getaddress = (String)msg.obj;
	            	tvendaddress.setText(getaddress);
	            break;
                }
	            case Global.GETADDRESS: {
					try {
						parseADDRESS((String) msg.obj);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
            }
    }};
    private void parseADDRESS(String str)throws Exception{ 
	    groups1.clear();
	   // System.out.println(str);
   	    JSONObject jsonobj = new JSONObject(str); 
        JSONArray jsonarray = jsonobj.getJSONArray("Data");
        for(int x=0;x<jsonarray.length();x++){
       	 JSONObject jsonobj2 = (JSONObject)jsonarray.get(x); 
        	 HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("groupItem",jsonobj2.getString("commAddressId"));
			 map.put("userId",jsonobj2.getString("userId"));
			 map.put("address",jsonobj2.getString("address"));
			 map.put("longitude",jsonobj2.getString("longitude"));
			 map.put("latidute",jsonobj2.getString("latidute"));
			// System.out.println(map.toString());
			 groups1.add(map);
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
         	  tvendaddress.setText(address);
           }
       }  
	 }  
}
