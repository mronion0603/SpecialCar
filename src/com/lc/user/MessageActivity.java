package com.lc.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.net.GetMessageNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
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

public class MessageActivity extends Activity implements OnClickListener {
	
    TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;
    private ListView listview;
  	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
  	GetMessageNet getMessageNet = new GetMessageNet();
  	SimpleAdapter listItemAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_message);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("消息首页");

		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		listview=(ListView)findViewById(R.id.listview);
		getData();
		listItemAdapter = new SimpleAdapter(this,listItem,R.layout.userinfo_message_listitem , 
				new String[]{"MessageTitle","MessageDate"},
				new int[]{R.id.MessageTitle,R.id.MessageDate});
		listview.setDividerHeight(1);
		
		listview.setAdapter(listItemAdapter);
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
			
		});
	}
	void getData(){
		getMessageNet.setHandler(mhandler);
		getMessageNet.setDevice(Global.DEVICE);
		getMessageNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		getMessageNet.getDataFromServer();
		/*
		for(int i=0;i<5;i++){
		     HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("MessageTitle", "您已成功支付专用车费120元");
			 map.put("MessageDate", "2015-5-5 10:00");
			 listItem.add(map);
		}
		*/
	}
	@SuppressLint("HandlerLeak")
	public Handler mhandler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.GETMESSAGE:{
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
    	addData(str,"Data"); 
    }
    
    void addData(String str,String key)throws Exception{
        JSONObject jsonobj = new JSONObject(str); 
        JSONArray jsonarray = jsonobj.getJSONArray(key);
        for(int x=0;x<jsonarray.length();x++){
        	JSONObject jsonobj2 = (JSONObject)jsonarray.get(x);
        	 HashMap<String , Object> map = new HashMap<String , Object>();
        	 map.put("MessageTitle",jsonobj2.getString("content"));
			 map.put("MessageDate",jsonobj2.getString("sendTime"));
			 listItem.add(map);
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
