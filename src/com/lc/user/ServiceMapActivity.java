package com.lc.user;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class ServiceMapActivity extends Activity  implements BaiduMap.OnMapClickListener,
OnGetRoutePlanResultListener{
	boolean useDefaultIcon = true;
	 MapView mMapView = null;  
	 private ImageView backbt;
	 Button titleguide,titleguide2;
     private TextView title;
	 private BaiduMap mBaiduMap;
	 BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.end);
	 BitmapDescriptor bdB = BitmapDescriptorFactory
				.fromResource(R.drawable.start);
	 private Marker mMarkerA;
	 private Marker mMarkerB;
	 double longitude,latitude;
	 
	 public LocationClient mLocationClient = null;
	 private LocationMode tempMode = LocationMode.Hight_Accuracy;
	 public BDLocationListener myListener = new MyLocationListener();
     private double mylongitude,mylatitude;
     private double getmylongitude,getmylatitude;
	 private String tempcoor="bd09ll";
	 boolean getlocation = false;
	 //搜索相关
	 RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
	 int nodeIndex = -1;//节点索引,供浏览节点时使用
	 RouteLine route = null;
	 OverlayManager routeOverlay = null;
	 LatLng llA,llB;
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
      //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.userinfo_service);  
        ExitApplication.getInstance().addActivity(this);
        init();
	}
	public void init(){
		 //重置浏览节点的路线数据
        route = null;
		Bundle extras = getIntent().getExtras();
        if(extras != null){
        	longitude = Double.parseDouble(extras.getString("elongitude"));
        	latitude = Double.parseDouble(extras.getString("elatitude"));
        	getmylongitude = Double.parseDouble(extras.getString("slongitude"));
        	getmylatitude = Double.parseDouble(extras.getString("slatitude"));
        }
       
		
        //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.bmapView);  
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		initOverlay();
		
		title = (TextView)findViewById(R.id.topTv);
		title.setText("服务中");
		backbt = (ImageView)findViewById(R.id.ArrowHead);
		backbt.setVisibility(View.VISIBLE);
		backbt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
		titleguide = (Button)findViewById(R.id.guide1);
		titleguide.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				LatLng pt1 =llB;
				LatLng pt2 = llA;
				// 构建 导航参数
				NaviPara para = new NaviPara();
				para.startPoint = pt1;
				para.startName = "从这里开始";
				para.endPoint = pt2;
				para.endName = "到这里结束";

				try {

					BaiduMapNavigation.openBaiduMapNavi(para, v.getContext());

				} catch (BaiduMapAppNotSupportNaviException e) {
					e.printStackTrace();
					AlertDialog.Builder builder = new AlertDialog.Builder(ServiceMapActivity.this);
					builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
					builder.setTitle("提示");
					builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							BaiduMapNavigation.getLatestBaiduMapApp(ServiceMapActivity.this);
						}

						
					});

					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

					builder.create().show();
				}
			}
		});
		titleguide2 = (Button)findViewById(R.id.guide2);
		titleguide2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				LatLng pt1 = llB;
				LatLng pt2 = llA;
				// 构建 导航参数
				NaviPara para = new NaviPara();
				para.startPoint = pt1;
				para.endPoint = pt2;
				BaiduMapNavigation.openWebBaiduMapNavi(para, v.getContext());
			}
		});
		 // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        PlanNode stNode = PlanNode.withLocation(llB);
        PlanNode enNode = PlanNode.withLocation(llA);
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode));
	}
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mMapView.onDestroy();  
     // 回收 bitmap 资源
     		bdA.recycle();
     		bdB.recycle();
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
    public void initOverlay() {
		// add marker overlay
		 llA = new LatLng(latitude, longitude);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		 llB = new LatLng(getmylatitude, getmylongitude);
		OverlayOptions ooB = new MarkerOptions().position(llB).icon(bdB)
				.zIndex(5);
		mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
		
		MapStatusUpdate u = MapStatusUpdateFactory
				.newLatLng(llA);
		mBaiduMap.setMapStatus(u);
		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			public void onMarkerDrag(Marker marker) {
			}

			public void onMarkerDragEnd(Marker marker) {
				Toast.makeText(
						ServiceMapActivity.this,
						"拖拽结束，新位置：" + marker.getPosition().latitude + ", "
								+ marker.getPosition().longitude,
						Toast.LENGTH_LONG).show();
			}

			public void onMarkerDragStart(Marker marker) {
			}
		});
	}
    public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
		            return ;
			if(!getlocation){
				getlocation=true;
				mylatitude =location.getLatitude();
				mylongitude =location.getLongitude();
				
			}
		}
	}
    
  //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.start);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.end);
            }
            return null;
        }
    }
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		// TODO Auto-generated method stub
		  if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	            Toast.makeText(ServiceMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
	        }
	        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	            //result.getSuggestAddrInfo()
	            return;
	        }
	        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	            nodeIndex = -1;
	            //mBtnPre.setVisibility(View.VISIBLE);
	            //mBtnNext.setVisibility(View.VISIBLE);
	            route = result.getRouteLines().get(0);
	            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
	            routeOverlay = overlay;
	            mBaiduMap.setOnMarkerClickListener(overlay);
	            overlay.setData(result.getRouteLines().get(0));
	            overlay.addToMap();
	            overlay.zoomToSpan();
	        }
	}
	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
