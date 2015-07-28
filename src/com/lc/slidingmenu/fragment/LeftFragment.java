package com.lc.slidingmenu.fragment;


import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lc.intercity.InterCityHomeActivity;
import com.lc.net.GetBalanceNet;
import com.lc.net.GetInfoNet;
import com.lc.specialcar.MainActivity;
import com.lc.specialcar.R;
import com.lc.user.AddressManageActivity;
import com.lc.user.AndroidShare;
import com.lc.user.BalanceActivity;
import com.lc.user.Card2Activity;
import com.lc.user.CardActivity;
import com.lc.user.DiscountActivity;
import com.lc.user.ItineraryActivity;
import com.lc.user.MessageActivity;
import com.lc.user.ModifyInfoActivity;
import com.lc.user.MoreActivity;
import com.lc.user.ReceiptActivity;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener{
	public static final int REQUSET = 1;
	private View userinfo;
	private View balanceView;
	private View lastListView;
	private View discussView;
	private View itineraryView;
	private View receiptView;
	private View addressView;
	private View messageView;
	private View shareView;
	private View moreView;
	private TextView card,cardconnect;
	private TextView tvname;
	private TextView tvmoney,tvbalance;
	private View view;
	GetInfoNet getInfoNet = new GetInfoNet();
	GetBalanceNet getBalanceNet = new GetBalanceNet();
	String strbalance="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		
		return view;
	}
	
	
	public void findViews(View view) {
		
		userinfo = view.findViewById(R.id.personbg);
		balanceView = view.findViewById(R.id.officeaccount);
		lastListView = view.findViewById(R.id.tvLastlist);
		discussView = view.findViewById(R.id.discount);
		itineraryView = view.findViewById(R.id.Itinerary);
		receiptView = view.findViewById(R.id.Receipt);
		addressView = view.findViewById(R.id.AddressManage);
		messageView = view.findViewById(R.id.Message);
		shareView = view.findViewById(R.id.tvShare);
		moreView = view.findViewById(R.id.tvMore);
		
		userinfo.setOnClickListener(this);
		balanceView.setOnClickListener(this);
		lastListView.setOnClickListener(this);
		discussView.setOnClickListener(this);
		itineraryView.setOnClickListener(this);
		receiptView.setOnClickListener(this);
		addressView.setOnClickListener(this);
		messageView.setOnClickListener(this);
		shareView.setOnClickListener(this);
		moreView.setOnClickListener(this);
		cardconnect = (TextView)view.findViewById(R.id.card_connect);
		card = (TextView)view.findViewById(R.id.tvCard);
		tvname = (TextView)view.findViewById(R.id.user_name);
		tvmoney = (TextView)view.findViewById(R.id.tvToday);
		tvbalance = (TextView)view.findViewById(R.id.account);
  		if(MySharePreference.getStringValue(getActivity(), MySharePreference.USER_TYPE).equals("1")){
  		   card.setText("公务卡");
  		   tvmoney.setText("对公结算");
  		   cardconnect.setVisibility(View.GONE);
  		}else{
  		   card.setText("信用卡");
  		   tvmoney.setText("余额");
  		}
  		getInfoNet.setHandler(mhandler);
		getInfoNet.setDevice(Global.DEVICE);
		getInfoNet.setAuthn(MySharePreference.getStringValue(getActivity(), MySharePreference.AUTHN));
		getInfoNet.getCodeFromServer();
		
		getBalanceNet.setHandler(mhandler);
		getBalanceNet.setDevice(Global.DEVICE);
		getBalanceNet.setAuthn(MySharePreference.getStringValue(getActivity(), MySharePreference.AUTHN));
		getBalanceNet.getCodeFromServer();
	}
	
	@SuppressLint("HandlerLeak")
	public Handler mhandler= new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.GETUSERINFO:{
	            		try {
							parseJSON((String)msg.obj);
							
						} catch (Exception e) {
							
							e.printStackTrace();
						}      	
	             break;
                }
	            case Global.GETBALANCE:{
            		try {
						parseBalance((String)msg.obj);
					} catch (Exception e) {
						e.printStackTrace();
					}      	
                 break;
                }
            }
    }};
    private void parseBalance(String str)throws Exception{  
    	//System.out.println(str);
    	JSONObject jsonobj = new JSONObject(str); 
    	int result = jsonobj.getInt("ResultCode");
   	    if(result==Global.SUCCESS){
   	    	JSONObject jsonobj2 = jsonobj.getJSONObject("Data");
   	        //String username = MySharePreference.getStringValue(getActivity(), MySharePreference.USERNAME);  
   	    	strbalance = jsonobj2.getString("balance");
   	    	tvbalance.setText("￥"+jsonobj2.getString("balance"));
        }else{
          
        } 
    }
    private void parseJSON(String str)throws Exception{  
    	//System.out.println(str);
    	JSONObject jsonobj = new JSONObject(str); 
    	int result = jsonobj.getInt("ResultCode");
   	    if(result==Global.SUCCESS){
   	    	JSONObject jsonobj2 = jsonobj.getJSONObject("Data");
   	        //for(int x=0;x<jsonarray.length();x++){
   	        	// JSONObject jsonobj2 = (JSONObject)jsonarray.get(x);
   	        	 if(jsonobj2.has("userName"))
   	        	 MySharePreference.editStringValue(getActivity(), MySharePreference.USERNAME, jsonobj2.getString("userName"));
   	        	 if(jsonobj2.has("email"))
   	        	 MySharePreference.editStringValue(getActivity(), MySharePreference.EMAIL, jsonobj2.getString("email"));
   	        	 if(jsonobj2.has("gender"))
   	        	 MySharePreference.editStringValue(getActivity(), MySharePreference.GENDER, jsonobj2.getString("gender"));
   	             //System.out.println(jsonobj2.getString("email"));
   	        //}
   	        String username = MySharePreference.getStringValue(getActivity(), MySharePreference.USERNAME);
 		    if(username==null){
 			tvname.setText("公务专车");
 		    }else{
 			tvname.setText(username);
 		    }
        }else{
          //Toast.makeText(Activity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
        } 
    }
    
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.personbg: //
		{	
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), ModifyInfoActivity.class);
			startActivityForResult(intent, REQUSET);  
		}
			break;
		case R.id.officeaccount: {	
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), BalanceActivity.class);
			intent.putExtra("account", strbalance);
			startActivity(intent);
		}break;
		case R.id.tvLastlist:// 公务卡/信用卡
			if(card.getText().toString().equals("公务卡")){
				Intent intent = new Intent();
				//intent.putExtra(name, value);
			    intent.setClass(getActivity(), CardActivity.class);
			    startActivity(intent);
			}else{
				Intent intent = new Intent();
				//intent.putExtra(name, value);
			    intent.setClass(getActivity(), Card2Activity.class);
			    startActivity(intent);
			}
			break;
		case R.id.discount: // 优惠券
		{	
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), DiscountActivity.class);
			startActivity(intent);
		}	break;
		case R.id.Itinerary: 
		{   // 行程
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), ItineraryActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.Receipt: // 发票
		{
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), ReceiptActivity.class);
			startActivity(intent);
		}	break;
		case R.id.Message: // 消息
		{
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), MessageActivity.class);
			startActivity(intent);
		}	break;
		case R.id.AddressManage: //设置
		{	
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), AddressManageActivity.class);
			startActivity(intent);
		}	break;
		case R.id.tvShare: // 分享
		{	
			AndroidShare as = new AndroidShare(view.getContext(),"很好用的专车app ---来自 春泰","");
			as.show();
		}	break;
		case R.id.tvMore: // 分享
		{	
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), MoreActivity.class);
			startActivity(intent);
		}	break;
		default:
			break;
		}
		if (newContent != null){
			switchFragment(newContent, title);
		}
	}
	
	/**
	 * 切换fragment
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchConent(fragment, title);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if (requestCode == InterCityHomeActivity.REQUSET && resultCode == Activity.RESULT_OK) {
        	  String name ="";
        	  Bundle extras = data.getExtras();
              if(extras != null){
            	  name = extras.getString("name");
            	  tvname.setText(name);
              }
        }  
       
    }  	
}
