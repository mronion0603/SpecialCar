package com.lc.user;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.lc.innercity.AddressActivity;
import com.lc.innercity.BillingRuleActivity;
import com.lc.innercity.CarDemandActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.ModifyNameActivity;
import com.lc.official.SelectCarGroupActivity;
import com.lc.official.SelectCarPersonActivity;
import com.lc.shuttle.GetActivity;
import com.lc.shuttle.SendActivity;
import com.lc.specialcar.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TimePicker;








import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.app.TabActivity;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

	@SuppressWarnings("deprecation")
	public class ChargeActivity extends TabActivity implements OnCheckedChangeListener,OnClickListener {
		private RadioGroup mainTab;
		private TabHost tabhost;
		private Intent iHome;
		private Intent iCatalog;
		 ImageView ivleft;
		 TextView tvTitle;
		 private RelativeLayout rls;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.userinfo_charge);
	        ExitApplication.getInstance().addActivity(this);
	        mainTab=(RadioGroup)findViewById(R.id.main_tab);
	        mainTab.setOnCheckedChangeListener(this);
	        tvTitle = (TextView) findViewById(R.id.topTv);
			tvTitle.setText("充值");
			ivleft = (ImageView) findViewById(R.id.ArrowHead);
			ivleft.setVisibility(View.VISIBLE);
			rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
			rls.setOnClickListener(this);
	        initTab();
		}

		
		private void initTab(){
	    //在此进行Intent的初始化,设置Intent将传进那个Activity
		  tabhost = getTabHost();
	      iHome = new Intent(this, ChargeOnlineActivity.class);
	      tabhost.addTab(tabhost.newTabSpec("iHome")
	              .setIndicator("在线充值")
	              .setContent(iHome));
	      
	      iCatalog = new Intent(this, ChargeCardActivity.class);
	      tabhost.addTab(tabhost.newTabSpec("iCatalog")
	              .setIndicator("充值卡充值")
	              .setContent(iCatalog));
	     
	       tabhost.setCurrentTab(0);  
	       
	      // mainTab.check(R.id.radio_button0); 
		}
		

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.rlslidemenu:
				Intent intent = new Intent();
	    	    setResult(RESULT_OK, intent); 
				finish();
				break;
			default:
				break;
			}
		}
		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			 switch(arg1){
		        case R.id.radio_button0:
		            //Toast.makeText(MainActivity.this,"button0",Toast.LENGTH_SHORT).show();
		            tabhost.setCurrentTabByTag("iHome");
		            break;
		        case R.id.radio_button1:
		            
		            tabhost.setCurrentTabByTag("iCatalog");
		            break;
  
		    }
		}

		
	}