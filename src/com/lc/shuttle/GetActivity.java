package com.lc.shuttle;

import com.lc.innercity.AddressActivity;
import com.lc.innercity.CarDemandActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.ModifyNameActivity;
import com.lc.popupwindow.AddressPopupWindow;
import com.lc.popupwindow.TimePopupWindow;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class GetActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
    TextView feeRule,txdate,tvname,tvphone;;
 
    Button ivSearch;
    private RelativeLayout rlusecar,rldate,rlmodifyname,rlstartaddress;
    private ImageView imAddress;
	private View originview; 
	GroupAdapter groupAdapter;
	AddressPopupWindow menuWindow;	//自定义的弹出框类
	TimePopupWindow timepWindow;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.shuttle_get);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_innercity_carinfo, null);  
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		
		rlusecar = (RelativeLayout) findViewById(R.id.usecar);
		rlusecar.setOnClickListener(this);
		
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
					 timepWindow = new TimePopupWindow(GetActivity.this);
					 timepWindow.showAsDropDown(originview, 0, 0); 		   
			     }         
			     return true; 
			}
	       
	    }); 
		txdate = (TextView) findViewById(R.id.txdate);
	
		imAddress = (ImageView) findViewById(R.id.Star);
		imAddress.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		

		case R.id.Star:
			menuWindow = new AddressPopupWindow(GetActivity.this);//实例化AddressPopupWindow
			menuWindow.showAsDropDown(originview, 0, 0); //显示窗口
			break;
		
		case R.id.Search:

			break;
		case R.id.usecar:
			Intent intent3 = new Intent();
			intent3.setClass(GetActivity.this,CarDemandActivity.class);
			startActivity(intent3);
			break;
		
		case R.id.rlmodifyname:
			Intent intent5 = new Intent();
			intent5.setClass(GetActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
			break;
		case R.id.startaddress:
			Intent intent6 = new Intent();
			intent6.setClass(GetActivity.this,AddressActivity.class);
			startActivity(intent6);
			break;
		
		default:
			break;
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
