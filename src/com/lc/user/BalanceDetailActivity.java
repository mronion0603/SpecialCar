package com.lc.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.trinea.android.common.view.DropDownListView;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;

import com.lc.net.GetAccountNet;
import com.lc.specialcar.R;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class BalanceDetailActivity extends Activity implements OnClickListener {
	public final static int PAGESIZE = 8;
	SimpleAdapter listItemAdapter;
    TextView tvTitle,textbg;
    ImageView ivleft;
    ImageView imgbg;
    private RelativeLayout rls;
    private DropDownListView listview;
  	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
  	GetAccountNet getAccountNet = new GetAccountNet();
 	private ProgressBar pro; 
	public int moreDataCount= 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_balancedetail);
		init();
	}

	public void init(){
		textbg = (TextView) findViewById(R.id.tvbg);
		imgbg = (ImageView) findViewById(R.id.imgbg);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("余额明细");
		pro = (ProgressBar)findViewById(R.id.progress2); 
		pro.setProgress(0);  
		pro.setIndeterminate(true);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		listview=(DropDownListView)findViewById(R.id.listview);
		 listview.setVisibility(View.GONE);
		getData();
		listItemAdapter = new SimpleAdapter(this,listItem,R.layout.userinfo_balancedetail_listitem , 
				new String[]{"MessageTitle","MessageDate","Money","MoneyType"},
				new int[]{R.id.MessageTitle,R.id.MessageDate,R.id.Money,R.id.MoneyType});
		listview.setDividerHeight(20);
		listview.setAdapter(listItemAdapter);
		listview.setOnBottomListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	 moreDataCount++;
            	 getAccountNet.setDevice(Global.DEVICE);
            	 getAccountNet.setPage(moreDataCount);
            	 getAccountNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
            	 getAccountNet.getCodeFromServer();
            }
        });
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
	
			}
			
		});
		
	}

	void getData(){
		getAccountNet.setHandler(mhandler);
		getAccountNet.setDevice(Global.DEVICE);
		getAccountNet.setPage(moreDataCount);
		getAccountNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		getAccountNet.getCodeFromServer();
	}
	
	@SuppressLint("HandlerLeak")
	public Handler mhandler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.GETACCOUNT:{
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
			if(jsonarray.length()>0){
		    listview.setVisibility(View.VISIBLE);
			for (int x = 0; x < jsonarray.length(); x++) {
				JSONObject jsonobj2 = (JSONObject) jsonarray.get(x);
				HashMap<String, Object> map = new HashMap<String, Object>();
				
				map.put("rechType", jsonobj2.getString("rechType"));
				/*
				long time  = Long.parseLong(jsonobj2.getString("time"));
				SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
				
				java.util.Date dt = new Date(time);  
				String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
				//System.out.println(sDateTime);
				 */
				map.put("MessageDate", jsonobj2.getString("time"));
				map.put("MessageTitle", jsonobj2.getString("orderNum"));
			   
				String money = jsonobj2.getString("money");
				double moneyint = Double.parseDouble(money);
				if(moneyint>0){
					 map.put("MoneyType", "充值");
					 map.put("Money", money);
				}else{
					 map.put("MoneyType", "消费");
					 moneyint = 0-moneyint;
					 map.put("Money", String.valueOf(moneyint));
				}
				listItem.add(map);
			}
			if(listItem.size()<PAGESIZE){
				listview.setHasMore(false);//禁掉下拉刷新	
			}
			listview.onBottomComplete();
			}else{
	        	 //禁掉下拉刷新
	        	 listview.setHasMore(false);
	        	 listview.onBottomComplete();
	         }
		} else {
			
		}
		if(listItem.size()==0){
 			imgbg.setVisibility(View.VISIBLE);
 			textbg.setVisibility(View.VISIBLE);
 		}
		pro.setVisibility(View.GONE); 
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
