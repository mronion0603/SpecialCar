package com.lc.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.lc.innercity.AddressActivity;
import com.lc.innercity.CarDemandActivity;
import com.lc.innercity.CarInfoActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.TypeAddressActivity;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class AddressManageActivity extends Activity implements OnClickListener {
	 TextView tvTitle,righttext;
	 ImageView ivleft;
	 
	 private RelativeLayout rls;
	ListView lv;
	GroupAdapter groupAdapter;
	private List<Integer> groups;
	private List<String> groups1;
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_address);
       
        init();
      
       
	}
	void init(){
		ExitApplication.getInstance().addActivity(this);
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("添加");
		righttext.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("地址");
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
        lv = (ListView) findViewById(R.id.lvGroup); 
        // 加载数据  
        groups1 = new ArrayList<String>();  
        groups1.add("光谷广场");  
        groups1.add("同济大学");  
        groups1.add("盘龙城1234号");
        groups1.add("解放大道8000号");
        
        groups = new ArrayList<Integer>();  
        groups.add(R.drawable.select_site_home);  
        groups.add(R.drawable.select_site_work);  
        groups.add(R.drawable.select_site_collect);
        groups.add(R.drawable.select_site_collect);
        groupAdapter = new GroupAdapter(this, groups1,groups);  
        lv.setAdapter(groupAdapter);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
			Intent intent3 = new Intent();
			intent3.setClass(AddressManageActivity.this,AddressActivity.class);
			startActivity(intent3);
			break;
		default:
			break;
		}
	}
}
