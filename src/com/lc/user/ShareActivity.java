package com.lc.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.lc.innercity.AddressActivity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class ShareActivity extends Activity implements OnClickListener {
	 TextView tvTitle,righttext;
	 ImageView ivleft;
	 
	 private RelativeLayout rls;
	 private LinearLayout llxinlang;
	
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_share);
        init();
	}
	void init(){
		ExitApplication.getInstance().addActivity(this);
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("分享到");
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		llxinlang = (LinearLayout) findViewById(R.id.xinlang);
		llxinlang.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.xinlang:
		    Intent intent=new Intent(Intent.ACTION_SEND); 
			intent.setType("text/plain"); 
			intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); 
			intent.putExtra(Intent.EXTRA_TEXT, "123");  
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(Intent.createChooser(intent, getTitle()));
			break;
		default:
			break;
		}
	}
}
