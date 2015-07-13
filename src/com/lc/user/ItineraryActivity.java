package com.lc.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.net.GetOrderNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class ItineraryActivity extends Activity implements OnClickListener {
	
    TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;
    private ListView listview;
  	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
  	GetOrderNet getOrderNet = new GetOrderNet();
  	SimpleAdapter listItemAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);

		setContentView(R.layout.userinfo_itinerary);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("行程");

		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		listview=(ListView)findViewById(R.id.listview);
		getData();
		listItemAdapter = new SimpleAdapter(this,listItem,R.layout.userinfo_itinerary_listitem , 
				new String[]{"OrderStatus","OrderDate","SerTypeId","OrderAddress","EndAddress"},
				new int[]{R.id.OrderStatus,R.id.OrderDate,R.id.OrderType,R.id.OrderAddress,R.id.OrderEndAddress});
		listview.setDividerHeight(20);
		
		listview.setAdapter(listItemAdapter);
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(getApplication(), ItineraryDetailActivity.class);
				intent.putExtra("OrderStatus", (String)listItem.get(arg2).get("OrderStatus"));
				intent.putExtra("OrderNum", (String)listItem.get(arg2).get("OrderNumber"));
				intent.putExtra("StartTime", (String)listItem.get(arg2).get("OrderDate"));
				intent.putExtra("StartAddress", (String)listItem.get(arg2).get("OrderAddress"));
				intent.putExtra("SerTypeId", (String)listItem.get(arg2).get("SerTypeId"));
				intent.putExtra("CarTypeId", (String)listItem.get(arg2).get("CarTypeId"));
				intent.putExtra("FlightNum", (String)listItem.get(arg2).get("FlightNum"));
				intent.putExtra("Airport", (String)listItem.get(arg2).get("Airport"));
				intent.putExtra("EndAddress", (String)listItem.get(arg2).get("EndAddress"));
				intent.putExtra("UseCarTime", (String)listItem.get(arg2).get("UseCarTime"));
				intent.putExtra("CarSum", (String)listItem.get(arg2).get("CarSum"));
				intent.putExtra("RiderName", (String)listItem.get(arg2).get("RiderName"));
				intent.putExtra("RiderPhone", (String)listItem.get(arg2).get("RiderPhone"));
				intent.putExtra("Comment", (String)listItem.get(arg2).get("Comment"));
				intent.putExtra("Mileage", (String)listItem.get(arg2).get("Mileage"));
				intent.putExtra("Time", (String)listItem.get(arg2).get("Time"));
				intent.putExtra("RealMoney", (String)listItem.get(arg2).get("RealMoney"));
				
				intent.putExtra("carNum", (String)listItem.get(arg2).get("carNum"));
				intent.putExtra("carType", (String)listItem.get(arg2).get("carType"));
				intent.putExtra("driverName", (String)listItem.get(arg2).get("driverName"));
				intent.putExtra("driverImg", (String)listItem.get(arg2).get("driverImg"));
				intent.putExtra("mileageMoney", (String)listItem.get(arg2).get("mileageMoney"));
				intent.putExtra("bascMoney", (String)listItem.get(arg2).get("bascMoney"));
				intent.putExtra("timeMoney", (String)listItem.get(arg2).get("timeMoney"));
				intent.putExtra("vouMoney", (String)listItem.get(arg2).get("vouMoney"));
				intent.putExtra("asssScore", (String)listItem.get(arg2).get("asssScore"));
				startActivity(intent);
			}
			
		});
	}
	void getData(){
		getOrderNet.setHandler(mhandler);
		getOrderNet.setDevice(Global.DEVICE);
		getOrderNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		getOrderNet.getCodeFromServer();
		/*
		for(int i=0;i<5;i++){
		     HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("OrderStatus","已完成");
			 map.put("OrderNumber", "20002313123");
			 map.put("OrderDate", "2015-5-5 10:00");
			 map.put("OrderAddress","汉口火车站");
			 listItem.add(map);
		}
		*/
	}
	@SuppressLint("HandlerLeak")
	public Handler mhandler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.GETORDER:{
	            		try {
							parseJSON((String)msg.obj);
							listItemAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							
							e.printStackTrace();
						}      	
	             break;
                }
            }
    }};
    private void parseJSON(String str)throws Exception{  
    	System.out.println(str);
		JSONObject jsonobj = new JSONObject(str);
		int result = jsonobj.getInt("ResultCode");
		if (result == Global.SUCCESS) {
			JSONArray jsonarray = jsonobj.getJSONArray("Data");
			for (int x = 0; x < jsonarray.length(); x++) {
				JSONObject jsonobj2 = (JSONObject) jsonarray.get(x);
				HashMap<String, Object> map = new HashMap<String, Object>();
				String getorderStatus = jsonobj2.getString("orderStatus");
				if(getorderStatus.equals("0")){
					map.put("OrderStatus","待服务");
				}else if(getorderStatus.equals("1")){
					map.put("OrderStatus","已完成");
				}else if(getorderStatus.equals("2")){
					map.put("OrderStatus","服务中");
				}else if(getorderStatus.equals("-1")){
					map.put("OrderStatus","取消");
				}else{
					map.put("OrderStatus","代付款");
				}
				map.put("OrderNumber", jsonobj2.getString("orderNum"));
				map.put("OrderDate", jsonobj2.getString("startTime"));
				map.put("OrderAddress", jsonobj2.getString("startAddress"));
				String getSerTypeId = jsonobj2.getString("SerTypeId");
				if(getSerTypeId.equals("1")){
				   map.put("SerTypeId", "接机");
				}else if(getSerTypeId.equals("2")){
					map.put("SerTypeId", "送机");	
				}else if(getSerTypeId.equals("3")){
					map.put("SerTypeId", "公务包车");
				}else if(getSerTypeId.equals("4")){
					map.put("SerTypeId", "市内约租");
				}else{
					map.put("SerTypeId", "城际约租");
				}
				map.put("CarTypeId", jsonobj2.getString("carTypeId"));
				map.put("FlightNum", jsonobj2.getString("flightNum"));
				map.put("Airport", jsonobj2.getString("airport"));
				map.put("EndAddress", jsonobj2.getString("endAddress"));
				map.put("UseCarTime", jsonobj2.getString("useCarTime"));
				map.put("CarSum", jsonobj2.getString("carSum"));
				map.put("RiderName", jsonobj2.getString("riderName"));
				map.put("RiderPhone", jsonobj2.getString("riderPhone"));
				map.put("Comment", jsonobj2.getString("comment"));
				map.put("Mileage", jsonobj2.getString("mileage"));
				map.put("Time", jsonobj2.getString("time"));
				map.put("RealMoney", jsonobj2.getString("realMoney"));
				
				map.put("carNum", jsonobj2.getString("carNum"));
				map.put("carType", jsonobj2.getString("carType"));
				map.put("driverName", jsonobj2.getString("driverName"));
				map.put("driverImg", jsonobj2.getString("driverImg"));
				map.put("mileageMoney", jsonobj2.getString("mileageMoney"));
				map.put("bascMoney", jsonobj2.getString("bascMoney"));
				map.put("timeMoney", jsonobj2.getString("timeMoney"));
				map.put("vouMoney", jsonobj2.getString("vouMoney"));
				map.put("asssScore", jsonobj2.getString("assessScore"));
				listItem.add(map);
			}
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
		default:
			break;
		}
	}
	
 
}
