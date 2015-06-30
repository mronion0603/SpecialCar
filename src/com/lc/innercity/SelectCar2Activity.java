package com.lc.innercity;


import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SelectCar2Activity extends Activity implements OnClickListener {
    TextView tvTitle,righttext;
    ImageView ivleft,ivselect1,ivselect2,ivselect3;
    private RelativeLayout rls;
    private LinearLayout select1;
    private LinearLayout select2;
    private LinearLayout select3;
    boolean flag1,flag2,flag3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);    
		setContentView(R.layout.innercity_select);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		flag1 = true;
		flag2 = false;
		flag3 = false;
		
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("选择车型");
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("保存");
		righttext.setOnClickListener(this);
		ivselect1 = (ImageView) findViewById(R.id.ivselect);
		ivselect2 = (ImageView) findViewById(R.id.ivselect2);
		ivselect3 = (ImageView) findViewById(R.id.ivselect3);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		select1 = (LinearLayout) findViewById(R.id.select1);
		select1.setOnClickListener(this);
		select2 = (LinearLayout) findViewById(R.id.select2);
		select2.setOnClickListener(this);
		select3 = (LinearLayout) findViewById(R.id.select3);
		select3.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
			String type="1";
            if(flag1){
			 type="1";	
			}
            if(flag2){
             type="2";	
			}
            if(flag3){
             type="3";	
   			}
			Intent intent = new Intent();
		    intent.putExtra("type", type);
		    setResult(RESULT_OK, intent); 
		    finish();
			break;
		case R.id.select1:
			if(flag1){
				
			}else{
				ivselect1.setVisibility(View.VISIBLE);
				ivselect2.setVisibility(View.GONE);
				ivselect3.setVisibility(View.GONE);
				flag1=true;
				flag2=false;
				flag3=false;
			}
			break;
		case R.id.select2:
			if(flag2){
				
			}else{
				ivselect2.setVisibility(View.VISIBLE);
				ivselect3.setVisibility(View.GONE);
				ivselect1.setVisibility(View.GONE);
				flag2=true;
				flag3=false;
				flag1=false;
			}
			break;
		case R.id.select3:
			if(flag3){
				
			}else{
				ivselect3.setVisibility(View.VISIBLE);
				ivselect1.setVisibility(View.GONE);
				ivselect2.setVisibility(View.GONE);
				flag3=true;
				flag1=false;
				flag2=false;
			}
			break;
		default:
			break;
		}
	}
	
	 
	   
}
