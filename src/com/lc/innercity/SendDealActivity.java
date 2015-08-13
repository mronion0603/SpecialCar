package com.lc.innercity;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.lc.net.CancelInnerNet;
import com.lc.popupwindow.ContactWindow;
import com.lc.specialcar.R;
import com.lc.utils.CircularImage;
import com.lc.utils.ConnectUrl;
import com.lc.utils.DeleteDealApplication;
import com.lc.utils.DeleteWXPayApplication;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SendDealActivity extends Activity implements OnClickListener {
	public final static int CANCEL=1;
    TextView tvTitle,righttext,tvdrivername,tvcarnum;
    ImageView ivleft,sendbg,ivcontactdriver;
    CircularImage ivdriverimg;
    String phonenum="";
    TextView btWait;
    private RelativeLayout rls,rlwait,rldriver;
    MapView mMapView = null;  
    private Timer timer = new Timer(); 
	private int recLen=TIMELONG;
	private boolean startTimer = false;
	private AnimationDrawable animationDrawable;
	public static boolean isForeground = false;
	//for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static final int DIAL_PHONE = 1;
	public static final int  CONTINUE =2;
	ContactWindow contactWindow;
	private View originview; 
	private MyAlertDialog myAlertDialog;  //自定义对话框
	public static final int  TIMELONG =120;
	BaiduMap mBaiduMap;
	CancelInnerNet cancelInnerNet = new CancelInnerNet();
	String getOrderNum="";
	TextView tvmessage;
	ImageView ivline;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		 //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.innercity_senddeal);
		init();
		registerMessageReceiver();  // used for receive msg
	}

	public void init(){
		DeleteDealApplication.getInstance().addActivity(this);
		Bundle extras = getIntent().getExtras();
	    if(extras != null){
	    	getOrderNum = extras.getString("orderNum");
	       
	    }
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.innercity_senddeal, null); 
		ExitApplication.getInstance().addActivity(this);
		tvdrivername = (TextView) findViewById(R.id.drivername);
		tvcarnum = (TextView) findViewById(R.id.carNum);
		ivcontactdriver = (ImageView) findViewById(R.id.contactDriver);
		ivline = (ImageView) findViewById(R.id.ivline);
		ivcontactdriver.setOnClickListener(this);
		ivdriverimg = (CircularImage) findViewById(R.id.driverimg);
		tvmessage = (TextView) findViewById(R.id.message);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("正在派单");
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("取消订单");
		righttext.setOnClickListener(this);
		btWait = (TextView) findViewById(R.id.Wait);
		//btWait.setOnClickListener(this);
		//ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		rlwait = (RelativeLayout) findViewById(R.id.rlwait);
		rldriver = (RelativeLayout) findViewById(R.id.usecar);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		sendbg = (ImageView) findViewById(R.id.sendbg);
		sendbg.setImageResource(R.anim.breath);
		animationDrawable = (AnimationDrawable) sendbg.getDrawable();
		sendbg.post(new Runnable() { // 在异步线程中执行启动的方法
				@Override
				public void run() {
					// TODO Auto-generated method stub
					animationDrawable.start(); // 启动动画
				}
			 });
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
        
        recLen=TIMELONG;
		if(!startTimer){
		timer.schedule(task, 1000, 1000);       // timeTask  
		}
		startTimer = true;
	}
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}
	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String message = intent.getStringExtra(KEY_MESSAGE);
              
              try {
            	int gettype = type(message);
            	if(gettype==0){
            		parseJSON(message);
            		timer.cancel();
         	        if(animationDrawable!=null){
         	           animationDrawable.stop();
         			   animationDrawable =null; 
         	        }
                    rlwait.setVisibility(View.GONE);
                    rldriver.setVisibility(View.VISIBLE);
                    ivline.setVisibility(View.VISIBLE);
            	}else if(gettype==3){
            		tvmessage.setText("您已上车，祝您旅途愉快!");
            	}
  				
  			  } catch (Exception e) {
  				e.printStackTrace();
  			  }

			}
		}
	}
	  private int type(String str)throws Exception{
		   int gettype = -1;
		   JSONObject jsonobj = new JSONObject(str);
		   int result = jsonobj.getInt("ResultCode");
			if (result == Global.SUCCESS) {
				JSONObject jsonobj2 = jsonobj.getJSONObject("Data");
				if(jsonobj2.getString("type").equals("0")){
					gettype=0;
				}else if(jsonobj2.getString("type").equals("3")){
					gettype=3;
				}
			}
			return gettype;
	  }
	  private void parseJSON(String str)throws Exception{  
	    	System.out.println(str);
			JSONObject jsonobj = new JSONObject(str);
			int result = jsonobj.getInt("ResultCode");
			if (result == Global.SUCCESS) {
				JSONObject jsonobj2 = jsonobj.getJSONObject("Data");
				tvdrivername.setText(jsonobj2.getString("driverName"));
				tvcarnum.setText(jsonobj2.getString("carNum"));
				phonenum=jsonobj2.getString("phoneNum");
					
				BitmapUtils bitmapUtils = new BitmapUtils(SendDealActivity.this);
		        bitmapUtils.display(ivdriverimg, ConnectUrl.commonurl0+jsonobj2.getString("driverImg"));
			} else {

			}
	    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.contactDriver:
		{	contactWindow = new ContactWindow(SendDealActivity.this,itemOnClick,phonenum);
	        contactWindow.showAsDropDown(originview, 0, 0); 
		}	break;
		case R.id.righttext:
			//finish();
			Intent intent = new Intent();
			intent.setClass(SendDealActivity.this,CancelOrderActivity.class);
			intent.putExtra("OrderNum", getOrderNum);
			startActivityForResult(intent,CANCEL);
			
			break;
		default:
			break;
		}
	}
	//为弹出窗口实现监听类
		private OnClickListener itemOnClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.telphone: {
					  // getphone=contactWindow.getItemStr();
					   mHandler.sendEmptyMessage(DIAL_PHONE);
					}
					break;
				case R.id.cancel: {
					  contactWindow.dismiss();
				    }
					break;
				}
			}
		};
		private Handler mHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch(msg.what) {
	            case DIAL_PHONE:{
	            	//用intent启动拨打电话  
	                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phonenum));  
	                startActivity(intent);  
	                break;
	                }
	            case CONTINUE:{
				animationDrawable = (AnimationDrawable) sendbg.getDrawable();
				sendbg.post(new Runnable() { // 在异步线程中执行启动的方法
					@Override
					public void run() {
						// TODO Auto-generated method stub
						animationDrawable.start(); // 启动动画
					}
				});
				recLen = TIMELONG;
				// timer.schedule(task, 1000, 1000); // timeTask
	            break;
	            }
	            case Global.CANCEL_INNER:{
	            	//用intent启动拨打电话  
	                //finish();
	                break;
	                }
	            }
	        }};
	 @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
	        mMapView.onDestroy();  
	       
	        timer.cancel();
	        if(animationDrawable!=null){
	           animationDrawable.stop();
			   animationDrawable =null; 
	        }
	        //unregisterReceiver(mMessageReceiver);
	    }  
	    @Override  
	    protected void onResume() {  
	        super.onResume();  
	        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
	        mMapView.onResume();  
	        isForeground = true;
	    }  
	    @Override  
	    protected void onPause() {  
	        super.onPause();  
	        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
	        mMapView.onPause();  
	        isForeground = false;
	    }  
	   
	    TimerTask task = new TimerTask() {  
	        @Override  
	        public void run() {   
	            runOnUiThread(new Runnable() {      // UI thread  
	                @Override  
	                public void run() {  	                   
					if (recLen <= 0) {
						// timer.cancel();
						if (animationDrawable != null) {
							animationDrawable.stop();
							animationDrawable = null;
							myAlertDialog = new MyAlertDialog(
									SendDealActivity.this);
							myAlertDialog.setContentText("是否继续等待？");
							myAlertDialog.setButtonText("是", "否");
							myAlertDialog
									.setOnPositiveButton(new OnClickListener() {
										@Override
										public void onClick(View v) {
											mHandler.sendEmptyMessageDelayed(
													CONTINUE, 2);
											myAlertDialog.dismiss();
										}
									});
							myAlertDialog
									.setOnNegativeButton(new OnClickListener() {
										@Override
										public void onClick(View v) {
											cancelInnerNet.setHandler(mHandler);
											cancelInnerNet.setOrderNum(getOrderNum);
											cancelInnerNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
											cancelInnerNet.getDataFromServer();
											myAlertDialog.dismiss();
											finish();
										}
									});

							// myAlertDialog.setCancelable(false);
							myAlertDialog.show();
						}
					} else {
						recLen--;
						btWait.setText(recLen + "s");
					}
	                }  
	            });  
	        }  
	    };  
	    @Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        super.onActivityResult(requestCode, resultCode, data);  
	        if (requestCode == CANCEL && resultCode == RESULT_OK) {        	 
	            	 //System.out.println("*****");
	            	 finish();
	        }  
	 }
}
