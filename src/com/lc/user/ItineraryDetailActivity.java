package com.lc.user;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.innercity.CancelOrderActivity;
import com.lc.net.GetDetailDNet;
import com.lc.specialcar.R;
import com.lc.utils.CircularImage;
import com.lc.utils.ConnectUrl;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;
import com.lidroid.xutils.BitmapUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


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
    GetDetailDNet getDetailDNet= new GetDetailDNet();
	private ProgressBar pro; 
    @Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);

		setContentView(R.layout.userinfo_itinerarydetail);
		init();
		
	}

	public void init(){
		pro = (ProgressBar)findViewById(R.id.progress2); 
		pro.setProgress(0);  
		pro.setIndeterminate(true);
		Bundle extras = getIntent().getExtras();
	    if(extras != null){
	    	if(extras.containsKey("new")){
	    		orderNum = extras.getString("orderNum");
	    		getDetailDNet.setHandler(mhandler);
	    		getDetailDNet.setDevice(Global.DEVICE);
	    		getDetailDNet.setOrderNum(orderNum);
	    		getDetailDNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
	    		getDetailDNet.getCodeFromServer();
	    	}else{
	    		pro.setVisibility(View.GONE);
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
        if(extras != null){
	    if(!extras.containsKey("new")){
        tvdriverName.setText(driverName);
        tvcar.setText(carType);
        tvcarnumber.setText(carNum);
		tvOrderStatus.setText(orderStatus);
		tvriderName.setText(riderName);
		tvorderNum.setText(orderNum);
		tvusecarTime.setText(startTime);
		tvstartAddress.setText(startAddress);
        tvendAddress.setText(endAddress);
        tvmile.setText(mileage+"公里");
        tvtimelong.setText(time+"分钟");
        tvstartMoney.setText(bascMoney+"元");
        tvtimefee.setText(timeMoney+"元");
        tvlongfee.setText(R_BMoney+"元");
        tvstopcar.setText(stopCarMoney+"元");
        tvrealmoney.setText(realMoney+"元");
        
        BitmapUtils bitmapUtils = new BitmapUtils(ItineraryDetailActivity.this);
    	bitmapUtils.display(imdriver, ConnectUrl.commonurl0+driverImg);
    	rb.setRating(Float.parseFloat(asssScore));
	    }}
	}
	@SuppressLint("HandlerLeak")
	public Handler mhandler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.GETDETAILITINERARY:{
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
    	System.out.println(str);
    	pro.setVisibility(View.GONE);
    	//System.out.println("******listItem: "+listItem.size());
		JSONObject jsonobj = new JSONObject(str);
		int result = jsonobj.getInt("ResultCode");
		if (result == Global.SUCCESS) {
			JSONObject jsonobj2 = jsonobj.getJSONObject("Data");
			HashMap<String, Object> map = new HashMap<String, Object>();
				String getorderStatus = jsonobj2.getString("orderStatus");
				if(getorderStatus.equals("2")){        //司机已接单 乘客未上车
					orderStatus="待服务";   
				}else if(getorderStatus.equals("1")){  //订单结束，已扣费
					orderStatus="已完成";
				}else if(getorderStatus.equals("3")){  //乘客已上车
					orderStatus="服务中";   
				}else if(getorderStatus.equals("-1")){
					orderStatus="取消";
				}else{
					orderStatus="待付款";
				}
				startTime= jsonobj2.getString("startTime");
				startAddress= jsonobj2.getString("startAddress");
				String getSerTypeId = jsonobj2.getString("serTypeId");
				if(getSerTypeId.equals("1")){
					serTypeId= "接机";
				}else if(getSerTypeId.equals("2")){
					serTypeId= "送机";	
				}else if(getSerTypeId.equals("3")){
					serTypeId= "公务包车";
				}else if(getSerTypeId.equals("4")){
					serTypeId= "市内约租";
				}else{
					serTypeId= "城际约租";
				}
				carTypeId= jsonobj2.getString("carTypeId");
				endAddress= jsonobj2.getString("endAddress");
				useCarTime= jsonobj2.getString("useCarTime");
				carSum= jsonobj2.getString("carSum");
				riderName= jsonobj2.getString("riderName");
				riderPhone= jsonobj2.getString("riderPhone");
				comment= jsonobj2.getString("comment");
				mileage= jsonobj2.getString("mileage");
				time=jsonobj2.getString("time");
				realMoney= jsonobj2.getString("realMoney");
				
				carNum= jsonobj2.getString("carNum");
				carType= jsonobj2.getString("carType");
				driverName= jsonobj2.getString("driverName");
				driverImg= jsonobj2.getString("driverImg");
				mileageMoney= jsonobj2.getString("mileageMoney");
				bascMoney= jsonobj2.getString("bascMoney");
				timeMoney=jsonobj2.getString("timeMoney");
				R_BMoney= jsonobj2.getString("R_BMoney");
				stopCarMoney= jsonobj2.getString("stopCarMoney");
				asssScore= jsonobj2.getString("assessScore");
				//slongitude= jsonobj2.getString("slongitude");
				//slatitude= jsonobj2.getString("slatitude");
				//elongitude= jsonobj2.getString("elongitude");
				//elatitude= jsonobj2.getString("elatitude");
				assessType= jsonobj2.getString("assessType");
		
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
			        tvmile.setText(mileage+"公里");
			        tvtimelong.setText(time+"分钟");
			        tvstartMoney.setText(bascMoney+"元");
			        tvtimefee.setText(timeMoney+"元");
			        tvlongfee.setText(R_BMoney+"元");
			        tvstopcar.setText(stopCarMoney+"元");
			        tvrealmoney.setText(realMoney+"元");
			        
			        BitmapUtils bitmapUtils = new BitmapUtils(ItineraryDetailActivity.this);
			    	bitmapUtils.display(imdriver, ConnectUrl.commonurl0+driverImg);
			    	rb.setRating(Float.parseFloat(asssScore));
		} else {
			Toast.makeText(getApplication(),jsonobj.getString("Message") , Toast.LENGTH_SHORT).show();
		}
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
