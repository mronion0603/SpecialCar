package com.lc.innercity;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog; 
import android.content.DialogInterface; 
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TimePicker;




public class CarInfoActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
    TextView tvTitle,righttext,feeRule,txdate,tvname,tvphone;
    ImageView ivleft;
    Button ivSearch;
    private RelativeLayout rls,rlusecar,rlselectcar,rldate,rlmodifyname,rlstartaddress,rlgetoffaddress;
    private ImageView imAddress,getoffAddress;
	private PopupWindow contactWindow,timeWindow; 
	private View view,timeview; 
	private View originview; 
	GroupAdapter groupAdapter;
	private List<Integer> groups;
	private List<String> groups1;
	private ListView lv_group1; 
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_innercity_carinfo);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_innercity_carinfo, null);  
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("市内预约");
		righttext = (TextView) findViewById(R.id.righttext);
		righttext.setVisibility(View.VISIBLE);
		righttext.setText("计费规则");
		righttext.setOnClickListener(this);
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		rlusecar = (RelativeLayout) findViewById(R.id.usecar);
		rlusecar.setOnClickListener(this);
		rlselectcar = (RelativeLayout) findViewById(R.id.selectcar);
		rlselectcar.setOnClickListener(this);
		rlmodifyname = (RelativeLayout) findViewById(R.id.rlmodifyname);
		rlmodifyname.setOnClickListener(this);
		rlstartaddress= (RelativeLayout) findViewById(R.id.startaddress);
		rlstartaddress.setOnClickListener(this);
		rlgetoffaddress= (RelativeLayout) findViewById(R.id.getoffaddress);
		rlgetoffaddress.setOnClickListener(this);
		rldate = (RelativeLayout) findViewById(R.id.usecardate);
		rldate.setOnClickListener(this);
		rldate.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if (event.getAction() == MotionEvent.ACTION_DOWN) { 
					    selectTimeWindow(originview);			   
			     }         
			     return true; 
			}
	       
	    }); 
		txdate = (TextView) findViewById(R.id.txdate);
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		feeRule=(TextView)findViewById(R.id.FeeRule);
		String ruleStr1 = "13";
		String ruleStr2 = "元+";
		String ruleStr3 = "1.5";
		String ruleStr4 = "元/公里+";
		String ruleStr5 = "0.24";
		String ruleStr6 = "元/分钟";
		
		String newMessageInfo = "<font color='black'><b>" + ruleStr1+ "</b></font>"
		                       +"<font color='gray'><b>" + ruleStr2+ "</b></font>"
		                       +"<font color='black'><b>" + ruleStr3+ "</b></font>"
		                       +"<font color='gray'><b>" + ruleStr4+ "</b></font>"
		                       +"<font color='black'><b>" + ruleStr5+ "</b></font>"
		                       +"<font color='gray'><b>" + ruleStr6+ "</b></font>";
		feeRule.setText(Html.fromHtml(newMessageInfo));
        
		imAddress = (ImageView) findViewById(R.id.star);
		imAddress.setOnClickListener(this);
		getoffAddress = (ImageView) findViewById(R.id.star3);
		getoffAddress.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
			Intent intent = new Intent();
			intent.setClass(CarInfoActivity.this,BillingRuleActivity.class);
			startActivity(intent);
			break;
		case R.id.star:
			selectAddressWindow(originview);
			break;
		case R.id.star3:
			selectAddressWindow(originview);
			break;
		case R.id.Search:
			Intent intent2 = new Intent();
			intent2.setClass(CarInfoActivity.this,SendDealActivity.class);
			startActivity(intent2);
			break;
		case R.id.usecar:
			Intent intent3 = new Intent();
			intent3.setClass(CarInfoActivity.this,CarDemandActivity.class);
			startActivity(intent3);
			break;
		case R.id.selectcar:
			Intent intent4 = new Intent();
			intent4.setClass(CarInfoActivity.this,SelectCarActivity.class);
			startActivity(intent4);
			break;
		case R.id.rlmodifyname:
			Intent intent5 = new Intent();
			intent5.setClass(CarInfoActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
			break;
		case R.id.startaddress:
			Intent intent6 = new Intent();
			intent6.setClass(CarInfoActivity.this,AddressActivity.class);
			startActivity(intent6);
			break;
		case R.id.getoffaddress:
			Intent intent7 = new Intent();
			intent7.setClass(CarInfoActivity.this,AddressActivity.class);
			startActivity(intent7);
			break;
		default:
			break;
		}
	}
	
	 public void selectAddressWindow(View parent) {  
		  
	        if (contactWindow == null) {  
	            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	  
	            view = layoutInflater.inflate(R.layout.innercity_address, null);  
	  
	            lv_group1 = (ListView) view.findViewById(R.id.lvGroup);  
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
	            lv_group1.setAdapter(groupAdapter);  
	            // 创建一个PopuWidow对象  
	            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
	            int width = windowManager.getDefaultDisplay().getWidth() ;
	            int height = windowManager.getDefaultDisplay().getHeight();
	            contactWindow = new PopupWindow(view, width, height);  
	        }  
	        //contactWindow.setAnimationStyle(R.style.mystyle);//设置动画样式
	        // 使其聚集  
	        contactWindow.setFocusable(true);  
	        // 设置允许在外点击消失  
	        contactWindow.setOutsideTouchable(true);  
	  
	        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
	        contactWindow.setBackgroundDrawable(new BitmapDrawable());  
	        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
	        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半  
	        //int xPos = windowManager.getDefaultDisplay().getWidth() / 2  
	        //        - contactWindow.getWidth() / 2;  

	        contactWindow.showAsDropDown(parent, 0, 0);  
	       
	        lv_group1.setOnItemClickListener(new OnItemClickListener() {  	  
	            @Override  
	            public void onItemClick(AdapterView<?> adapterView, View view,  
	                    int position, long id) {  
	                    
	                if (contactWindow != null) {  
	                	contactWindow.dismiss();  
	                }  
	            }  
	        });  
	    }  
	 
	 public void selectTimeWindow(View parent) {  
		  
	        if (timeWindow == null) {  
	            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	  
	            timeview = layoutInflater.inflate(R.layout.time2_layout, null);  
	    		final WheelView hours = (WheelView) timeview.findViewById(R.id.hour);
	    		NumericWheelAdapter hourAdapter = new NumericWheelAdapter(this, 0, 23);
	    		hourAdapter.setItemResource(R.layout.wheel_text_item);
	    		hourAdapter.setItemTextResource(R.id.text);
	    		hours.setViewAdapter(hourAdapter);

	    		final WheelView mins = (WheelView) timeview.findViewById(R.id.mins);
	    		NumericWheelAdapter minAdapter = new NumericWheelAdapter(this, 0, 59, "%02d");
	    		minAdapter.setItemResource(R.layout.wheel_text_item);
	    		minAdapter.setItemTextResource(R.id.text);
	    		mins.setViewAdapter(minAdapter);
	    		mins.setCyclic(true);

	    		// set current time
	    		Calendar calendar = Calendar.getInstance(Locale.US);
	    		hours.setCurrentItem(calendar.get(Calendar.HOUR));
	    		mins.setCurrentItem(calendar.get(Calendar.MINUTE));
	    		//ampm.setCurrentItem(calendar.get(Calendar.AM_PM));

	    		final WheelView day = (WheelView) timeview.findViewById(R.id.day);
	    		day.setViewAdapter(new DayArrayAdapter(this, calendar));
	            
	    		Button confirm  = (Button) timeview.findViewById(R.id.comfirm);
	    		confirm.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if (timeWindow != null) {  
							timeWindow.dismiss();  
		                }  
					}	
	    		});
	    		Button cancel  = (Button) timeview.findViewById(R.id.canceltime);
	    		cancel.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if (timeWindow != null) {  
							timeWindow.dismiss();  
		                }  
					}	
	    		});
	    		
	            // 创建一个PopuWidow对象  
	            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
	            int width = windowManager.getDefaultDisplay().getWidth() ;
	            int height = windowManager.getDefaultDisplay().getHeight();
	            timeWindow = new PopupWindow(timeview, width, height);  
	        }  
	  
	        // 使其聚集  
	        timeWindow.setFocusable(true);  
	        // 设置允许在外点击消失  
	        timeWindow.setOutsideTouchable(true);  
	  
	        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
	        timeWindow.setBackgroundDrawable(new BitmapDrawable());  
	        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
	        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半  
	        //int xPos = windowManager.getDefaultDisplay().getWidth() / 2  
	        //        - contactWindow.getWidth() / 2;  

	        timeWindow.showAsDropDown(parent, 0, 0);  
	       
	        
	    }  
	 
	 /**
		 * Day adapter
		 *
		 */
		private class DayArrayAdapter extends AbstractWheelTextAdapter {
			// Count of days to be shown
			private final int daysCount = 7;

			// Calendar
			Calendar calendar;

			/**
			 * Constructor
			 */
			protected DayArrayAdapter(Context context, Calendar calendar) {
				super(context, R.layout.time2_day, NO_RESOURCE);
				this.calendar = calendar;

				setItemTextResource(R.id.time2_monthday);
			}

			@Override
			public View getItem(int index, View cachedView, ViewGroup parent) {
				int day =  index;
				Calendar newCalendar = (Calendar) calendar.clone();
				newCalendar.roll(Calendar.DAY_OF_YEAR, day);

				View view = super.getItem(index, cachedView, parent);
				TextView weekday = (TextView) view.findViewById(R.id.time2_weekday);
				if (day == 0) {
					weekday.setText("");
				} else {
					DateFormat format = new SimpleDateFormat("EEE");
					weekday.setText(format.format(newCalendar.getTime()));
				}

				TextView monthday = (TextView) view.findViewById(R.id.time2_monthday);
				if (day == 0) {
					monthday.setText("今天");
					monthday.setTextColor(0xFF29b43d);
					//monthday.setTextColor(0xFF0000F0);
				} else {
					DateFormat format = new SimpleDateFormat("MMM d");
					monthday.setText(format.format(newCalendar.getTime()));
					monthday.setTextColor(0xFF111111);
				}

				return view;
			}

			@Override
			public int getItemsCount() {
				return daysCount + 1;
			}

			@Override
			protected CharSequence getItemText(int index) {
				return "";
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
	    }  
}
