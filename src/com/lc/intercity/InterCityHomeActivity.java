package com.lc.intercity;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.lc.innercity.AddressActivity;
import com.lc.innercity.TypeAddressActivity;
import com.lc.setting.ButtonEffect;
import com.lc.specialcar.MainActivity;
import com.lc.specialcar.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class InterCityHomeActivity extends Activity implements OnClickListener {
	public static final int REQUSET = 1;
	public static final int REQUSET2 = 2;
    TextView tvTitle,tvstartaddress,tvendaddress;
    ImageView ivSearch,ivleft;
    private RelativeLayout rls;
    private RelativeLayout endaddress,startaddress;
   
    RadioGroup group;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_home);
		init();
		
	}

	public void init(){
		tvstartaddress = (TextView) findViewById(R.id.tvstartactivity);
		tvendaddress = (TextView) findViewById(R.id.tvendaddress);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("城际用车");
		ivSearch = (ImageView) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		endaddress = (RelativeLayout) findViewById(R.id.morenotify);
		endaddress.setOnClickListener(this);
		startaddress = (RelativeLayout) findViewById(R.id.moresafty);
		startaddress.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		group = (RadioGroup)this.findViewById(R.id.radioGroup);
		        //绑定一个匿名监听器
		         group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		            
		            @Override
		             public void onCheckedChanged(RadioGroup arg0, int arg1) {
		                //获取变更后的选中项的ID
		              
		             }
		        });
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
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
