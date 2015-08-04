package com.lc.user;

import com.lc.innercity.CancelOrderActivity;
import com.lc.specialcar.R;
import com.lc.utils.CircularImage;
import com.lc.utils.ConnectUrl;
import com.lc.utils.ExitApplication;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ItineraryDetailActivity extends Activity implements OnClickListener {
	public final static int CANCEL=1;
	public final static int COMMENT=2;
	RatingBar rb;
    TextView tvTitle;
    ImageView ivleft;
    CircularImage imdriver;
    private RelativeLayout rls;
    private LinearLayout llextend;
    String orderStatus="", orderNum="", startTime="", startAddress="",
    	   serTypeId="", carTypeId="", airport="",
    	   endAddress="", useCarTime="", carSum="",riderName="",
    	   riderPhone="",comment="",mileage="",time="",realMoney="",
    	   carNum="",carType="",driverName="",driverImg="",
           mileageMoney="",bascMoney="",timeMoney="",assessType="",
        		   asssScore="",R_BMoney="",stopCarMoney="";
    Button bt;
    TextView tvdriverName,tvcar,tvcarnumber,tvOrderStatus,tvriderName,tvorderNum,tvusecarTime,tvstartAddress,
             tvendAddress,tvmile,tvtimelong,tvstartMoney,tvtimefee,tvlongfee,tvstopcar,tvrealmoney;
    //CancelInnerNet cancelInnerNet = new CancelInnerNet();
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
	    	R_BMoney= extras.getString("R_BMoney");
	    	assessType= extras.getString("assessType");
	    	endAddress= extras.getString("EndAddress");
	    	useCarTime= extras.getString("UseCarTime");
	    	carSum= extras.getString("CarSum");
	    	riderName= extras.getString("RiderName");
	    	riderPhone= extras.getString("RiderPhone");
	    	comment= extras.getString("Comment");
	    	mileage= extras.getString("Mileage");
	    	time= extras.getString("Time");
	    	realMoney= extras.getString("RealMoney");
	    	
	    	carNum= extras.getString("carNum");
	    	carType= extras.getString("carType");
	    	driverName= extras.getString("driverName");
	    	driverImg= extras.getString("driverImg");
	    	mileageMoney= extras.getString("mileageMoney");
	    	bascMoney= extras.getString("bascMoney");
	    	timeMoney= extras.getString("timeMoney");
	    	stopCarMoney= extras.getString("stopCarMoney");
	    	asssScore= extras.getString("asssScore");
	    }
		ExitApplication.getInstance().addActivity(this);
		bt= (Button) findViewById(R.id.Search);
		bt.setOnClickListener(this);
		rb= (RatingBar) findViewById(R.id.ratingBar1);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("行程明细");
		llextend= (LinearLayout) findViewById(R.id.llextend);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		imdriver=(CircularImage) findViewById(R.id.driverImg);
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
        tvstopcar=(TextView) findViewById(R.id.discount);
        tvrealmoney=(TextView) findViewById(R.id.realmoney);
        if(orderStatus.equals("待服务")){
        	llextend.setVisibility(View.GONE);
        	bt.setText("取消");
        }else if(orderStatus.equals("待付款")){
        	llextend.setVisibility(View.VISIBLE);
        	bt.setText("去充值");
        }else if(orderStatus.equals("已完成")){
        	llextend.setVisibility(View.VISIBLE);
        	bt.setText("去评价");
        	if(assessType.equals("1")){
        		bt.setVisibility(View.GONE);
        	}
        }else if(orderStatus.equals("取消")){
        	llextend.setVisibility(View.VISIBLE);
        	bt.setVisibility(View.GONE);
        }else if(orderStatus.equals("服务中")){
        	llextend.setVisibility(View.GONE);
        	bt.setVisibility(View.GONE);
        }
        
        tvdriverName.setText(driverName);
        tvcar.setText(carType);
        tvcarnumber.setText(carNum);
		tvOrderStatus.setText(orderStatus);
		tvriderName.setText(riderName);
		tvorderNum.setText(orderNum);
		tvusecarTime.setText(startTime);
		tvstartAddress.setText(startAddress);
        tvendAddress.setText(endAddress);
        tvmile.setText(mileage);
        tvtimelong.setText(time);
        tvstartMoney.setText(bascMoney);
        tvtimefee.setText(timeMoney);
        tvlongfee.setText(R_BMoney);
        tvstopcar.setText(stopCarMoney);
        tvrealmoney.setText(realMoney);
        
        BitmapUtils bitmapUtils = new BitmapUtils(ItineraryDetailActivity.this);
    	bitmapUtils.display(imdriver, ConnectUrl.commonurl0+driverImg);
    	rb.setRating(Float.parseFloat(asssScore));
    	
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
		{	finish();
		}	break;
		case R.id.Search:
		{
			if(bt.getText().toString().equals("去充值")){
				Intent intent =new Intent();
				intent.setClass(getApplication(), ChargeActivity.class);
				startActivity(intent);
			}
			if(bt.getText().toString().equals("取消")){
				Intent intent =new Intent();
				intent.setClass(getApplication(), CancelOrderActivity.class);
				if(serTypeId.equals("城际约租")){
					intent.putExtra("SerTypeId", serTypeId);
				}
				intent.putExtra("OrderNum", orderNum);
				startActivityForResult(intent,CANCEL);
				
			}
			if(bt.getText().toString().equals("去评价")){
				Intent intent =new Intent();
				intent.setClass(getApplication(), CommentActivity.class);
				intent.putExtra("OrderNum", orderNum);
				startActivityForResult(intent,COMMENT);
			}
		}break;
			
		default:
			break;
		}
	}
	
	 @Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        super.onActivityResult(requestCode, resultCode, data);  
	        if (requestCode == CANCEL && resultCode == RESULT_OK) { 
	        	     Intent intent = new Intent();
         	         setResult(RESULT_OK, intent); 
	            	 finish();
	        }  
	        if (requestCode == COMMENT && resultCode == RESULT_OK) { 
       	         Intent intent = new Intent();
    	         setResult(RESULT_OK, intent); 
           	     finish();
       }  
	 }
}
