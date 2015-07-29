package com.lc.user;

import com.lc.popupwindow.ContactWindow;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;





public class AboutActivity extends Activity implements OnClickListener {
	public static final int DIAL_PHONE = 1;
	TextView tvTitle,tvversion;
    ImageView ivleft;
    private RelativeLayout rls;
    private RelativeLayout rlphone;
    ContactWindow contactWindow;
	private View originview; 
	String getphone="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_about);
		init();
	}

	public void init(){
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.more_about, null); 
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("关于我们");
		tvversion= (TextView) findViewById(R.id.version);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		rlphone= (RelativeLayout) findViewById(R.id.callphone);
		rlphone.setOnClickListener(this);
		tvversion.setText(getVersion());
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.callphone:
			contactWindow = new ContactWindow(AboutActivity.this,itemOnClick,"4000849488");
		    contactWindow.showAsDropDown(originview, 0, 0); 
			break;
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
	        /**
		      * 获取版本号
		      * @return 当前应用的版本号
		      */
		     public String getVersion() {
		         try {
		             PackageManager manager = this.getPackageManager();
		             PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
		             String version = info.versionName;
		            return version;
		        } catch (Exception e) {
		            e.printStackTrace();
		            return "找不到版本号";
		       }
		    }
}
