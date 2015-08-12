package com.lc.intercity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.innercity.AddressActivity;
import com.lc.innercity.ModifyNameActivity;
import com.lc.net.AddCarPoolNet;
import com.lc.net.GetAddressNet;
import com.lc.net.NotifyDriverNet;
import com.lc.popupwindow.AddressPopupWindow;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;




public class CharteredCarActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
	public static final int REQUSET_ADDRESS = 2;
	public static final int SIGNUP =3;
    TextView tvTitle,tvname,tvphone,chooseaddress;
    Button ivSearch;
    ImageView ivleft,star;
    private RelativeLayout rls,modify;
    private View originview; 
    AddressPopupWindow menuWindow;    //自定义的弹出框类
    String getorderNum="",pickUpArea="";
    int curnum =0,totalnum=0;
    AddCarPoolNet addCarPoolNet = new AddCarPoolNet();
    GetAddressNet getaddressnet = new GetAddressNet();
    private List<HashMap<String , Object>> groups1= new ArrayList<HashMap<String , Object>>();
    String getdriverid="";
    NotifyDriverNet notifyDriverNet = new NotifyDriverNet();
    EditText feedbackET;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_charteredcar);
		init();
		
	}
	public void init(){
		Bundle extras = getIntent().getExtras();
	    if(extras != null){
	       getorderNum = extras.getString("orderNum");
	       curnum = Integer.parseInt(extras.getString("CurNum"));
	       totalnum = Integer.parseInt(extras.getString("TotalNum"));
	       pickUpArea = extras.getString("PickUpArea");
	       getdriverid= extras.getString("driverid");
	    }
		ExitApplication.getInstance().addActivity(this);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_intercity_charteredcar, null); 
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		tvTitle = (TextView) findViewById(R.id.topTv);
		feedbackET= (EditText) findViewById(R.id.feedbackET);
		tvTitle.setText("包车");
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		chooseaddress = (TextView) findViewById(R.id.caraddress);
		chooseaddress.setOnClickListener(this);
		modify = (RelativeLayout) findViewById(R.id.modify);
		modify.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		star = (ImageView) findViewById(R.id.star);
		star.setOnClickListener(this);
		
		String username = MySharePreference.getStringValue(getApplication(), MySharePreference.USERNAME);
		if(username==null){
			tvname.setText( MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
		}else{
			tvname.setText(username);
		}
		// 实例化AddressPopupWindow
		menuWindow = new AddressPopupWindow(CharteredCarActivity.this, itemOnClick,groups1);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.star:
		{   star.setClickable(false);
			getaddressnet.setHandler(mHandler);
	        getaddressnet.setDevice(Global.DEVICE);
	        getaddressnet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
	        getaddressnet.getCodeFromServer();
		}	break;
		case R.id.modify:
			{Intent intent5 = new Intent();
			intent5.setClass(CharteredCarActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
			}break;
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.caraddress:
			{Intent intent5 = new Intent();
			intent5.setClass(CharteredCarActivity.this,AddressActivity.class);
			startActivityForResult(intent5, REQUSET_ADDRESS);
			}break;
		case R.id.Search:
			addCarPoolNet.setHandler(mHandler);	
			addCarPoolNet.setAuth(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
			addCarPoolNet.setDevice(Global.DEVICE);
			addCarPoolNet.setCjridersum("1");
			addCarPoolNet.setOrderNum(getorderNum);			
			addCarPoolNet.setRiderName(tvname.getText().toString());
			if(tvphone.getText().toString().equals("本人")){
			   addCarPoolNet.setRiderPhone(MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
			}else{
			   addCarPoolNet.setRiderPhone(tvphone.getText().toString());
			}
			String feedback = feedbackET.getText().toString();
			if(feedback==null||feedback.length()<=0){
			    addCarPoolNet.setcomment("");	
			}else{
				addCarPoolNet.setcomment(feedback);	
			}
			addCarPoolNet.setServiceTypeId("5");
			addCarPoolNet.setCpbStauts("2");
			addCarPoolNet.getDataFromServer();
			
			break;
		
		default:
			break;
		}
	}
	 private void parseADDRESS(String str)throws Exception{ 
		     groups1.clear();
	    	 JSONObject jsonobj = new JSONObject(str); 
	         JSONArray jsonarray = jsonobj.getJSONArray("Data");
	         for(int x=0;x<jsonarray.length();x++){
	        	 JSONObject jsonobj2 = (JSONObject)jsonarray.get(x); 
	         	 HashMap<String , Object> map = new HashMap<String , Object>();
	 			 map.put("groupItem",jsonobj2.getString("commAddressId"));
	 			 map.put("userId",jsonobj2.getString("userId"));
	 			 map.put("address",jsonobj2.getString("address"));
	 			 groups1.add(map);

	         }
	    }
	 
	//为弹出窗口实现监听类
    private OnItemClickListener  itemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				String address = menuWindow.getItemStr2(arg2);
				Message message = Message.obtain();  
			    message.obj = address;  
				message.what = Global.ADDRESS_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				menuWindow.dismiss();   
		}
    };
    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Global.ADDRESS_MESSAGE: {
				String getaddress = (String) msg.obj;
				chooseaddress.setText(getaddress);
				break;
			}
			case Global.ADDCARPOOL: {
				try {
					parseJSON((String) msg.obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case Global.GETADDRESS: {
				try {
					parseADDRESS((String) msg.obj);
					// 显示窗口
					menuWindow.showAsDropDown(originview, 0, 0);
				} catch (Exception e) {

					e.printStackTrace();
				}
				star.setClickable(true);
				break;
			}
			}
		}
	};
       private void parseJSON(String str)throws Exception{  
       	JSONObject jsonobj = new JSONObject(str); 
       	int result = jsonobj.getInt("ResultCode");
      	    if(result==Global.SUCCESS){
      	    	//notifyDriverNet.setHandler(mHandler);
      	    	//notifyDriverNet.setOrderNum(getorderNum);
       	    	//notifyDriverNet.getDataFromServer();
       	    	
      	    	Intent intent = new Intent();
    			intent.putExtra("title", "包车");
    			intent.setClass(getApplication(), SignUpActivity.class);
    			startActivityForResult(intent,SIGNUP);
    			
           }else{
              Toast.makeText(CharteredCarActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
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
            	  if(name.length()<=0||phone.length()<=0){
            		  
            	  }else{
            	   tvphone.setText(phone);
            	   tvname.setText(name);
            	  }
              }
        }  
        if (requestCode == REQUSET_ADDRESS && resultCode == RESULT_OK) {
        	  String address ="";
        	  Bundle extras = data.getExtras();
              if(extras != null){
            	  address = extras.getString("address");
            	  chooseaddress.setText(address);
              }
        }  
        if (requestCode == SIGNUP && resultCode == RESULT_OK) {
     	   Intent intent = new Intent();
	       setResult(RESULT_OK, intent); 
     	   finish();
          
     } 
    }  
}
