package com.lc.shuttle;



import com.lc.specialcar.R;

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
	public class ShuttleHomeActivity extends TabActivity implements OnCheckedChangeListener,OnClickListener {
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
	        setContentView(R.layout.shuttle_home);
	      
	        mainTab=(RadioGroup)findViewById(R.id.main_tab);
	        mainTab.setOnCheckedChangeListener(this);
	        tvTitle = (TextView) findViewById(R.id.topTv);
			tvTitle.setText("接送机");
			ivleft = (ImageView) findViewById(R.id.ArrowHead);
			ivleft.setVisibility(View.VISIBLE);
			rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
			rls.setOnClickListener(this);
	        initTab();
		}

		
		private void initTab(){
	    //在此进行Intent的初始化,设置Intent将传进那个Activity
		  tabhost = getTabHost();
	      iHome = new Intent(this, GetActivity.class);
	      tabhost.addTab(tabhost.newTabSpec("iHome")
	              .setIndicator("接机")
	              .setContent(iHome));
	      
	      iCatalog = new Intent(this, SendActivity.class);
	      tabhost.addTab(tabhost.newTabSpec("iCatalog")
	              .setIndicator("送机")
	              .setContent(iCatalog));
	     
	       tabhost.setCurrentTab(0);  
	       
	      // mainTab.check(R.id.radio_button0); 
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
		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
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