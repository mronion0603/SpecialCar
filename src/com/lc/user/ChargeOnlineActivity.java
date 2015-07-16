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
import com.lc.pay.PayActivity;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;

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
import android.widget.EditText;
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
import android.widget.Toast;




public class ChargeOnlineActivity extends Activity implements OnClickListener {
	
    TextView feeRule,txdate;
    EditText et;
   // Button ivSearch;
    private RelativeLayout rlalipay,rldate,rlmodifyname,rlstartaddress;
    private ImageView imAddress;

	//RadioGroup group;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.userinfo_chargeonline);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		et=(EditText)findViewById(R.id.txdate2);
		rlalipay=(RelativeLayout)findViewById(R.id.rlalipay);
		rlalipay.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlalipay:
		{
			String money = et.getText().toString();
			if(money==null|money.length()<=0){
				Toast.makeText(getApplication(), "请输入充值金额", Toast.LENGTH_SHORT).show();
			}else{
			Intent intent = new Intent(ChargeOnlineActivity.this,PayActivity.class);
			intent.putExtra("type", "new");
			intent.putExtra("itemname", "充值");
			intent.putExtra("itemprice",money);
			intent.putExtra("itemcontent","春泰专车账号充值");
			intent.putExtra("serviceid","12");
			startActivity(intent);
			}
		}	break;
		
		default:
			break;
		}
	}
	
	
	 
	 
	
}
