package com.lc.intercity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.net.GetCityNet;
import com.lc.sortlistview.CharacterParser;
import com.lc.sortlistview.ClearEditText;
import com.lc.sortlistview.PinyinComparator;
import com.lc.sortlistview.SideBar;
import com.lc.sortlistview.SortAdapter;
import com.lc.sortlistview.SortModel;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.lc.sortlistview.SideBar.OnTouchingLetterChangedListener;

public class TypeCityActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,righttext;
    EditText etaddress;
    ImageView ivleft;
    GetCityNet getCityNet = new GetCityNet();
    private RelativeLayout rls;
    ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
    //ListView mListView;  
    String citystr = "武汉";
    SimpleAdapter listItemAdapter;
   // ProgressDialog proDialog;
    
    private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	
	//String [] datas;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
 
		setContentView(R.layout.intercity_citytype);
		init();
		//initViews();
	}
	private void initViews() {
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) findViewById(R.id.place_list);
		sortListView.setOnItemClickListener(itemClickListener);
		/*sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
			}
		});
		*/
		//System.out.println("size:"+listItem.size()+"");
		SourceDateList = filledData(listItem);
		
		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		etaddress = (EditText) findViewById(R.id.etAddress);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("输入地址");
		righttext = (TextView) findViewById(R.id.righttext);
		//righttext.setVisibility(View.VISIBLE);
		righttext.setText("保存");
		righttext.setOnClickListener(this);
		
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		getData();
        // 周边位置列表  
        //mListView = (ListView) findViewById(R.id.place_list);  
        // mListView.setOnItemClickListener(itemClickListener);  
       
        //listItemAdapter = new SimpleAdapter(this,listItem,R.layout.intercity_city_listitem , 
		//		new String[]{"groupItem"},
		//		new int[]{R.id.groupItem});
        //mListView.setAdapter(listItemAdapter);  
	}
	
 // listView选项点击事件监听器  
    OnItemClickListener itemClickListener = new OnItemClickListener() {  
  
        @Override  
        public void onItemClick(AdapterView<?> parent, View view, int position,  
                long id) {  
            // TODO Auto-generated method stub  
        	SortModel sm = (SortModel)adapter.getItem(position);
            Intent intent = new Intent();
            intent.setClass(TypeCityActivity.this, InterCityHomeActivity.class);
            String address = sm.getName()+"";
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
	
	@SuppressLint("HandlerLeak")
	public Handler mhandler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.CITY:{
	            		try {
							parseJSON((String)msg.obj);
							//listItemAdapter.notifyDataSetChanged();
							initViews();
							//System.out.println("success");
							adapter.notifyDataSetChanged();
						} catch (Exception e) {
							
							e.printStackTrace();
						}      	
	             break;
                }
            }
    }};
    private void parseJSON(String str)throws Exception{  
    	//JSONObject jsonobj = new JSONObject(str); 
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
        if(jsonobj.has(key)){
        JSONArray jsonarray = jsonobj.getJSONArray(key);
        for(int x=0;x<jsonarray.length();x++){
        	 HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("groupItem",jsonarray.get(x));
			 listItem.add(map);
        }
        }
    }
    
    /**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData( ArrayList<HashMap<String,Object>>  date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.size(); i++){
			SortModel sortModel = new SortModel();
			sortModel.setName((String)date.get(i).get("groupItem"));
			//汉字转换成拼音
			String pinyin = characterParser.getSelling((String)date.get(i).get("groupItem"));
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
}
