package com.lc.user;


import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ItineraryDetailActivity extends Activity implements OnClickListener {
	
    TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;
    private LinearLayout llextend;
    String orderStatus="", orderNum="", startTime="", startAddress="",
    	   serTypeId="", carTypeId="", flightNum="", airport="",
    	   endAddress="", useCarTime="", carSum="",riderName="",
    	   riderPhone="",comment="",mileage="",time="",realMoney="";
    TextView tvdriverName,tvcar,tvcarnumber,tvOrderStatus,tvriderName,tvorderNum,tvusecarTime,tvstartAddress,
             tvendAddress,tvmile,tvtimelong,tvstartMoney,tvtimefee,tvlongfee,tvdiscount,tvrealmoney;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);

		setContentView(R.layout.userinfo_itinerarydetail);
		init();
		
	}

	public void init(){
		Bundle extras = getIntent().getExtras();
	    if(extras != null){
	    	orderStatus = extras.getString("OrderStatus");
	    	orderNum = extras.getString("OrderNum");
	    	startTime = extras.getString("StartTime");
	    	startAddress= extras.getString("StartAddress");
	    	serTypeId= extras.getString("SerTypeId");
	    	carTypeId= extras.getString("CarTypeId");
	    	flightNum= extras.getString("FlightNum");
	    	airport= extras.getString("Airport");
	    	endAddress= extras.getString("EndAddress");
	    	useCarTime= extras.getString("UseCarTime");
	    	carSum= extras.getString("CarSum");
	    	riderName= extras.getString("RiderName");
	    	riderPhone= extras.getString("RiderPhone");
	    	comment= extras.getString("Comment");
	    	mileage= extras.getString("Mileage");
	    	time= extras.getString("Time");
	    	realMoney= extras.getString("RealMoney");
	    }
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("行程明细");
		llextend= (LinearLayout) findViewById(R.id.llextend);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		
		tvdriverName=(TextView) findViewById(R.id.driverName);
		tvcar=(TextView) findViewById(R.id.car);
		tvcarnumber=(TextView) findViewById(R.id.carnumber);
		tvOrderStatus=(TextView) findViewById(R.id.OrderStatus);
		tvriderName=(TextView) findViewById(R.id.riderName);
		tvorderNum=(TextView) findViewById(R.id.OrderNum);
		tvusecarTime=(TextView) findViewById(R.id.usecarTime);
		tvstartAddress=(TextView) findViewById(R.id.startAddress);
        tvendAddress=(TextView) findViewById(R.id.endAddress);
        tvmile=(TextView) findViewById(R.id.mile);
        tvtimelong=(TextView) findViewById(R.id.timelong);
        tvstartMoney=(TextView) findViewById(R.id.startMoney);
        tvtimefee=(TextView) findViewById(R.id.timefee);
        tvlongfee=(TextView) findViewById(R.id.longfee);
        tvdiscount=(TextView) findViewById(R.id.discount);
        tvrealmoney=(TextView) findViewById(R.id.realmoney);
        if(orderStatus.equals("待服务")){
        	llextend.setVisibility(View.GONE);
        }else if(orderStatus.equals("待付款")){
        	llextend.setVisibility(View.VISIBLE);
        }else if(orderStatus.equals("已完成")){
        	llextend.setVisibility(View.VISIBLE);
        }else if(orderStatus.equals("取消")){
        	
        }else if(orderStatus.equals("服务中")){
        	
        }
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		
		default:
			break;
		}
	}
	
 
}
