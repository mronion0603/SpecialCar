package com.lc.innercity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lc.net.AddAddressNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddressActivity extends Activity implements OnClickListener {
	public static final int REQUSET = 1;
    TextView tvTitle,righttext;
    TextView curaddress;
    ImageView ivleft;
    ImageView ivcenter,ivstar;
    private RelativeLayout rls;
    
    MapView mMapView = null;  
    BaiduMap mBaiduMap;
    // 定位相关
 	LocationClient mLocClient;
 	public MyLocationListenner myListener = new MyLocationListenner();
 	//private LocationMode mCurrentMode;
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
    double lat=0.0,lont=0.0;
    AddAddressNet addaddressnet = new AddAddressNet();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		 //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.innercity_addresshome);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("选择地址");
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("保存");
		righttext.setOnClickListener(this);
		curaddress = (TextView) findViewById(R.id.curaddress);
		curaddress.setOnClickListener(this);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		ivcenter = (ImageView) findViewById(R.id.ivpoint);
		ivstar = (ImageView) findViewById(R.id.star);
        //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.bmapView);  
        mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		String latstr = MySharePreference.getStringValue(getApplication(), MySharePreference.LAT);
		String lontstr = MySharePreference.getStringValue(getApplication(), MySharePreference.LONT);
	    double lat = 30.511876;   double lont = 114.405751;
	    if(latstr!=null){
	    	lat = Double.parseDouble(latstr);
	    	lont = Double.parseDouble(lontstr);
	    }
		LatLng cenpt = new LatLng(lat,lont); 
	    //定义地图状态
	    MapStatus mMapStatus = new MapStatus.Builder()
	        .target(cenpt)
	        .zoom(16)
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
        ivstar.setOnClickListener(this);
        
        LatLng centerLL = new LatLng(lat,lont);  
	     // 发起反地理编码检索  
        mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())  
              .location(centerLL));  
        
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
             
             //mLoadBar.setVisibility(View.VISIBLE);  
            
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
                	lat = mInfoList.get(0).location.latitude;
                	lont = mInfoList.get(0).location.longitude;
                   curaddress.setText(mInfoList.get(0).address);
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
		case R.id.star:
		{
		  addaddressnet.setHandler(mHandler);
      	  addaddressnet.setDevice(Global.DEVICE);
      	  addaddressnet.setAuth(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
      	  addaddressnet.setAddress(curaddress.getText().toString());
      	  addaddressnet.setLatidute(String.valueOf(lat));
      	  addaddressnet.setLongitude(String.valueOf(lont));
      	  addaddressnet.getDataFromServer();
		}	break;
		case R.id.righttext:
		{   Intent intent = new Intent();
	        String address =  curaddress.getText().toString();
	        intent.putExtra("address", address);
	        intent.putExtra("latidute",lat);
	        intent.putExtra("longitude", lont);
	        setResult(RESULT_OK, intent); 
			finish();
		}	break;
		case R.id.curaddress:
			Intent intent = new Intent();
			intent.setClass(AddressActivity.this, TypeAddressActivity.class);	
			startActivityForResult(intent, REQUSET);  
			break;	
		default:
			break;
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.ADDADDRESS:{
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
   	      //groupAdapter.notifyDataSetChanged();
   	    	Toast.makeText(AddressActivity.this,"添加地址成功", Toast.LENGTH_LONG).show();
        }else{
          Toast.makeText(AddressActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
        } 
   }
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
					isFirstLoc = false;
					LatLng ll = new LatLng(location.getLatitude(),
							location.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
					//initOverlay();
				}
			}

			public void onReceivePoi(BDLocation poiLocation) {
			}
	}

}
