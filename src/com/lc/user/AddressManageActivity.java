package com.lc.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.lc.innercity.AddressActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.net.AddAddressNet;
import com.lc.net.GetAddressNet;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddressManageActivity extends Activity implements OnClickListener {
	TextView tvTitle,righttext;
	ImageView ivleft;
	public static final int REQUSET = 1;
	private RelativeLayout rls;
	ListView lv;
	GroupAdapter groupAdapter;
	private List<HashMap<String , Object>> groups1;
	AddAddressNet addaddressnet = new AddAddressNet();
	GetAddressNet getaddressnet = new GetAddressNet();
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_address);
        init();
	}
	void init(){
		ExitApplication.getInstance().addActivity(this);
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("添加");
		righttext.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("地址");
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
        lv = (ListView) findViewById(R.id.lvGroup); 
        // 加载数据  
        groups1 = new ArrayList<HashMap<String , Object>>();  
        groupAdapter = new GroupAdapter(this, groups1);  
        lv.setAdapter(groupAdapter);
        getaddressnet.setHandler(mHandler);
        getaddressnet.setDevice(Global.DEVICE);
        getaddressnet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
        getaddressnet.getCodeFromServer();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
			Intent intent3 = new Intent();
			intent3.setClass(AddressManageActivity.this,AddressActivity.class);
			startActivityForResult(intent3, REQUSET);  
			break;
		default:
			break;
		}
	}
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if (requestCode == REQUSET && resultCode == RESULT_OK) {
        	  String address ="";
        	  double lat,lont;
        	  Bundle extras = data.getExtras();
              if(extras != null){
            	  address = extras.getString("address");
            	  lat =extras.getDouble("latidute");
            	  lont =extras.getDouble("longitude");
            	  HashMap<String , Object> map = new HashMap<String , Object>();
 	 			  map.put("address",address);
            	  groups1.add(map);
            	  addaddressnet.setHandler(mHandler);
            	  addaddressnet.setDevice(Global.DEVICE);
            	  addaddressnet.setAuth(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
            	  addaddressnet.setAddress(address);
            	  addaddressnet.setLatidute(String.valueOf(lat));
            	  addaddressnet.setLongitude(String.valueOf(lont));
            	  addaddressnet.getDataFromServer();
              }   
        }  
    }  	
	
	 @SuppressLint("HandlerLeak")
		private Handler mHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch(msg.what) { 
		            case Global.ADDADDRESS:{
		            	try {
							parseJSON((String)msg.obj);
						} catch (Exception e) {	
							e.printStackTrace();
						}      	
		            break;
	                }
		            case Global.GETADDRESS:{
		            	try {
		            		parseADDRESS((String)msg.obj);
							
						} catch (Exception e) {
							
							e.printStackTrace();
						}      	
		            break;
	                }
	            }
	    }};
	    private void parseJSON(String str)throws Exception{  
	    	JSONObject jsonobj = new JSONObject(str); 
	    	int result = jsonobj.getInt("ResultCode");
	   	    if(result==Global.SUCCESS){
	   	      groupAdapter.notifyDataSetChanged();
	        }else{
	          Toast.makeText(AddressManageActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	        } 
	   }
	    private void parseADDRESS(String str)throws Exception{ 
	    	 JSONObject jsonobj = new JSONObject(str); 
	         JSONArray jsonarray = jsonobj.getJSONArray("Data");
	         for(int x=0;x<jsonarray.length();x++){
	        	 JSONObject jsonobj2 = (JSONObject)jsonarray.get(x); 
	         	 HashMap<String , Object> map = new HashMap<String , Object>();
	 			 map.put("groupItem",jsonobj2.getString("commAddressId"));
	 			 map.put("userId",jsonobj2.getString("userId"));
	 			 map.put("address",jsonobj2.getString("address"));
	 			 groups1.add(map);
	 			 groupAdapter.notifyDataSetChanged();
	         }
	    }
}
