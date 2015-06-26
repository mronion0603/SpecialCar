package com.lc.innercity;


import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CarDemandActivity extends Activity implements OnClickListener {
    TextView tvTitle,righttext;
    ImageView ivleft;
    private RelativeLayout rls;
    CheckBox cb1,cb2,cb3,cb4,cb5,cb6;
    EditText et;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);    
		setContentView(R.layout.innercity_cardemand);
		init();
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("用车要求");
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("保存");
		righttext.setOnClickListener(this);
		
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		
        cb1 = (CheckBox) findViewById(R.id.checked);
        cb2 = (CheckBox) findViewById(R.id.checked2);
        cb3 = (CheckBox) findViewById(R.id.checked3);
        cb4 = (CheckBox) findViewById(R.id.checked4);
        cb5 = (CheckBox) findViewById(R.id.checked5);
        cb6 = (CheckBox) findViewById(R.id.checked6);
        et = (EditText) findViewById(R.id.feedbackET);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
		{	String demand="";
			if(cb1.isChecked()){
				demand =demand+cb1.getText().toString()+";";
			}
			if(cb2.isChecked()){
				demand =demand+cb2.getText().toString()+";";
			}
			if(cb3.isChecked()){
				demand =demand+cb3.getText().toString()+";";
			}
			if(cb4.isChecked()){
				demand =demand+cb4.getText().toString()+";";
			}
			if(cb5.isChecked()){
				demand =demand+cb5.getText().toString()+";";
			}
			if(cb6.isChecked()){
				demand =demand+cb6.getText().toString()+";";
			}
			demand =demand+et.getText().toString();
			Intent intent = new Intent();
		    intent.putExtra("demand", demand);
		    setResult(RESULT_OK, intent); 
		    finish();
		}	break;
			
		default:
			break;
		}
	}
	
	 
	   
}
