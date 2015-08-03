package com.lc.urgent;

import com.lc.popupwindow.ContactWindow;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class UrgentHomeActivity extends Activity implements OnClickListener {
	public static final int DIAL_PHONE = 1;
    TextView tvTitle,righttext;
    ImageView ivleft;
    Button ivSearch;
    private RelativeLayout rls,phone1,phone2,phone3;
    ContactWindow contactWindow;
	private View originview; 
	String getphone="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.urgent_home);
		init();
		
	}

	public void init(){
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.urgent_home, null); 
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("市内预约");
		righttext = (TextView) findViewById(R.id.righttext2);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("武汉");
		righttext.setOnClickListener(this);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		phone1 = (RelativeLayout) findViewById(R.id.usecardate);
		phone1.setOnClickListener(this);
		phone2 = (RelativeLayout) findViewById(R.id.usecardate2);
		phone2.setOnClickListener(this);
		phone3 = (RelativeLayout) findViewById(R.id.usecardate3);
		phone3.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext2:
			//Intent intent = new Intent();
			//intent.setClass(UrgentHomeActivity.this,BillingRuleActivity.class);
			//startActivity(intent);
			break;
		case R.id.usecardate:
		{	contactWindow = new ContactWindow(UrgentHomeActivity.this,itemOnClick,"4000849488");
		    contactWindow.showAsDropDown(originview, 0, 0); 
		}	break;
        case R.id.usecardate2:
        {	
        	contactWindow = new ContactWindow(UrgentHomeActivity.this,itemOnClick,"4000849488");
		    contactWindow.showAsDropDown(originview, 0, 0); 
        }	break;
        case R.id.usecardate3:
        {	contactWindow = new ContactWindow(UrgentHomeActivity.this,itemOnClick,"4000849488");
	        contactWindow.showAsDropDown(originview, 0, 0); 
        }	break;	
		default:
			break;
		}
	}

	//为弹出窗口实现监听类
	private OnClickListener itemOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.telphone: {
				   getphone=contactWindow.getItemStr();
				   mHandler.sendEmptyMessage(DIAL_PHONE);
				}
				break;
			case R.id.cancel: {
				  contactWindow.dismiss();
			    }
				break;
			}
		}
	};
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
            case DIAL_PHONE:{
            	//用intent启动拨打电话  
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+getphone));  
                startActivity(intent);  
                break;
                }
            }
        }};
}
