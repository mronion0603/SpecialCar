package com.lc.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.net.GetDiscountNet;
import com.lc.net.GetMessageNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DiscountActivity extends Activity implements OnClickListener {
	
    TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;
    private ListView listview;
  	ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
  	GetMessageNet getMessageNet = new GetMessageNet();
  	//SimpleAdapter listItemAdapter;
  	MyAdapter listItemAdapter;
  	GetDiscountNet getDiscountNet = new GetDiscountNet();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_discount);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("优惠券");

		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		listview=(ListView)findViewById(R.id.listview);
		getData();
		listItemAdapter = new MyAdapter(this,listItem);
		listview.setDividerHeight(1);
		listview.setAdapter(listItemAdapter);
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				HashMap<String,Object> temp = listItem.get(position);
				String checkstr = (String)temp.get("check");
				if(checkstr.equals("false")){
					temp.put("check", "true");
				}else{
					temp.put("check", "false");
				}
				listItem.set(position, temp);
				 int totalHeight = 0;
			        for(int i=0;i<listItemAdapter.getCount();i++) {
			          View viewItem = listItemAdapter.getView(i, null, listview);//这个很重要，那个展开的item的measureHeight比其他的大
			          viewItem.measure(0, 0);
			          totalHeight += viewItem.getMeasuredHeight();
			        }
			    ViewGroup.LayoutParams params = listview.getLayoutParams();
			    params.height = totalHeight
			            + (listview.getDividerHeight() * (listview.getCount() - 1));
			    listview.setLayoutParams(params);
			        
				listItemAdapter.notifyDataSetChanged();
			}
		});
	}
	void getData(){
		getDiscountNet.setHandler(mhandler);
		getDiscountNet.setDevice(Global.DEVICE);
		getDiscountNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		getDiscountNet.getCodeFromServer();
		/*
		for(int i=0;i<5;i++){
		     HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("vMoney", "120");
			 map.put("validityTime", "2015-5-5 10:00");
			 map.put("check", "false");
			 listItem.add(map);
		}
		*/
	}
	@SuppressLint("HandlerLeak")
	public Handler mhandler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.GETDISCOUNT:{
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
    	System.out.println(str);
		JSONObject jsonobj = new JSONObject(str);
		int result = jsonobj.getInt("ResultCode");
		if (result == Global.SUCCESS) {
			JSONArray jsonarray = jsonobj.getJSONArray("Data");
			for (int x = 0; x < jsonarray.length(); x++) {
				JSONObject jsonobj2 = (JSONObject) jsonarray.get(x);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("voucherNum", jsonobj2.getString("voucherNum"));
				map.put("vMoney", jsonobj2.getString("vMoney"));
				map.put("validityTime", jsonobj2.getString("validityTime"));
				map.put("vouStatus", jsonobj2.getString("vouStatus"));
				map.put("check", "false");
				listItem.add(map);
			}
		} else {

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
	
	private class MyAdapter extends BaseAdapter{
		    private Context context;  
		    private ArrayList<HashMap<String,Object>> myList;
		    public final class ListItemView{                //自定义控件集合      
	            public TextView txmoney;
	            public TextView txdate; 
	            public TextView txrule; 
	            public LinearLayout ll; 
	     }     
		    public MyAdapter(Context context,ArrayList<HashMap<String,Object>> mList){
		    	this.context = context;  
		    	myList = mList;
		    }
		    @Override
		    public int getCount() {
		      return myList.size();
		    }

		    @Override
		    public Object getItem(int position) {
		      return myList.get(position);
		    }

		    @Override
		    public long getItemId(int position) {
		      return position;
		    }

		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) { 
		    HashMap<String,Object> data = myList.get(position);
		    ListItemView  listItemView = null;   
		      if(convertView == null) {
		    	listItemView = new ListItemView(); 
		    	convertView=LayoutInflater.from(context).inflate(R.layout.userinfo_discount_listitem, null);  
		    	listItemView.txmoney = (TextView)convertView.findViewById(R.id.MessageTitle);
		    	listItemView.txdate = (TextView)convertView.findViewById(R.id.MessageDate);
		    	listItemView.txrule = (TextView)convertView.findViewById(R.id.rule);
		    	listItemView.ll = (LinearLayout)convertView.findViewById(R.id.llrule);
		        convertView.setTag(listItemView);
		      }else{
		    	listItemView = (ListItemView)convertView.getTag();
		      }
		      if(((String)data.get("check")).equals("true")) {
		    	  listItemView.ll.setVisibility(View.VISIBLE);
		      }else{
		    	  listItemView.ll.setVisibility(View.GONE);
		      }
		      
		      listItemView.txmoney.setText((String)data.get("vMoney"));
		      listItemView.txdate.setText((String)data.get("validityTime"));
		      //listItemView.txrule.setText((String)data.get(""));
		      return convertView;
		    }  
		  }
}
