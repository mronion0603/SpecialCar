package com.lc.user;

import java.util.ArrayList;
import java.util.HashMap;
import com.lc.specialcar.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);

		setContentView(R.layout.userinfo_itinerary);
		init();
		
	}

	public void init(){
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("行程");

		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		listview=(ListView)findViewById(R.id.listview);
		getData();
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.userinfo_itinerary_listitem , 
				new String[]{"OrderStatus","OrderNumber","OrderDate","OrderAddress"},
				new int[]{R.id.OrderStatus,R.id.OrderNumber,R.id.OrderDate,R.id.OrderAddress});
		listview.setDividerHeight(20);
		
		listview.setAdapter(listItemAdapter);
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(getApplication(), ItineraryDetailActivity.class);
				startActivity(intent);
			}
			
		});
	}
	void getData(){
		for(int i=0;i<5;i++){
		     HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("OrderStatus","已完成");
			 map.put("OrderNumber", "20002313123");
			 map.put("OrderDate", "2015-5-5 10:00");
			 map.put("OrderAddress","汉口火车站");
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
