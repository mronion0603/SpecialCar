package com.lc.innercity;

import java.util.Timer;
import java.util.TimerTask;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.lc.specialcar.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SendDealActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,righttext;
    ImageView ivleft,sendbg;
    TextView btWait;
    private RelativeLayout rls;
    MapView mMapView = null;  
    private Timer timer = new Timer(); 
	private int recLen=30;
	private boolean startTimer = false;
	private AnimationDrawable animationDrawable;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		 //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.innercity_senddeal);
		init();
		
	}

	public void init(){
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
        recLen=120;
		if(!startTimer){
		timer.schedule(task, 1000, 1000);       // timeTask  
		}
		startTimer = true;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
			finish();
			Intent intent = new Intent();
			intent.setClass(SendDealActivity.this,CancelOrderActivity.class);
			startActivity(intent);
			break;
		
			
		default:
			break;
		}
	}
	
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
	   
	    TimerTask task = new TimerTask() {  
	        @Override  
	        public void run() {  
	  
	            runOnUiThread(new Runnable() {      // UI thread  
	                @Override  
	                public void run() {  
	                    recLen--;  
	                    btWait.setText(recLen+"s");  
	                    if(recLen <= 0){  
	                    	 finish();
	                    	 Intent intent = new Intent();
	             			 intent.setClass(SendDealActivity.this,DriverDistributeActivity.class);
	             			 startActivity(intent);
	                    }  
	                }  
	            });  
	        }  
	    };  
}
