package com.lc.intercity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lc.innercity.PlaceListAdapter;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class TypeCityActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,righttext;
    EditText etaddress;
    ImageView ivleft;

    private RelativeLayout rls;
    ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
    ListView mListView;  
    String citystr = "武汉";
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
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.intercity_city_listitem , 
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
		
		     HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("groupItem", "武汉市");
			 HashMap<String , Object> map2 = new HashMap<String , Object>();
			 map2.put("groupItem", "黄冈市");
			 HashMap<String , Object> map3 = new HashMap<String , Object>();
			 map3.put("groupItem", "天门市");
			 HashMap<String , Object> map4 = new HashMap<String , Object>();
			 map4.put("groupItem", "荆州市");
			 HashMap<String , Object> map5 = new HashMap<String , Object>();
			 map5.put("groupItem", "宜昌市");
			 listItem.add(map);
			 listItem.add(map2);
			 listItem.add(map3);
			 listItem.add(map4);
			 listItem.add(map5);
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
	

	   
}
