package com.lc.popupwindow;

import java.util.Calendar;
import java.util.Locale;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.lc.specialcar.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;


@SuppressLint("ViewConstructor")
public class TimePopupWindow extends PopupWindow {
	private View mMenuView;
	WheelView hours,mins,day;
	NumericWheelAdapter hourAdapter,minAdapter;
	DayArrayAdapter dayAdapter;
	public TimePopupWindow(Activity context,OnClickListener itemsOnClick) {
		super(context);	
		init(context);
		Button confirm  = (Button) mMenuView.findViewById(R.id.comfirm);
		confirm.setOnClickListener(itemsOnClick);
		Button cancel  = (Button) mMenuView.findViewById(R.id.canceltime);
		cancel.setOnClickListener(itemsOnClick);
		setWindow();
	}

	public TimePopupWindow(Activity context) {
		super(context);
		init(context);
		Button confirm  = (Button) mMenuView.findViewById(R.id.comfirm);
		confirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
					dismiss();   
			}	
		});
		Button cancel  = (Button) mMenuView.findViewById(R.id.canceltime);
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
			dismiss();  
			}	
		});
		setWindow();
	}
	void init(Activity context){
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.time2_layout, null);
		
		hours = (WheelView) mMenuView.findViewById(R.id.hour);
		hourAdapter = new NumericWheelAdapter(context, 0, 23);
		hourAdapter.setItemResource(R.layout.wheel_text_item);
		hourAdapter.setItemTextResource(R.id.text);
		hours.setViewAdapter(hourAdapter);
		
		
	    mins = (WheelView) mMenuView.findViewById(R.id.mins);
		minAdapter = new NumericWheelAdapter(context, 0, 59, "%02d");
		minAdapter.setItemResource(R.layout.wheel_text_item);
		minAdapter.setItemTextResource(R.id.text);
		mins.setViewAdapter(minAdapter);
		mins.setCyclic(true);
		// set current time
		Calendar calendar = Calendar.getInstance(Locale.US);
		hours.setCurrentItem(calendar.get(Calendar.HOUR));
		mins.setCurrentItem(calendar.get(Calendar.MINUTE));
		//ampm.setCurrentItem(calendar.get(Calendar.AM_PM));

		day = (WheelView) mMenuView.findViewById(R.id.day);
		dayAdapter=new DayArrayAdapter(context, calendar);
		day.setViewAdapter(dayAdapter);
	}
	@SuppressWarnings("deprecation")
	void setWindow(){
		//设置SelectPicPopupWindow的View
				this.setContentView(mMenuView);
				//设置SelectPicPopupWindow弹出窗体的宽
				this.setWidth(LayoutParams.FILL_PARENT);
				//设置SelectPicPopupWindow弹出窗体的高
				this.setHeight(LayoutParams.WRAP_CONTENT);
				//设置SelectPicPopupWindow弹出窗体可点击
				this.setFocusable(true);
				//设置SelectPicPopupWindow弹出窗体动画效果
				this.setAnimationStyle(R.style.mystyle);
				//实例化一个ColorDrawable颜色为半透明
				ColorDrawable dw = new ColorDrawable(0xb0000000);
				//设置SelectPicPopupWindow弹出窗体的背景
				this.setBackgroundDrawable(dw);
				//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
				mMenuView.setOnTouchListener(new OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						int height = mMenuView.findViewById(R.id.ll).getTop();
						int y=(int) event.getY();
						if(event.getAction()==MotionEvent.ACTION_UP){
							if(y<height){
								dismiss();
							}
						}				
						return true;
					}
				});
	}
	
	public String getTime(){
		
		String date = dayAdapter.getItemText(day.getCurrentItem())+" ";
		String date2 = hourAdapter.getItemText(hours.getCurrentItem())+":";
		String date3 = minAdapter.getItemText(mins.getCurrentItem())+"";
		return date+date2+date3;
	}
	public String getTimeUpload(){
		return dayAdapter.getItemText2(day.getCurrentItem())+"";
	}
	public String getTimeUpload2(){
		String date2 = hourAdapter.getItemText(hours.getCurrentItem())+":";
		String date3 = minAdapter.getItemText(mins.getCurrentItem())+"";
		return dayAdapter.getItemText2(day.getCurrentItem())+" "+date2+":"+date3;
	}
}
