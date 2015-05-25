package com.lc.innercity;



import java.util.ArrayList;
















import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lc.setting.ButtonEffect;
import com.lc.specialcar.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class AddressActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,righttext;
    TextView curaddress;
    ImageView ivleft;
    ImageView ivcenter;
    private RelativeLayout rls;
    
    MapView mMapView = null;  
    BaiduMap mBaiduMap;
    // 定位相关
 	LocationClient mLocClient;
 	public MyLocationListenner myListener = new MyLocationListenner();
 	private LocationMode mCurrentMode;
 	BitmapDescriptor mCurrentMarker;
 	boolean isFirstLoc = true;// 是否首次定位
    // 地理编码  
    GeoCoder mGeoCoder = null;
    // 位置列表  
 
    
    List<PoiInfo> mInfoList;  
    PoiInfo mCurentInfo;  
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
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("市内预约");
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("保存");
		righttext.setOnClickListener(this);
		curaddress = (TextView) findViewById(R.id.curaddress);
		
		
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		ivcenter = (ImageView) findViewById(R.id.ivpoint);
		
        //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.bmapView);  
        mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
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
		
	}
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
                //mAdapter.notifyDataSetChanged();  
                //mLoadBar.setVisibility(View.GONE);  
                curaddress.setText(mInfoList.get(0).address);
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
		case R.id.righttext:
			finish();
			break;
		
			
		default:
			break;
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
