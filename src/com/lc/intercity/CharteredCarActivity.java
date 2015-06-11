package com.lc.intercity;

import com.lc.innercity.AddressActivity;
import com.lc.innercity.CarInfoActivity;
import com.lc.innercity.ModifyNameActivity;
import com.lc.popupwindow.AddressPopupWindow;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class CharteredCarActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
	public static final int REQUSET_ADDRESS = 2;
    TextView tvTitle,tvname,tvphone,chooseaddress;
    Button ivSearch;
    ImageView ivleft,star;
    private RelativeLayout rls,modify;
    private View originview; 
    AddressPopupWindow menuWindow;    //自定义的弹出框类
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_charteredcar);
		init();
		
	}
	public void init(){
		ExitApplication.getInstance().addActivity(this);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_intercity_charteredcar, null); 
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("包车");
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		chooseaddress = (TextView) findViewById(R.id.caraddress);
		chooseaddress.setOnClickListener(this);
		modify = (RelativeLayout) findViewById(R.id.modify);
		modify.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		star = (ImageView) findViewById(R.id.star);
		star.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.star:
			//实例化AddressPopupWindow
			menuWindow = new AddressPopupWindow(CharteredCarActivity.this);
			//显示窗口
			menuWindow.showAsDropDown(originview, 0, 0); 
			break;
		case R.id.modify:
			{Intent intent5 = new Intent();
			intent5.setClass(CharteredCarActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
			}break;
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.caraddress:
			{Intent intent5 = new Intent();
			intent5.setClass(CharteredCarActivity.this,AddressActivity.class);
			startActivityForResult(intent5, REQUSET_ADDRESS);
			}break;
		case R.id.Search:
			Intent intent = new Intent();
			intent.putExtra("title", "包车");
			intent.setClass(getApplication(), SignUpActivity.class);
			startActivity(intent);
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
        if (requestCode == REQUSET_ADDRESS && resultCode == RESULT_OK) {
        	  String address ="";
        	  Bundle extras = data.getExtras();
              if(extras != null){
            	  address = extras.getString("name");
            	  chooseaddress.setText(address);
              }
        }  
    }  
}
