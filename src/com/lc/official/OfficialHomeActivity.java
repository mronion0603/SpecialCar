package com.lc.official;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lc.innercity.AddressActivity;
import com.lc.innercity.BillingRuleActivity;
import com.lc.innercity.CarDemandActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.ModifyNameActivity;
import com.lc.net.GetAddressNet;
import com.lc.popupwindow.AddressPopupWindow;
import com.lc.popupwindow.TimeLongPopupWindow;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressLint("SimpleDateFormat")
public class OfficialHomeActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
	public static final int REQUSET_ADDRESS = 2;
	public static final int REQUSET_DEMAND = 4;
    TextView tvTitle,righttext,feeRule,txdate,tvname,tvphone,tvdate,tvaddress,tvtimelong,tvdemand;
    ImageView ivleft;
    Button ivSearch;
    private RelativeLayout rls,rlusecar,rldate,rlmodifyname,rlstartaddress,rltimelong;
    private ImageView imAddress;
	private View originview; 
	GroupAdapter groupAdapter; 
	RadioGroup group;
	TimePopupWindow timepWindow;
	AddressPopupWindow menuWindow;
	TimeLongPopupWindow timelongpWindow;
	
	// 定位相关
 	LocationClient mLocClient;
 	public MyLocationListenner myListener = new MyLocationListenner();
 	BitmapDescriptor mCurrentMarker;
 	boolean isFirstLoc = true;// 是否首次定位
    GeoCoder mGeoCoder = null; 	 // 地理编码  
    PoiInfo mCurentInfo;    // 当前位置信息
    
    GetAddressNet getaddressnet = new GetAddressNet();
    private List<HashMap<String , Object>> groups1= new ArrayList<HashMap<String , Object>>();
    double lat=0,lont=0;
    String time="";
    String time3="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);    
		setContentView(R.layout.official_home);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
		init();	
	}

	public void init(){
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		//地理编码  
        mGeoCoder = GeoCoder.newInstance();  
        mGeoCoder.setOnGetGeoCodeResultListener(GeoListener);
        
		ExitApplication.getInstance().addActivity(this);
		tvaddress = (TextView) findViewById(R.id.TvAddress);
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		tvdate = (TextView) findViewById(R.id.UseDate);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_innercity_carinfo, null);  
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("公务包车");
		righttext = (TextView) findViewById(R.id.righttext);
		tvtimelong = (TextView) findViewById(R.id.timelong);
		righttext.setText("计费规则");
		righttext.setOnClickListener(this);
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		rlusecar = (RelativeLayout) findViewById(R.id.usecar);
		rlusecar.setOnClickListener(this);
		rltimelong = (RelativeLayout) findViewById(R.id.Duration);
		rltimelong.setOnClickListener(this);
		rlmodifyname = (RelativeLayout) findViewById(R.id.rlmodifyname);
		rlmodifyname.setOnClickListener(this);
		rlstartaddress= (RelativeLayout) findViewById(R.id.startaddress);
		rlstartaddress.setOnClickListener(this);
		tvdemand = (TextView) findViewById(R.id.Demand);
		rldate = (RelativeLayout) findViewById(R.id.usecardate);
		rldate.setOnClickListener(this);
		txdate = (TextView) findViewById(R.id.txdate);
		
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		
		imAddress = (ImageView) findViewById(R.id.star);
		imAddress.setOnClickListener(this);
		group = (RadioGroup)this.findViewById(R.id.radioGroup);
        //绑定一个匿名监听器
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {          
            @Override
             public void onCheckedChanged(RadioGroup arg0, int arg1) {
                //获取变更后的选中项的ID
              
             }
        });
        getaddressnet.setHandler(mHandler);
        getaddressnet.setDevice(Global.DEVICE);
        getaddressnet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
        getaddressnet.getCodeFromServer();
        
        String username = MySharePreference.getStringValue(getApplication(), MySharePreference.USERNAME);
		if(username==null){
			tvname.setText( MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
		}else{
			tvname.setText(username);
		}
		
	}
	//为弹出窗口实现监听类
    private OnItemClickListener  itemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (arg0.getId()) {
			case R.id.lvGroup:
				String address =  menuWindow.getItemStr2(arg2);
				//
				Message message = Message.obtain();  
			    message.obj = address;  
				message.what = Global.ADDRESS_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				menuWindow.dismiss();   
				break;
			default:
				break;
			}	
		}
    };
	//为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.comfirm:
				String time2 = timepWindow.getTime();
				time3 =  timepWindow.getTimeUpload2();
				Message message = Message.obtain();  
			    message.obj = time2;  
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
  //为弹出窗口实现监听类
    private OnClickListener timelongitemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.comfirm:
				time = timelongpWindow.getTime();
				Message message = Message.obtain();  
			    message.obj = time;  
				message.what = Global.TIMELONG_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				timelongpWindow.dismiss();   
				break;
			case R.id.canceltime:	
				timelongpWindow.dismiss();   
				break;
			default:
				break;
			}	
		}
    };
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.usecardate:
			timepWindow = new TimePopupWindow(OfficialHomeActivity.this,itemsOnClick);
			timepWindow.showAsDropDown(originview, 0, 0); 	
			break;
		case R.id.Duration:
			timelongpWindow = new TimeLongPopupWindow(OfficialHomeActivity.this,timelongitemsOnClick);
			timelongpWindow.showAsDropDown(originview, 0, 0); 		
			break;
		case R.id.righttext:
			Intent intent = new Intent();
			intent.setClass(OfficialHomeActivity.this,BillingRuleActivity.class);
			startActivity(intent);
			break;
		case R.id.star:
			menuWindow = new AddressPopupWindow(OfficialHomeActivity.this,itemOnClick,groups1);//实例化AddressPopupWindow
			menuWindow.showAsDropDown(originview, 0, 0); 
			break;
		
		case R.id.Search:
			  int radioButtonId = group.getCheckedRadioButtonId();
            //根据ID获取RadioButton的实例
            RadioButton rb = (RadioButton)OfficialHomeActivity.this.findViewById(radioButtonId);
            //更新文本内容，以符合选中项
            if(rb.getText().equals("个人包车")){
				Intent intent2 = new Intent();
				intent2.setClass(OfficialHomeActivity.this, SelectCarPersonActivity.class);
				intent2.putExtra("sLatitude", String.valueOf(lat));
				intent2.putExtra("sLongitude", String.valueOf(lont));
				intent2.putExtra("StartAddress", tvaddress.getText().toString());
				intent2.putExtra("username", tvname.getText().toString());
				if(tvphone.getText().toString().equals("本人")){
		          intent2.putExtra("phone", MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
				}else{
		          intent2.putExtra("phone", tvphone.getText().toString());
		        }
				String startTime = tvdate.getText().toString();
				if(startTime.equals("现在")){
	            	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
	        		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
	        		startTime  = formatter.format(curDate); 
	        		intent2.putExtra("starttime", startTime);
	            }else{
				    intent2.putExtra("starttime", time3);
	            }
				if(tvtimelong.getText().equals("1天"))
				intent2.putExtra("timelong", String.valueOf(24*60));
				else{
				intent2.putExtra("timelong", String.valueOf(Integer.parseInt(time)*24*60));
				}
				if( tvdemand.getText().toString()!=null)
				intent2.putExtra("commont", tvdemand.getText().toString());
				else
				intent2.putExtra("commont","");	
				startActivity(intent2);
            }else{
          	    Intent intent2 = new Intent();
				intent2.setClass(OfficialHomeActivity.this, SelectCarGroupActivity.class);
				intent2.putExtra("sLatitude", String.valueOf(lat));
				intent2.putExtra("sLongitude", String.valueOf(lont));
				intent2.putExtra("StartAddress", tvaddress.getText().toString());
				intent2.putExtra("username", tvname.getText().toString());
				if(tvphone.getText().toString().equals("本人")){
		          intent2.putExtra("phone", MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
				}else{
		          intent2.putExtra("phone", tvphone.getText().toString());
		        }
				String startTime = tvdate.getText().toString();
				if(startTime.equals("现在")){
	            	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
	        		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
	        		startTime  = formatter.format(curDate); 
	        		intent2.putExtra("starttime", startTime);
	            }else{
				    intent2.putExtra("starttime", time3);
	            }
				if(tvtimelong.getText().equals("1天"))
				intent2.putExtra("timelong", String.valueOf(24*60));
				else{
				intent2.putExtra("timelong", String.valueOf(Integer.parseInt(time)*24*60));
				}
				if( tvdemand.getText().toString()!=null)
				intent2.putExtra("commont", tvdemand.getText().toString());
				else
				intent2.putExtra("commont","");	
				startActivity(intent2);
            }

			break;
		case R.id.usecar:
			Intent intent3 = new Intent();
			intent3.setClass(OfficialHomeActivity.this,CarDemandActivity.class);
			startActivityForResult(intent3,REQUSET_DEMAND);
			break;
		
		case R.id.rlmodifyname:
			Intent intent5 = new Intent();
			intent5.setClass(OfficialHomeActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
			break;
		case R.id.startaddress:
			Intent intent6 = new Intent();
			intent6.setClass(OfficialHomeActivity.this,AddressActivity.class);
			startActivityForResult(intent6, REQUSET_ADDRESS);  
			break;
		
		default:
			break;
		}
	}
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
	            	tvaddress.setText(getaddress);
	            break;
                }
	            case Global.TIMELONG_MESSAGE:{
	            	String getdate = (String)msg.obj;
	            	tvtimelong.setText(getdate+"天");
	            break;
                }
	            case Global.GETADDRESS: {
					try {
						parseADDRESS((String) msg.obj);
						// 显示窗口
						//menuWindow.showAsDropDown(originview, 0, 0);
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
	            	  lat = extras.getDouble("latidute");
	            	  lont = extras.getDouble("longitude");
	            	  tvaddress.setText(address);
	              }
	        }  
	        if (requestCode == REQUSET_DEMAND && resultCode == RESULT_OK) {
	        	  String demand ="";
	        	  Bundle extras = data.getExtras();
	              if(extras != null){
	            	  demand = extras.getString("demand");
	            	  tvdemand.setText(demand);
	              }
	        }  
	    }  
	    
		 /**
			 * 定位SDK监听函数
			 */
		public class MyLocationListenner implements BDLocationListener {

				@Override
				public void onReceiveLocation(BDLocation location) {
					if (location == null )
						return;
					if (isFirstLoc) {
						isFirstLoc = false;
						
						LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
						 // 发起反地理编码检索  
			             mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())  
			                     .location(ll));  
					}
				}
		}
		 // 地理编码监听器  
	    OnGetGeoCoderResultListener GeoListener = new OnGetGeoCoderResultListener() {  
	        public void onGetGeoCodeResult(GeoCodeResult result) {  
	            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	                // 没有检索到结果  
	            }  
	            // 获取地理编码结果  
	        }  
	        @Override  
	        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {  
	            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	                // 没有找到检索结果  
	            }  
	            // 获取反向地理编码结果  
	            else {  
	                // 当前位置信息  
	                mCurentInfo = new PoiInfo();  
	                mCurentInfo.address = result.getAddress();  
	                mCurentInfo.location = result.getLocation();  
	                mCurentInfo.name = "[位置]";  
	                if(result.getPoiList() != null){
	                	lat = mCurentInfo.location.latitude;
						lont = mCurentInfo.location.longitude;
	                	tvaddress.setText(mCurentInfo.address);
	                }else{
	                	tvaddress.setText("上车地");
	                } 
	            }  
	        }  
	    };  
	    
	    @Override  
		 protected void onDestroy() {  
		        super.onDestroy();  
		        // 退出时销毁定位
				mLocClient.stop();
		 }  
		
	    
}
