package com.lc.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.net.GetAccountNet;
import com.lc.specialcar.R;
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


public class BalanceDetailActivity extends Activity implements OnClickListener {
	SimpleAdapter listItemAdapter;
    TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;
    private ListView listview;
  	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
  	GetAccountNet getAccountNet = new GetAccountNet();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);

		setContentView(R.layout.userinfo_balancedetail);
		init();
		
		
	}

	public void init(){
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("余额明细");

		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		listview=(ListView)findViewById(R.id.listview);
		getData();
		listItemAdapter = new SimpleAdapter(this,listItem,R.layout.userinfo_balancedetail_listitem , 
				new String[]{"MessageTitle","MessageDate","Money"},
				new int[]{R.id.MessageTitle,R.id.MessageDate,R.id.Money});
		listview.setDividerHeight(1);
		
		listview.setAdapter(listItemAdapter);
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Intent intent = new Intent();
				//intent.setClass(getApplication(), ItineraryDetailActivity.class);
				//startActivity(intent);
			}
			
		});
	}
	void getData(){
		getAccountNet.setHandler(mhandler);
		getAccountNet.setDevice(Global.DEVICE);
		getAccountNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		getAccountNet.getCodeFromServer();
		
		for(int i=0;i<5;i++){
		     HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("MessageTitle", "13123123");
			 map.put("MessageDate", "2015-5-5 10:00");
			 map.put("Money", "¥120");
			 listItem.add(map);
		}
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
			for (int x = 0; x < jsonarray.length(); x++) {
				JSONObject jsonobj2 = (JSONObject) jsonarray.get(x);
				HashMap<String, Object> map = new HashMap<String, Object>();
				
				map.put("rechType", jsonobj2.getString("rechType"));
				map.put("MessageDate", jsonobj2.getString("time"));
				map.put("MessageTitle", jsonobj2.getString("orderNum"));
				if(jsonobj2.getString("addMoney")!=null){
					map.put("Money", jsonobj2.getString("addMoney"));
				}
				if(jsonobj2.getString("subMoney")!=null){
					map.put("Money", jsonobj2.getString("subMoney"));
				}
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
