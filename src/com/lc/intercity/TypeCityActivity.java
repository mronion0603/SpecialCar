package com.lc.intercity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lc.net.GetCityNet;
import com.lc.specialcar.MainActivity;
import com.lc.specialcar.R;
import com.lc.user.Login2Activity;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class TypeCityActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,righttext;
    EditText etaddress;
    ImageView ivleft;
    GetCityNet getCityNet = new GetCityNet();
    private RelativeLayout rls;
    ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
    ListView mListView;  
    String citystr = "武汉";
    SimpleAdapter listItemAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
 
		setContentView(R.layout.intercity_citytype);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		etaddress = (EditText) findViewById(R.id.etAddress);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("输入地址");
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("保存");
		righttext.setOnClickListener(this);
		
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		
        // 周边位置列表  
        mListView = (ListView) findViewById(R.id.place_list);  
        //mLoadBar = (ProgressBar) findViewById(R.id.place_progressBar);  
        mListView.setOnItemClickListener(itemClickListener);  
        getData();
        listItemAdapter = new SimpleAdapter(this,listItem,R.layout.intercity_city_listitem , 
				new String[]{"groupItem"},
				new int[]{R.id.groupItem});
        mListView.setAdapter(listItemAdapter);  
       
	}
	
 // listView选项点击事件监听器  
    OnItemClickListener itemClickListener = new OnItemClickListener() {  
  
        @Override  
        public void onItemClick(AdapterView<?> parent, View view, int position,  
                long id) {  
            // TODO Auto-generated method stub  
            
            Intent intent = new Intent();
            intent.setClass(TypeCityActivity.this, InterCityHomeActivity.class);
            String address =  listItem.get(position).get("groupItem")+"";
            intent.putExtra("city", address);
            setResult(RESULT_OK, intent); 
            finish();
        }  
  
    };  
	
    void getData(){
    	getCityNet.setHandler(mhandler);
    	getCityNet.getDataFromServer();
	}
       
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
			 Intent intent = new Intent();
	         intent.setClass(TypeCityActivity.this, InterCityHomeActivity.class);
	         String address =  etaddress.getText().toString();
	         intent.putExtra("city", address);
	         setResult(RESULT_OK, intent); 
	         finish();
			break;
		
			
		default:
			break;
		}
	}
	
	public Handler mhandler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.CITY:{
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
    	addData(str,"A");
    	addData(str,"B");
    	addData(str,"C");
    	addData(str,"D");
    	addData(str,"E");
    	addData(str,"F");
    	addData(str,"G");
    	addData(str,"H");
    	addData(str,"I");
    	addData(str,"J");
    	addData(str,"K");
    	addData(str,"L");
    	addData(str,"M");
    	addData(str,"N");
    	addData(str,"O");
    	addData(str,"P");
    	addData(str,"Q");
    	addData(str,"R");
    	addData(str,"S");
    	addData(str,"T");
    	addData(str,"U");
    	addData(str,"V");
    	addData(str,"W");
    	addData(str,"X");
    	addData(str,"Y");
    	addData(str,"Z");
    	/*
        int result = jsonobj.getInt("ResultCode");
   	    if(result==Global.SUCCESS){
   		   //String getauthn = jsonobj.getJSONObject("Data").getString("authn");
   		   Toast.makeText(TypeCityActivity.this, str, Toast.LENGTH_LONG).show();
        }else{
           Toast.makeText(TypeCityActivity.this,str, Toast.LENGTH_LONG).show();
        } 
        */
    }
    
    void addData(String str,String key)throws Exception{
        JSONObject jsonobj = new JSONObject(str); 
        JSONArray jsonarray = jsonobj.getJSONArray(key);
        for(int x=0;x<jsonarray.length();x++){
        	 HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("groupItem",jsonarray.get(x));
			 listItem.add(map);
        }
    }
}
