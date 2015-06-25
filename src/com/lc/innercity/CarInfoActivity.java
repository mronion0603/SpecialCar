package com.lc.innercity;

import java.util.ArrayList;
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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
    // 定位相关
  	LocationClient mLocClient;
  	public MyLocationListenner myListener = new MyLocationListenner();
  	BitmapDescriptor mCurrentMarker;
  	boolean isFirstLoc = true;// 是否首次定位
    GeoCoder mGeoCoder = null; 	 // 地理编码  
    PoiInfo mCurentInfo;    // 当前位置信息
    private List<HashMap<String , Object>> groups1= new ArrayList<HashMap<String , Object>>();
    GetAddressNet getaddressnet = new GetAddressNet();
    @Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_innercity_carinfo);
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
		// 地理编码
		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(GeoListener);
		
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
		
		getaddressnet.setHandler(mHandler);
        getaddressnet.setDevice(Global.DEVICE);
        getaddressnet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
        getaddressnet.getCodeFromServer();
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
    private OnItemClickListener  itemOnClick2 = new OnItemClickListener(){
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
	            case Global.GETADDRESS: {
					try {
						parseADDRESS((String) msg.obj);
						// 显示窗口
						menuWindow.showAsDropDown(originview, 0, 0);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
            }
    }};
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
		case R.id.usecardate:
		{	timepWindow = new TimePopupWindow(CarInfoActivity.this,itemsOnClick);
			timepWindow.showAsDropDown(originview, 0, 0); 
		}	break;
		case R.id.star:	
		{	menuWindow = new AddressPopupWindow(CarInfoActivity.this,itemOnClick,groups1);//实例化AddressPopupWindow
			menuWindow.showAsDropDown(originview, 0, 0); //显示窗口
		}	break;
		case R.id.star3:
		{	menuWindow = new AddressPopupWindow(CarInfoActivity.this,itemOnClick2,groups1);
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
					LatLng ll = new LatLng(location.getLatitude(),
							location.getLongitude());
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
                	tvstartAddress.setText(mCurentInfo.address);
                }else{
                	tvstartAddress.setText("出发地");
                }
                
            }  
        }  
    };  
}
