package com.lc.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.trinea.android.common.view.DropDownListView;

import com.lc.net.GetMessageNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
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
import android.widget.Toast;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;
public class MessageActivity extends Activity implements OnClickListener {
	 private String[]             mStrings            = {"Aaaaaa", "Bbbbbb", "Cccccc", "Dddddd", "Eeeeee", "Ffffff",
	            "Gggggg", "Hhhhhh", "Iiiiii", "Jjjjjj", "Kkkkkk", "Llllll", "Mmmmmm", "Nnnnnn",};
    TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;
    private DropDownListView listview;
  	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
  	GetMessageNet getMessageNet = new GetMessageNet();
  	SimpleAdapter listItemAdapter;
  	private ProgressBar pro; 
  	public int moreDataCount= 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_message);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		pro = (ProgressBar)findViewById(R.id.progress2); 
		pro.setProgress(0);  
		pro.setIndeterminate(true);
		
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("消息首页");

		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		listview=(DropDownListView)findViewById(R.id.listview);
		getData();
		listItemAdapter = new SimpleAdapter(this,listItem,R.layout.userinfo_message_listitem , 
				new String[]{"MessageTitle","MessageDate"},
				new int[]{R.id.MessageTitle,R.id.MessageDate});
		listview.setDividerHeight(1);
		
		listview.setAdapter(listItemAdapter);
		/*
		listview.setOnDropDownListener(new OnDropDownListener() {  
			  
            @Override  
            public void onDropDown() {  
                new GetDataTask(true).execute();  
            }  
        });
        */  
		listview.setOnBottomListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	 moreDataCount++;
            	 getMessageNet.setDevice(Global.DEVICE);
            	 getMessageNet.setPage(moreDataCount);
 				 getMessageNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
 				 getMessageNet.getDataFromServer();
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
		getMessageNet.setHandler(mhandler);
		getMessageNet.setPage(moreDataCount);
		getMessageNet.setDevice(Global.DEVICE);
		getMessageNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		getMessageNet.getDataFromServer();

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
    	 //System.out.println(str);
    	 JSONObject jsonobj = new JSONObject(str); 
    	 int result = jsonobj.getInt("ResultCode");
 		 if (result == Global.SUCCESS) {
	         JSONArray jsonarray = jsonobj.getJSONArray("Data");
	         if(jsonarray.length()>0){
	         for(int x=0;x<jsonarray.length();x++){
	         	JSONObject jsonobj2 = (JSONObject)jsonarray.get(x);
	         	 HashMap<String , Object> map = new HashMap<String , Object>();
	         	 map.put("MessageTitle",jsonobj2.getString("content"));
	 			 map.put("MessageDate",jsonobj2.getString("sendTime"));
	 			 listItem.add(map);
	 			 listview.onBottomComplete();
             }
	         }else{
	        	 //禁掉下拉刷新
	        	 listview.setHasMore(false);
	        	 listview.onBottomComplete();
	         }
	     }else{
        	Toast.makeText(getApplication(),jsonobj.getString("Message") , Toast.LENGTH_SHORT).show();
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
	
 
	 private class GetDataTask extends AsyncTask<Void, Void, String[]> {  
		  
	        private boolean isDropDown;  
	  
	        public GetDataTask(boolean isDropDown){  
	            this.isDropDown = isDropDown;  
	        }  
	  
	        @Override  
	        protected String[] doInBackground(Void... params) {  
	            try {  
	                Thread.sleep(1000);  
	            } catch (InterruptedException e) {  
	                ;  
	            }  
	            return mStrings;  
	        }  
	  
	        @Override  
	        protected void onPostExecute(String[] result) {  
	  
	            if (isDropDown) {  
	                //listItems.addFirst("Added after drop down");  
	                //adapter.notifyDataSetChanged();  
	  
	                // should call onDropDownComplete function of DropDownListView at end of drop down complete.  
	                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");  
	                listview.onDropDownComplete("更新于"
	                                            + dateFormat.format(new Date()));  
	            } 
	  
	            super.onPostExecute(result);  
	        }  
	    
	}  

}
