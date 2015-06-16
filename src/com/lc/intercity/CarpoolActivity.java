package com.lc.intercity;


import org.json.JSONObject;

import com.lc.innercity.GroupAdapter;
import com.lc.innercity.ModifyNameActivity;
import com.lc.net.AddCarPoolNet;
import com.lc.popupwindow.AddressPopupWindow;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CarpoolActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
    TextView tvTitle,tvname,tvphone,tvcurtol;
    Button ivSearch;
    ImageView ivleft;
    private RelativeLayout rls,modify;
    TextView chooseaddress;
	private View originview; 
	GroupAdapter groupAdapter;
	EditText etnumber;
	//自定义的弹出框类
    AddressPopupWindow menuWindow;
    String getorderNum="",pickUpArea="";
    int curnum =0,totalnum=0;
    AddCarPoolNet addCarPoolNet = new AddCarPoolNet();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_carpool);
		init();
	}
	public void init(){
		Bundle extras = getIntent().getExtras();
	    if(extras != null){
	       getorderNum = extras.getString("orderNum");
	       curnum = Integer.parseInt(extras.getString("CurNum"));
	       totalnum = Integer.parseInt(extras.getString("TotalNum"));
	       pickUpArea = extras.getString("PickUpArea");
	    }
		ExitApplication.getInstance().addActivity(this);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_intercity_carpool, null); 
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("拼车");
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		modify = (RelativeLayout) findViewById(R.id.modify);
		modify.setOnClickListener(this);
		chooseaddress = (TextView) findViewById(R.id.address);
		etnumber = (EditText) findViewById(R.id.number);
		tvcurtol = (TextView) findViewById(R.id.arrow2);
		tvcurtol.setText(String.valueOf(curnum)+"/"+String.valueOf(totalnum));
		chooseaddress.setText(pickUpArea);
		String username = MySharePreference.getStringValue(getApplication(), MySharePreference.USERNAME);
		if(username==null){
			tvname.setText( MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
		}else{
			tvname.setText(username);
		}
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modify:
		{	Intent intent5 = new Intent();
			intent5.setClass(CarpoolActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
		}	break;
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.Search:
			int rider = Integer.parseInt(etnumber.getText().toString());
			if(rider+curnum<=totalnum){
				addCarPoolNet.setHandler(mHandler);	
				addCarPoolNet.setAuth(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
				addCarPoolNet.setDevice(Global.DEVICE);
				addCarPoolNet.setCjridersum(etnumber.getText().toString());
				addCarPoolNet.setOrderNum(getorderNum);			
				addCarPoolNet.setRiderName(tvname.getText().toString());
				addCarPoolNet.setRiderPhone(MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
				addCarPoolNet.setServiceTypeId("5");
				addCarPoolNet.getDataFromServer();
				
			}else{
				Toast.makeText(CarpoolActivity.this,"超员啦", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}

    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) { 
	            case Global.ADDCARPOOL:{
	            	try {
						parseJSON((String)msg.obj);
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}      	
	            break;
                }
            }
    }};
    
    private void parseJSON(String str)throws Exception{  
    	JSONObject jsonobj = new JSONObject(str); 
    	int result = jsonobj.getInt("ResultCode");
   	    if(result==Global.SUCCESS){
   	    	Intent intent = new Intent();
			intent.putExtra("title", "拼车");
			intent.setClass(getApplication(), SignUpActivity.class);
			startActivity(intent);
        }else{
          Toast.makeText(CarpoolActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
        } 
    }
    
	//重写的结果返回方法  
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if (requestCode == REQUSET_NAMEPHONE && resultCode == RESULT_OK) {
        	String name ="";
        	String phone ="";
        	  Bundle extras = data.getExtras();
              if(extras != null){
            	  name = extras.getString("name");
            	  phone = extras.getString("phone");
            	  tvphone.setText(phone);
            	  tvname.setText(name);
              }
        }  
    }  
}
