package com.lc.intercity;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.lc.setting.ButtonEffect;
import com.lc.specialcar.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
    private PopupWindow timeWindow; 
    RadioGroup group;
	private View timeview; 
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
