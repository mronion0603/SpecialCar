package com.lc.official;

import com.lc.innercity.AddressActivity;
import com.lc.innercity.BillingRuleActivity;
import com.lc.innercity.CarDemandActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.ModifyNameActivity;
import com.lc.popupwindow.AddressPopupWindow;
import com.lc.popupwindow.TimeLongPopupWindow;
import com.lc.popupwindow.TimePopupWindow;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class OfficialHomeActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
	public static final int REQUSET_ADDRESS = 2;
    TextView tvTitle,righttext,feeRule,txdate,tvname,tvphone,tvdate,tvaddress;
    ImageView ivleft;
    Button ivSearch;
    private RelativeLayout rls,rlusecar,rldate,rlmodifyname,rlstartaddress,rltimelong;
    private ImageView imAddress;
	private View originview; 
	GroupAdapter groupAdapter; 
	RadioGroup group;
	TimePopupWindow timepWindow;
	AddressPopupWindow menuWindow;
	TimeLongPopupWindow timelongpWindow;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.official_home);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvaddress = (TextView) findViewById(R.id.TvAddress);
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		tvdate = (TextView) findViewById(R.id.UseDate);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_innercity_carinfo, null);  
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("公务包车");
		righttext = (TextView) findViewById(R.id.righttext);
		//righttext.setVisibility(View.VISIBLE);
		righttext.setText("计费规则");
		righttext.setOnClickListener(this);
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		rlusecar = (RelativeLayout) findViewById(R.id.usecar);
		rlusecar.setOnClickListener(this);
		rltimelong = (RelativeLayout) findViewById(R.id.Duration);
		rltimelong.setOnClickListener(this);
		rlmodifyname = (RelativeLayout) findViewById(R.id.rlmodifyname);
		rlmodifyname.setOnClickListener(this);
		rlstartaddress= (RelativeLayout) findViewById(R.id.startaddress);
		rlstartaddress.setOnClickListener(this);
		
		rldate = (RelativeLayout) findViewById(R.id.usecardate);
		rldate.setOnClickListener(this);
		rldate.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if (event.getAction() == MotionEvent.ACTION_DOWN) { 
					 timepWindow = new TimePopupWindow(OfficialHomeActivity.this,itemsOnClick);
					 timepWindow.showAsDropDown(originview, 0, 0); 		   
			     }         
			     return true; 
			}
	    }); 
		txdate = (TextView) findViewById(R.id.txdate);
		
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		
		imAddress = (ImageView) findViewById(R.id.star);
		imAddress.setOnClickListener(this);
		group = (RadioGroup)this.findViewById(R.id.radioGroup);
        //绑定一个匿名监听器
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {          
            @Override
             public void onCheckedChanged(RadioGroup arg0, int arg1) {
                //获取变更后的选中项的ID
              
             }
        });
	}
	//为弹出窗口实现监听类
    private OnItemClickListener  itemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (arg0.getId()) {
			case R.id.lvGroup:
				String address = menuWindow.getItemStr(arg2);
				Message message = Message.obtain();  
			    message.obj = address;  
				message.what = Global.ADDRESS_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				menuWindow.dismiss();   
				break;
			default:
				break;
			}	
		}
    };
	//为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.comfirm:
				String time = timepWindow.getTime();
				Message message = Message.obtain();  
			    message.obj = time;  
				message.what = Global.TIME_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				timepWindow.dismiss();   
				break;
			case R.id.canceltime:	
				timepWindow.dismiss();   
				break;
			default:
				break;
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
		case R.id.Duration:
			timelongpWindow = new TimeLongPopupWindow(OfficialHomeActivity.this);
			timelongpWindow.showAsDropDown(originview, 0, 0); 		
			break;
		case R.id.righttext:
			Intent intent = new Intent();
			intent.setClass(OfficialHomeActivity.this,BillingRuleActivity.class);
			startActivity(intent);
			break;
		case R.id.star:
			menuWindow = new AddressPopupWindow(OfficialHomeActivity.this,itemOnClick);//实例化AddressPopupWindow
			menuWindow.showAsDropDown(originview, 0, 0); 
			break;
		
		case R.id.Search:
			  int radioButtonId = group.getCheckedRadioButtonId();
            //根据ID获取RadioButton的实例
            RadioButton rb = (RadioButton)OfficialHomeActivity.this.findViewById(radioButtonId);
            //更新文本内容，以符合选中项
            if(rb.getText().equals("个人包车")){
				Intent intent2 = new Intent();
				intent2.setClass(OfficialHomeActivity.this, SelectCarPersonActivity.class);
				startActivity(intent2);
            }else{
          	    Intent intent2 = new Intent();
				intent2.setClass(OfficialHomeActivity.this, SelectCarGroupActivity.class);
				startActivity(intent2);
            }

			break;
		case R.id.usecar:
			Intent intent3 = new Intent();
			intent3.setClass(OfficialHomeActivity.this,CarDemandActivity.class);
			startActivity(intent3);
			break;
		
		case R.id.rlmodifyname:
			Intent intent5 = new Intent();
			intent5.setClass(OfficialHomeActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
			break;
		case R.id.startaddress:
			Intent intent6 = new Intent();
			intent6.setClass(OfficialHomeActivity.this,AddressActivity.class);
			startActivityForResult(intent6, REQUSET_ADDRESS);  
			break;
		
		default:
			break;
		}
	}
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
	            case Global.TIME_MESSAGE:{
	            	String getdate = (String)msg.obj;
	            	tvdate.setText(getdate);
	            break;
                }
	            case Global.ADDRESS_MESSAGE:{
	            	String getaddress = (String)msg.obj;
	            	tvaddress.setText(getaddress);
	            break;
                }
            }
    }};
	 	
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
	        if (requestCode == REQUSET_ADDRESS && resultCode == RESULT_OK) {
	        	  String address ="";
	        	  Bundle extras = data.getExtras();
	              if(extras != null){
	            	  address = extras.getString("address");
	            	  tvaddress.setText(address);
	              }
	        }  
	    }  
}
