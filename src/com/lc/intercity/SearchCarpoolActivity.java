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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class SearchCarpoolActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,tvstart,tvend;
    ImageView ivleft;
    private RelativeLayout rls;
    private ListView listview;
	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
	String getstart="",getend="", getdate="", getdevice="";
	//SimpleAdapter listItemAdapter;
	CarpoolListViewAdapter listItemAdapter;
	InterCarPoolNet interCarPoolNet = new InterCarPoolNet();
	private ProgressBar pb; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_searchcarpool);
		init();
	}

	public void init(){
		pb = (ProgressBar)findViewById(R.id.progress); 
		pb.setProgress(0);  
		pb.setIndeterminate(true);
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
		listview=(ListView)findViewById(R.id.carpoollistview);
		getData();
		listItemAdapter = new CarpoolListViewAdapter(this,listItem);
	    //listItemAdapter = new SimpleAdapter(this,listItem,R.layout.activity_intercity_carpool_listitem , 
		//		new String[]{"Time","PickUpArea","GetOffArea","CurNum","TotalNum"},
		//		new int[]{R.id.Time,R.id.PickUpArea,R.id.GetOffArea,R.id.CurNum,R.id.TotalNum});
		listview.setDividerHeight(0);
		listview.setAdapter(listItemAdapter);
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				String orderNum = listItem.get(arg2).get("orderNum")+"";
				String CurNum = listItem.get(arg2).get("CurNum")+"";
				String totalNum = listItem.get(arg2).get("TotalNum")+"";
				String PickUpArea = listItem.get(arg2).get("PickUpArea")+"";
				String driverid = listItem.get(arg2).get("driverid")+"";
				Intent intent = new Intent();
				intent.setClass(getApplication(), CarpoolActivity.class);
				intent.putExtra("orderNum", orderNum);
				intent.putExtra("CurNum", CurNum);
				intent.putExtra("TotalNum", totalNum);
				intent.putExtra("PickUpArea", PickUpArea);
				intent.putExtra("driverid", driverid);
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
	}
	@Override
	public void onClick(View v) {
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
    	//System.out.println(str);
    	JSONObject jsonobj = new JSONObject(str); 
    	int result = jsonobj.getInt("ResultCode");
   	    if(result==Global.SUCCESS){
   	       addData(str,"Data");
        }else{
           Toast.makeText(SearchCarpoolActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
        } 
   	    pb.setVisibility(View.GONE); 
   	    listview.setVisibility(View.VISIBLE);
    }
    void addData(String str,String key)throws Exception{
        JSONObject jsonobj = new JSONObject(str); 
        JSONArray jsonarray = jsonobj.getJSONArray(key);
        for(int x=0;x<jsonarray.length();x++){
        	String cpbstatus = ((JSONObject)jsonarray.get(x)).getString("cpbStatus");
        	if(cpbstatus.equals("1")|cpbstatus.equals("0")){
        	 HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("CarIcon", "2");	
			 map.put("orderNum",((JSONObject)jsonarray.get(x)).getString("orderNum"));
			 map.put("driverid",((JSONObject)jsonarray.get(x)).getString("driverNum"));
			 //map.put("groupItem3",((JSONObject)jsonarray.get(x)).getString("startAddress"));
			 //map.put("groupItem4",((JSONObject)jsonarray.get(x)).getString("endAddress"));
			 map.put("Time",((JSONObject)jsonarray.get(x)).getString("startTime"));
			 map.put("PickUpArea",((JSONObject)jsonarray.get(x)).getString("accAddress"));
			 map.put("GetOffArea",((JSONObject)jsonarray.get(x)).getString("stopAddress"));
			 //map.put("groupItem5",((JSONObject)jsonarray.get(x)).getString("longitude"));
			 //map.put("groupItem6",((JSONObject)jsonarray.get(x)).getString("latitude"));
			 map.put("CurNum",((JSONObject)jsonarray.get(x)).getString("joinRider"));
			 map.put("TotalNum",((JSONObject)jsonarray.get(x)).getString("limitSit"));
			 listItem.add(map);
        	}
        }
    }
}
