package com.lc.official;


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
import com.lc.innercity.SelectCarActivity;
import com.lc.innercity.SendDealActivity;
import com.lc.intercity.InterCityHomeActivity;
import com.lc.intercity.SearchCarpoolActivity;
import com.lc.intercity.SearchCharteredCarActivity;
import com.lc.setting.ButtonEffect;
import com.lc.specialcar.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog; 
import android.content.DialogInterface; 
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TimePicker;




public class OfficialHomeActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,righttext,feeRule,txdate;
    ImageView ivleft;
    Button ivSearch;
    private RelativeLayout rls,rlusecar,rldate,rlmodifyname,rlstartaddress,rltimelong;
    private ImageView imAddress;
	private PopupWindow contactWindow,timeWindow,timeWindow2; 
	private View view,timeview,timeview2; 
	private View originview; 
	GroupAdapter groupAdapter;
	private List<Integer> groups;
	private List<String> groups1;
	private ListView lv_group1; 
	RadioGroup group;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.official_home);
		init();
		
	}

	public void init(){
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_innercity_carinfo, null);  
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("公务包车");
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
		rltimelong = (RelativeLayout) findViewById(R.id.Duration);
		rltimelong.setOnClickListener(this);
		rlmodifyname = (RelativeLayout) findViewById(R.id.rlmodifyname);
		rlmodifyname.setOnClickListener(this);
		rlstartaddress= (RelativeLayout) findViewById(R.id.startaddress);
		rlstartaddress.setOnClickListener(this);
		
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
		
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		
        
		imAddress = (ImageView) findViewById(R.id.star);
		imAddress.setOnClickListener(this);
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
		case R.id.Duration:
			selectTimeLongWindow(originview);
			break;
		case R.id.righttext:
			Intent intent = new Intent();
			intent.setClass(OfficialHomeActivity.this,BillingRuleActivity.class);
			startActivity(intent);
			break;
		case R.id.star:
			selectAddressWindow(originview);
			break;
		
		case R.id.Search:
			  int radioButtonId = group.getCheckedRadioButtonId();
            //根据ID获取RadioButton的实例
            RadioButton rb = (RadioButton)OfficialHomeActivity.this.findViewById(radioButtonId);
            //更新文本内容，以符合选中项
            if(rb.getText().equals("个人包车")){
				Intent intent2 = new Intent();
				intent2.setClass(OfficialHomeActivity.this, SelectCarPersonActivity.class);
				startActivity(intent2);
            }else{
          	    Intent intent2 = new Intent();
				intent2.setClass(OfficialHomeActivity.this, SelectCarGroupActivity.class);
				startActivity(intent2);
            }

			break;
		case R.id.usecar:
			Intent intent3 = new Intent();
			intent3.setClass(OfficialHomeActivity.this,CarDemandActivity.class);
			startActivity(intent3);
			break;
		
		case R.id.rlmodifyname:
			Intent intent5 = new Intent();
			intent5.setClass(OfficialHomeActivity.this,ModifyNameActivity.class);
			startActivity(intent5);
			break;
		case R.id.startaddress:
			Intent intent6 = new Intent();
			intent6.setClass(OfficialHomeActivity.this,AddressActivity.class);
			startActivity(intent6);
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
	 
	 public void selectTimeLongWindow(View parent) {  
		  
	        if (timeWindow2 == null) {  
	            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	  
	            timeview2 = layoutInflater.inflate(R.layout.time3_layout, null);  
	    		final WheelView hours2 = (WheelView) timeview2.findViewById(R.id.hour);
	    		NumericWheelAdapter hourAdapter = new NumericWheelAdapter(this, 0, 23);
	    		hourAdapter.setItemResource(R.layout.wheel_text_item);
	    		hourAdapter.setItemTextResource(R.id.text);
	    		hours2.setViewAdapter(hourAdapter);
	    		// set current time
	    		Calendar calendar = Calendar.getInstance(Locale.US);
	    		hours2.setCurrentItem(calendar.get(Calendar.HOUR));
	    	
	    		Button confirm  = (Button) timeview2.findViewById(R.id.comfirm);
	    		confirm.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if (timeWindow2 != null) {  
							timeWindow2.dismiss();  
		                }  
					}	
	    		});
	    		Button cancel  = (Button) timeview2.findViewById(R.id.canceltime);
	    		cancel.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if (timeWindow2 != null) {  
							timeWindow2.dismiss();  
		                }  
					}	
	    		});
	            // 创建一个PopuWidow对象  
	            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
	            int width = windowManager.getDefaultDisplay().getWidth() ;
	            int height = windowManager.getDefaultDisplay().getHeight();
	            timeWindow2 = new PopupWindow(timeview2, width, height);  
	        }  
	  
	        // 使其聚集  
	        timeWindow2.setFocusable(true);  
	        // 设置允许在外点击消失  
	        timeWindow2.setOutsideTouchable(true);  
	  
	        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
	        timeWindow2.setBackgroundDrawable(new BitmapDrawable());  
	        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  

	        timeWindow2.showAsDropDown(parent, 0, 0);  
 
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
}
