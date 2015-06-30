package com.lc.innercity;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lc.specialcar.R;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class TypeAddressActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,righttext;
    EditText etaddress;
    ImageView ivleft;
   // Button ivSearch;
    private RelativeLayout rls;
 
    
    // 地理编码  
    GeoCoder mGeoCoder = null;
    List<PoiInfo> mInfoList;  
    PoiInfo mCurentInfo;  
    
 // 位置列表  
    PlaceListAdapter mAdapter; 
    ListView mListView;  
    ImageView mSelectImg;
    String citystr = "武汉";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		 //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.innercity_addresstype);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		etaddress = (EditText) findViewById(R.id.etAddress);
		etaddress.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				GeoCodeOption gco= new GeoCodeOption();
				//gco.
				gco.city(citystr);
				gco.address(etaddress.getText().toString()) ;
				mGeoCoder.geocode(gco);
			}});
		
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("输入地址");
		righttext = (TextView) findViewById(R.id.righttext);
		//righttext.setVisibility(View.VISIBLE);
		righttext.setText("计费规则");
		righttext.setOnClickListener(this);
		
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		// 初始化POI信息列表  
        mInfoList = new ArrayList<PoiInfo>();  
       
        // 地理编码  
        mGeoCoder = GeoCoder.newInstance();  
        mGeoCoder.setOnGetGeoCodeResultListener(GeoListener); 
        // 周边位置列表  
        mListView = (ListView) findViewById(R.id.place_list);  
        //mLoadBar = (ProgressBar) findViewById(R.id.place_progressBar);  
        mListView.setOnItemClickListener(itemClickListener);  
        mAdapter = new PlaceListAdapter(getLayoutInflater(), mInfoList);  
        setListViewHeightBasedOnChildren(mListView);
        mListView.setAdapter(mAdapter);  
       /// mListView.setVisibility(View.GONE);
        mSelectImg = new ImageView(this);  
	}
	/**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) { 
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) { 
            // pre-condition 
            return; 
        } 

        int totalHeight = 0; 
        for (int i = 0; i < listAdapter.getCount(); i++) { 
            View listItem = listAdapter.getView(i, null, listView); 
            listItem.measure(0, 0); 
            totalHeight += listItem.getMeasuredHeight(); 
        } 

        ViewGroup.LayoutParams params = listView.getLayoutParams(); 
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
        listView.setLayoutParams(params); 
    }
 // listView选项点击事件监听器  
    OnItemClickListener itemClickListener = new OnItemClickListener() {  
  
        @Override  
        public void onItemClick(AdapterView<?> parent, View view, int position,  
                long id) {  
            // TODO Auto-generated method stub  
            
            Intent intent = new Intent();
            intent.setClass(TypeAddressActivity.this, AddressActivity.class);
            String address =  mInfoList.get(position).name;
            LatLng ll = mInfoList.get(position).location;
            //mInfoList.get(position).name
            double latitude = ll.latitude;
            double longitude = ll.longitude;
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("address", address);
            setResult(RESULT_OK, intent); 
            finish();
        }  
  
    };  
	// 地理编码监听器  
    OnGetGeoCoderResultListener GeoListener = new OnGetGeoCoderResultListener() {  
        public void onGetGeoCodeResult(GeoCodeResult result) {  
        	 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
                 // 没有找到检索结果 
             	System.out.println("无结果");
             }  
             // 获取地理编码结果  
             else {  
             	
                 // 当前位置信息  
                 mCurentInfo = new PoiInfo();  
                 mCurentInfo.address = result.getAddress();  
                 mCurentInfo.location = result.getLocation();  
                 mCurentInfo.name = "[位置]";  
                 mInfoList.clear();  
                 mInfoList.add(mCurentInfo);  
   
                 // 将周边信息加入表  
                
                 // 通知适配数据已改变  
                 mAdapter.notifyDataSetChanged();  
                 
                 mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())  
                         .location(mCurentInfo.location));  
                 
                 //mLoadBar.setVisibility(View.GONE);  
                 //if(result.getPoiList() != null){
                    //curaddress.setText(mInfoList.get(0).address);
               //  }else{
                    //curaddress.setText("获取地址失败");
                // }
             }  
         
        }  
  
        @Override  
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {  
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
                // 没有找到检索结果 
            	//System.out.println("无结果");
            }  
            // 获取反向地理编码结果  
            else {  
            	
                // 当前位置信息  
                mCurentInfo = new PoiInfo();  
                mCurentInfo.address = result.getAddress();  
                mCurentInfo.location = result.getLocation();  
                mCurentInfo.name = "[位置]";  
                mInfoList.clear();  
                mInfoList.add(mCurentInfo);  
  
                // 将周边信息加入表  
                if (result.getPoiList() != null) {  
                    mInfoList.addAll(result.getPoiList());  
                }  
                // 通知适配数据已改变  
                mAdapter.notifyDataSetChanged();  
                //mLoadBar.setVisibility(View.GONE);  
                if(result.getPoiList() != null){
                   //curaddress.setText(mInfoList.get(0).address);
                }else{
                   //curaddress.setText("获取地址失败");
                }
            }  
        }  
    };  
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
			Intent intent = new Intent();
			intent.setClass(TypeAddressActivity.this,BillingRuleActivity.class);
			startActivity(intent);
			break;
		
			
		default:
			break;
		}
	}
	
	   
}
