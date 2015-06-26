package com.lc.intercity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import com.lc.net.InterCarPoolNet;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchCharteredCarActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,tvstart,tvend;
    
    ImageView ivleft;
    private RelativeLayout rls;
    //SimpleAdapter listItemAdapter;
    CharteredListViewAdapter listItemAdapter;
    private ListView listview;
	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
	String getstart="",getend="", getdate="", getdevice="";
	InterCarPoolNet interCarPoolNet = new InterCarPoolNet();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_searchcharteredcar);
		init();
		
	}

	public void init(){
		Bundle extras = getIntent().getExtras();
	    if(extras != null){
	        	getstart = extras.getString("startAddress");
	        	getend = extras.getString("endAddress");
	        	getdate = extras.getString("date");
	        	getdevice = extras.getString("device");
	        	//getauthn = extras.getString("authn");
	    }
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("查询");
		
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		
		listview=(ListView)findViewById(R.id.listview);
		
		getData();
		listItemAdapter = new CharteredListViewAdapter(this,listItem);
		//listItemAdapter = new SimpleAdapter(this,listItem,R.layout.activity_intercity_searcharcar_listitem , 
		//		new String[]{"Time","GetOffArea"},
		//		new int[]{R.id.Time,R.id.GetOffArea});
		//listItemAdapter = new CarpoolListViewAdapter(this,listItem);
		listview.setDividerHeight(0);
		listview.setAdapter(listItemAdapter);
		
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String orderNum = listItem.get(arg2).get("orderNum")+"";
				String CurNum = listItem.get(arg2).get("CurNum")+"";
				String totalNum = listItem.get(arg2).get("TotalNum")+"";
				String PickUpArea = listItem.get(arg2).get("PickUpArea")+"";
				Intent intent = new Intent();
				intent.setClass(getApplication(), CharteredCarActivity.class);
				intent.putExtra("orderNum", orderNum);
				intent.putExtra("CurNum", CurNum);
				intent.putExtra("TotalNum", totalNum);
				intent.putExtra("PickUpArea", PickUpArea);
				startActivity(intent);
			}
			
		});
		
		tvstart = (TextView) findViewById(R.id.from);
		tvend = (TextView) findViewById(R.id.to);
		tvstart.setText(getstart);
		tvend.setText(getend);
		
	}
	
	void getData(){
		interCarPoolNet.setHandler(mhandler);
		interCarPoolNet.setAuth(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		interCarPoolNet.setDate(getdate);
		interCarPoolNet.setDevice(getdevice);
		interCarPoolNet.setStartAddress(getstart);
		interCarPoolNet.setEndAddress(getend);
		interCarPoolNet.getDataFromServer();
		/*
		for(int i=0;i<5;i++){
		     HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("Time","05-09 13:30");
			 map.put("GetOffArea", "天门客运站");
			 map.put("CarIcon", "2");		
			 listItem.add(map);
		}
		*/
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

	@SuppressLint("HandlerLeak")
	public Handler mhandler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.INTERCARPOOL:{
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
    	JSONObject jsonobj = new JSONObject(str); 
    	int result = jsonobj.getInt("ResultCode");
   	    if(result==Global.SUCCESS){
   	       addData(str,"Data");
        }else{
           Toast.makeText(SearchCharteredCarActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
        } 
    }
    void addData(String str,String key)throws Exception{
        JSONObject jsonobj = new JSONObject(str); 
        JSONArray jsonarray = jsonobj.getJSONArray(key);
        for(int x=0;x<jsonarray.length();x++){
        	String limit = ((JSONObject)jsonarray.get(x)).getString("limitsit");
        	if(limit.equals("1")){
        	 HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("CarIcon", "2");	
			 map.put("orderNum",((JSONObject)jsonarray.get(x)).getString("ordernum"));
			 //map.put("groupItem2",((JSONObject)jsonarray.get(x)).getString("driverid"));
			 //map.put("groupItem3",((JSONObject)jsonarray.get(x)).getString("startAddress"));
			 //map.put("groupItem4",((JSONObject)jsonarray.get(x)).getString("endAddress"));
			 map.put("Time",((JSONObject)jsonarray.get(x)).getString("starttime"));
			 map.put("PickUpArea",((JSONObject)jsonarray.get(x)).getString("cjwaitcaraddress"));
			 map.put("GetOffArea",((JSONObject)jsonarray.get(x)).getString("cjstopaddress"));
			 //map.put("groupItem5",((JSONObject)jsonarray.get(x)).getString("longitude"));
			 //map.put("groupItem6",((JSONObject)jsonarray.get(x)).getString("latitude"));
			 map.put("CurNum",((JSONObject)jsonarray.get(x)).getString("countRider"));
			 map.put("TotalNum",((JSONObject)jsonarray.get(x)).getString("limitsit"));
			 listItem.add(map);
        	}
        }
    }
}
