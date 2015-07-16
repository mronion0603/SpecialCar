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
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lc.net.GetAddressNet;
import com.lc.net.GetDriverNet;
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
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class InnerCityHomeActivity extends Activity implements OnClickListener {
	AddressPopupWindow menuWindow;	//自定义的弹出框类
	public static final int REQUSET = 1;
	public static final int TypeAddress = 2;
    TextView tvTitle,righttext;
    TextView curaddress;
    Button ivSearch;
    ImageView ivleft,ivstar;
    ImageView ivcenter;
    private RelativeLayout rls;
    MapView mMapView = null;  
    BaiduMap mBaiduMap;
    // 定位相关
 	LocationClient mLocClient;
 	public MyLocationListenner myListener = new MyLocationListenner();
 	BitmapDescriptor mCurrentMarker;
 	boolean isFirstLoc = true;// 是否首次定位
    // 地理编码  
    GeoCoder mGeoCoder = null;
    // 位置列表  
    PlaceListAdapter mAdapter; 
    ListView mListView;  
    List<PoiInfo> mInfoList;  
    PoiInfo mCurentInfo;  
    ImageView mSelectImg;
    GetDriverNet getDriverNet = new GetDriverNet();
    List<HashMap<String,String>> list =new ArrayList<HashMap<String,String>>();
    GetAddressNet getaddressnet = new GetAddressNet();
    private List<HashMap<String , Object>> groups1= new ArrayList<HashMap<String , Object>>();
    double slat=0,slont =0;
    private View originview; 
    String driveridStr="";
    @Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		 //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.activity_innercity_home);
		init();
		
	}

	public void init(){
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_innercity_home, null);  
		ExitApplication.getInstance().addActivity(this);
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("市内约租");
		righttext = (TextView) findViewById(R.id.righttext);
		//righttext.setVisibility(View.VISIBLE);
		righttext.setText("计费规则");
		righttext.setOnClickListener(this);
		curaddress = (TextView) findViewById(R.id.curaddress);
		curaddress.setOnClickListener(this);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		ivcenter = (ImageView) findViewById(R.id.ivpoint);
		ivstar= (ImageView) findViewById(R.id.star);
		ivstar.setOnClickListener(this);
        //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.bmapView);  
        mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		   LatLng cenpt = new LatLng(30.511876,114.405751); 
	        //定义地图状态
	        MapStatus mMapStatus = new MapStatus.Builder()
	        .target(cenpt)
	        .zoom(12)
	        .build();
	        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
	        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
	        //改变地图状态
	        mBaiduMap.setMapStatus(mMapStatusUpdate);
	        
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		mBaiduMap.setOnMapStatusChangeListener(touchListener);
		// 初始化POI信息列表  
        mInfoList = new ArrayList<PoiInfo>();  
		 // 地理编码  
        mGeoCoder = GeoCoder.newInstance();  
        mGeoCoder.setOnGetGeoCodeResultListener(GeoListener);  
		
        // 周边位置列表  
        mListView = (ListView) findViewById(R.id.place_list);  
        //mLoadBar = (ProgressBar) findViewById(R.id.place_progressBar);  
        mListView.setOnItemClickListener(itemClickListener);  
        mAdapter = new PlaceListAdapter(getLayoutInflater(), mInfoList);  
        setListViewHeightBasedOnChildren(mListView);
        mListView.setAdapter(mAdapter);  
        mListView.setVisibility(View.GONE);
        mSelectImg = new ImageView(this);  
        
        getaddressnet.setHandler(mHandler);
        getaddressnet.setDevice(Global.DEVICE);
        getaddressnet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
        getaddressnet.getCodeFromServer();
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if (requestCode == AddressActivity.REQUSET && resultCode == RESULT_OK) {
        	//carListItem.clear();
        	//getMyCarNet.getCarFromServer(); 
        	 // System.out.println("开始");
        	  String address ="";
        	  double lat,lng;
        	  Bundle extras = data.getExtras();
              if(extras != null){
            	  address = extras.getString("address");
            	  lat= extras.getDouble("latitude");
            	  lng= extras.getDouble("longitude");
            	  //System.out.println(address);
            	  curaddress.setText(address); 
            	  LatLng ll = new LatLng(lat, lng);
				  MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				  mBaiduMap.animateMapStatus(u);
              }
        }  
        if (requestCode == TypeAddress && resultCode == RESULT_OK) {
        	//carListItem.clear();
        	//getMyCarNet.getCarFromServer(); 
        	 // System.out.println("开始");
        	  String address ="";
        	  double lat,lng;
        	  Bundle extras = data.getExtras();
              if(extras != null){
            	  address = extras.getString("address");
            	  lat= extras.getDouble("latitude");
            	  lng= extras.getDouble("longitude");
            	  //System.out.println(address);
            	  curaddress.setText(address);
            	  
            	  LatLng ll = new LatLng(lat, lng);
				  MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				  mBaiduMap.animateMapStatus(u);	
              }
        }  
    }  
	
	/**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) { 
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) { 
            // pre-condition 
            return; 
        } 

        int totalHeight = 0; 
        for (int i = 0; i < listAdapter.getCount(); i++) { 
            View listItem = listAdapter.getView(i, null, listView); 
            listItem.measure(0, 0); 
            totalHeight += listItem.getMeasuredHeight(); 
        } 

        ViewGroup.LayoutParams params = listView.getLayoutParams(); 
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
        listView.setLayoutParams(params); 
    }
    
	// listView选项点击事件监听器  
    OnItemClickListener itemClickListener = new OnItemClickListener() {  
  
        @Override  
        public void onItemClick(AdapterView<?> parent, View view, int position,  
                long id) {  
            // TODO Auto-generated method stub  
  
            // 通知是适配器第position个item被选择了  
            mAdapter.setNotifyTip(position);  
  
            mBaiduMap.clear();  
            PoiInfo info = (PoiInfo) mAdapter.getItem(position);  
            LatLng la = info.location;  
  
            // 动画跳转  
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(la);  
            mBaiduMap.animateMapStatus(u);  
  
            // 选中项打勾  
            mSelectImg.setBackgroundResource(R.drawable.rectangle_bg);  
            mSelectImg = (ImageView) view.findViewById(R.id.place_select);  
            mSelectImg.setBackgroundResource(R.drawable.check);  
              
        }  
  
    };  
    
	// 地图触摸事件监听器  
	OnMapStatusChangeListener touchListener = new OnMapStatusChangeListener() {  
		@Override
		public void onMapStatusChange(MapStatus arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onMapStatusChangeFinish(MapStatus arg0) {
			// TODO Auto-generated method stub
			 Point point =  mBaiduMap.getMapStatus().targetScreen;
		     LatLng centerLL = mBaiduMap.getProjection().fromScreenLocation(point);  
		     // 发起反地理编码检索  
             mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())  
                     .location(centerLL));  
             getDriverNet.setHandler(mHandler);
				getDriverNet.setLongitude(String.valueOf(centerLL.longitude));
				getDriverNet.setLatitude(String.valueOf(centerLL.latitude));
				getDriverNet.setDevice(Global.DEVICE);
				getDriverNet.setAuthn(MySharePreference.getStringValue(
						getApplication(), MySharePreference.AUTHN));
				getDriverNet.getDataFromServer();
		}

		@Override
		public void onMapStatusChangeStart(MapStatus arg0) {
			// TODO Auto-generated method stub	
		}  
    };  
    
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
                mInfoList.clear();  
                mInfoList.add(mCurentInfo);  
  
                // 将周边信息加入表  
                if (result.getPoiList() != null) {  
                    mInfoList.addAll(result.getPoiList());  
                }  
                // 通知适配数据已改变  
                mAdapter.notifyDataSetChanged();  
                //mLoadBar.setVisibility(View.GONE);  
                if(result.getPoiList() != null){
                   curaddress.setText(mInfoList.get(0).address);
                   slont = mInfoList.get(0).location.longitude;
                   slat = mInfoList.get(0).location.latitude;
                }else{
                   curaddress.setText("获取地址失败");
                }
            }  
        }  
    };  
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			
			break;
		case R.id.curaddress:{
			Intent intent = new Intent();
			intent.setClass(InnerCityHomeActivity.this,TypeAddressActivity.class);
			startActivityForResult(intent, TypeAddress);
			break;
		}
		case R.id.righttext:
		{//finish();
			Intent intent = new Intent();
			intent.setClass(InnerCityHomeActivity.this,BillingRuleActivity.class);
			startActivity(intent);
		}	break;
		case R.id.Search:
		{	
			if(curaddress.getText().toString().equals("获取地址失败")|curaddress.getText().toString().equals("地址获取中...")){
				Toast.makeText(InnerCityHomeActivity.this,"开始地址还未选择", Toast.LENGTH_LONG).show();
		    	
			}else{Intent intent = new Intent();
			intent.setClass(InnerCityHomeActivity.this, CarInfoActivity.class);	
			intent.putExtra("address", curaddress.getText().toString());
			intent.putExtra("lontitude", slont);
			intent.putExtra("latitude", slat);
			intent.putExtra("driveridStr",driveridStr);
			startActivity(intent);  
			}
		}	break;	
		case R.id.star:
		{	
			menuWindow = new AddressPopupWindow(InnerCityHomeActivity.this,itemOnClick,groups1);//实例化AddressPopupWindow
			menuWindow.showAsDropDown(originview, 0, 0); //显示窗口
		}	break;	
		default:
			break;
		}
	}
	
	//为弹出窗口实现监听类
    private OnItemClickListener  itemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				String address = menuWindow.getItemStr2(arg2);
				HashMap<String , Object> map = menuWindow.getMap(arg2);
				//System.out.println("map2:"+map.toString());
				String strlat = (String)map.get("latidute");
				String strlont = (String)map.get("longitude");
				slat =Double.parseDouble(strlat) ;
				slont =Double.parseDouble(strlont);
				
				Message message = Message.obtain();  
			    message.obj = address;  
				message.what = Global.ADDRESS_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				menuWindow.dismiss();   
		}
    };
    
	 @Override  
	 protected void onDestroy() {  
	        super.onDestroy();  
	        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
	  
	        // 退出时销毁定位
			mLocClient.stop();
			// 关闭定位图层
			mBaiduMap.setMyLocationEnabled(false);
			mMapView.onDestroy();
			mMapView = null;
	 }  
	 @Override  
	 protected void onResume() {  
	        super.onResume();  
	        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
	        mMapView.onResume();  
	        }  
	 @Override  
	 protected void onPause() {  
	        super.onPause();  
	        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
	        mMapView.onPause();  
	 }  

	 /**
		 * 定位SDK监听函数
		 */
	public class MyLocationListenner implements BDLocationListener {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// map view 销毁后不在处理新接收的位置
				if (location == null || mMapView == null)
					return;
			
			        
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				if (isFirstLoc) {
					
					//System.out.println(String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
					getDriverNet.setHandler(mHandler);
					getDriverNet.setLongitude(String.valueOf(location.getLongitude()));
					getDriverNet.setLatitude(String.valueOf(location.getLatitude()));
					getDriverNet.setDevice(Global.DEVICE);
					getDriverNet.setAuthn(MySharePreference.getStringValue(
							getApplication(), MySharePreference.AUTHN));
					getDriverNet.getDataFromServer();
					isFirstLoc = false;
					
					LatLng ll = new LatLng(location.getLatitude(),
							location.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
					
					
				}
			}

			public void onReceivePoi(BDLocation poiLocation) {
			}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.GETNEARBYDRIVER:{
	            	try {
						parseJSON((String)msg.obj);
					} catch (Exception e) {	
						e.printStackTrace();
					}      	
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
	            case Global.ADDRESS_MESSAGE:{
	            	String getaddress = (String)msg.obj;
	            	curaddress.setText(getaddress);	
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
    			 map.put("latidute",jsonobj2.getString("latitude"));
    			// System.out.println(map.toString());
    			 groups1.add(map);
            }
        }
        private void parseJSON(String str) throws Exception {
        
        	mBaiduMap.clear();
    		System.out.println(str);
    		JSONObject jsonobj = new JSONObject(str);
    		if (jsonobj.getInt("ResultCode") == Global.SUCCESS) {
    			JSONArray jsonarray = jsonobj.getJSONArray("Data");
    			for (int x = 0; x < jsonarray.length(); x++) {
    				JSONObject jsonobj2 = (JSONObject) jsonarray.get(x);
    				HashMap<String, String> map = new HashMap<String, String>();
    				map.put("carType", jsonobj2.getString("carType"));
    				map.put("driverName", jsonobj2.getString("driverName"));
    				map.put("phoneNum", jsonobj2.getString("phoneNum"));
    				map.put("longitude", jsonobj2.getString("longitude"));
    				map.put("latitude", jsonobj2.getString("latitude"));
    				map.put("driverNum", jsonobj2.getString("driverNum"));
    				driveridStr=driveridStr+jsonobj2.getString("driverNum")+",";
    				list.add(map);
    			}
    			for(int i=0;i<list.size();i++){
					double lat=Double.parseDouble((String)list.get(i).get("latitude"));
					double lont=Double.parseDouble((String)list.get(i).get("longitude"));
					LatLng llA = new LatLng(lat, lont);
					BitmapDescriptor bdA = BitmapDescriptorFactory
								.fromResource(R.drawable.car1);
					OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
								.zIndex(9).draggable(true);
					mBaiduMap.addOverlay(ooA);
				}
    		}else{
    			 //Toast.makeText(InnerCityHomeActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
    		}
    	}
}
