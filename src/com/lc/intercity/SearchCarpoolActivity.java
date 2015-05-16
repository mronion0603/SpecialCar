package com.lc.intercity;

import java.util.ArrayList;
import java.util.HashMap;

import cn.trinea.android.common.view.DropDownListView;

import com.lc.specialcar.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;





public class SearchCarpoolActivity extends Activity implements OnClickListener {
	
    TextView tvTitle;
    
    ImageView ivleft;
    private RelativeLayout rls;
    
    private ListView listview;
	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
	//CarpoolListViewAdapter listItemAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_searchcarpool);
		init();
		
	}

	public void init(){
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("查询");
		
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		
		listview=(ListView)findViewById(R.id.listview);
		
		getData();
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.activity_intercity_carpool_listitem , 
				new String[]{"Time","PickUpArea","GetOffArea","CurNum","TotalNum"},
				new int[]{R.id.Time,R.id.PickUpArea,R.id.GetOffArea,R.id.CurNum,R.id.TotalNum});
		//listItemAdapter = new CarpoolListViewAdapter(this,listItem);
		listview.setDividerHeight(0);
		listview.setAdapter(listItemAdapter);
		
		
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
			
		});
		
	}
	
	void getData(){
		for(int i=0;i<5;i++){
		     HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("Time","05-09 13:30");
			 map.put("PickUpArea", "中南商圈");
			 map.put("GetOffArea", "天门客运站");
			 map.put("CurNum","1");
			 map.put("TotalNum", "5");
			 map.put("CarIcon", "2");		
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
