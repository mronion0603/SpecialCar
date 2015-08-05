package com.lc.intercity;

import org.json.JSONObject;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.ModifyNameActivity;
import com.lc.net.AddCarPoolNet;
import com.lc.popupwindow.AddressPopupWindow;
import com.lc.progressbutton.CircularProgressButton;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CarpoolActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
	public static final int SIGNUP =2;
    TextView tvTitle,tvname,tvphone,tvcurtol;
    CircularProgressButton ivSearch;
    ImageView ivleft;
    private RelativeLayout rls,modify;
    TextView chooseaddress;
	GroupAdapter groupAdapter;
	EditText etnumber;
	//自定义的弹出框类
    AddressPopupWindow menuWindow;
    String getorderNum="",pickUpArea="";
    int curnum =0,totalnum=0;
    AddCarPoolNet addCarPoolNet = new AddCarPoolNet();
   // NotifyDriverNet notifyDriverNet = new NotifyDriverNet();
    String getdriverid="";
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
	       getdriverid= extras.getString("driverid");
	    }
		ExitApplication.getInstance().addActivity(this);
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("拼车");
		ivSearch = (CircularProgressButton) findViewById(R.id.Search);
		ivSearch.setIndeterminateProgressMode(true);
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
			if(etnumber.getText().toString().length()>0){
				int rider = Integer.parseInt(etnumber.getText().toString());
				if(rider<=0){
					Toast.makeText(CarpoolActivity.this,"乘车人数必须至少为1人", Toast.LENGTH_LONG).show();
				}else{
					if(rider+curnum<=totalnum){
						ivSearch.setClickable(false);
						ivSearch.setProgress(50);
						addCarPoolNet.setHandler(mHandler);	
						addCarPoolNet.setAuth(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
						addCarPoolNet.setDevice(Global.DEVICE);
						addCarPoolNet.setCjridersum(rider+"");
						addCarPoolNet.setOrderNum(getorderNum);			
						addCarPoolNet.setRiderName(tvname.getText().toString());
						if(tvphone.getText().toString().equals("本人")){
						    addCarPoolNet.setRiderPhone(MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
						}else{
							addCarPoolNet.setRiderPhone(tvphone.getText().toString());
						}
						addCarPoolNet.setServiceTypeId("5");
						addCarPoolNet.setCpbStauts("1");
						addCarPoolNet.getDataFromServer();			
					}else{
						Toast.makeText(CarpoolActivity.this,"超员啦", Toast.LENGTH_LONG).show();
					}
				}
			}else{
				Toast.makeText(CarpoolActivity.this,"请输入乘车人数", Toast.LENGTH_LONG).show();
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
   	    	
   	    	//notifyDriverNet.setHandler(mHandler);
   	    	//notifyDriverNet.setOrderNum(getorderNum);
   	    	//notifyDriverNet.getDataFromServer();
   	    	
   	    	Intent intent = new Intent();
			intent.putExtra("title", "拼车");
			intent.setClass(getApplication(), SignUpActivity.class);
			startActivityForResult(intent,SIGNUP);
			
        }else{
        	ivSearch.setClickable(true);
        	ivSearch.setProgress(0);
          Toast.makeText(CarpoolActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
        } 
    }
    
    private void parseNotify(String str)throws Exception{  
    	JSONObject jsonobj = new JSONObject(str); 
    	int result = jsonobj.getInt("ResultCode");
   	    if(result==Global.SUCCESS){
   	    	
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
        if (requestCode == SIGNUP && resultCode == RESULT_OK) {
        	   Intent intent = new Intent();
	           setResult(RESULT_OK, intent); 
        	   finish();
             
        } 
    }  
}
