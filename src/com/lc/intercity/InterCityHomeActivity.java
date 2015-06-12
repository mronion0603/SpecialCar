package com.lc.intercity;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class InterCityHomeActivity extends Activity implements OnClickListener {
	public static final int REQUSET = 1;
	public static final int REQUSET2 = 2;
    TextView tvTitle,tvstartaddress,tvendaddress,tvdate;
    ImageView ivSearch,ivleft;
    private RelativeLayout rls;
    private RelativeLayout endaddress,startaddress,choosedate;
    RadioGroup group;
	private View originview; 
	TimePopupWindow timepWindow;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_home);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_intercity_home, null);  
		tvstartaddress = (TextView) findViewById(R.id.tvstartactivity);
		tvendaddress = (TextView) findViewById(R.id.tvendaddress);
		tvdate = (TextView) findViewById(R.id.UseDate);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("城际约租");
		ivSearch = (ImageView) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		choosedate = (RelativeLayout) findViewById(R.id.usecardate);
		choosedate.setOnClickListener(this);
		endaddress = (RelativeLayout) findViewById(R.id.morenotify);
		endaddress.setOnClickListener(this);
		startaddress = (RelativeLayout) findViewById(R.id.moresafty);
		startaddress.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		group = (RadioGroup)this.findViewById(R.id.radioGroup);    
	}
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

    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
	            case Global.TIME_MESSAGE:{
	            	String getdate = (String)msg.obj;
	            	tvdate.setText(getdate);
	            break;
                }
            }
    }};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.usecardate:
			 timepWindow = new TimePopupWindow(InterCityHomeActivity.this,itemsOnClick);
			 timepWindow.showAsDropDown(originview, 0, 0); 
			break;
		case R.id.moresafty:  //出发地
		{	Intent intent = new Intent();
			intent.setClass(InterCityHomeActivity.this, TypeCityActivity.class);	
			startActivityForResult(intent, REQUSET);  
		}	break;
		case R.id.morenotify: //目的地
		{	Intent intent = new Intent();
		    intent.setClass(InterCityHomeActivity.this, TypeCityActivity.class);	
		    startActivityForResult(intent, REQUSET2);  
	    }
			break;
		case R.id.Search:
			  int radioButtonId = group.getCheckedRadioButtonId();
              //根据ID获取RadioButton的实例
              RadioButton rb = (RadioButton)InterCityHomeActivity.this.findViewById(radioButtonId);
              //更新文本内容，以符合选中项
              if(rb.getText().equals("拼车")){
				Intent intent = new Intent();
				intent.setClass(InterCityHomeActivity.this, SearchCarpoolActivity.class);
				startActivity(intent);
              }else{
            	Intent intent = new Intent();
  				intent.setClass(InterCityHomeActivity.this, SearchCharteredCarActivity.class);
  				startActivity(intent);
              }
			break;
			
		default:
			break;
		}
	}
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if (requestCode == InterCityHomeActivity.REQUSET && resultCode == RESULT_OK) {
        	  String address ="";
        	  Bundle extras = data.getExtras();
              if(extras != null){
            	  address = extras.getString("city");
            	  tvstartaddress.setText(address);
              }
        }  
        if (requestCode == InterCityHomeActivity.REQUSET2 && resultCode == RESULT_OK) {
        	  String address ="";
        	  Bundle extras = data.getExtras();
              if(extras != null){
            	  address = extras.getString("city");
            	  tvendaddress.setText(address);
              }
        }  
    }  	
}
