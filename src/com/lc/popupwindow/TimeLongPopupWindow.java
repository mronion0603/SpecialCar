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
public class TimeLongPopupWindow extends PopupWindow {


	//private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	NumericWheelAdapter hourAdapter;
    WheelView hours2;
	@SuppressWarnings("deprecation")
	public TimeLongPopupWindow(Activity context,OnClickListener timelongitemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.time3_layout, null);
		
		hours2 = (WheelView) mMenuView.findViewById(R.id.hour);
	    hourAdapter = new NumericWheelAdapter(context, 0, 23);
		hourAdapter.setItemResource(R.layout.wheel_text_item);
		hourAdapter.setItemTextResource(R.id.text);
		hours2.setViewAdapter(hourAdapter);
		// set current time
		Calendar calendar = Calendar.getInstance(Locale.US);
		hours2.setCurrentItem(calendar.get(Calendar.HOUR));
	
		Button confirm  = (Button) mMenuView.findViewById(R.id.comfirm);
		confirm.setOnClickListener(timelongitemsOnClick);
		Button cancel  = (Button) mMenuView.findViewById(R.id.canceltime);
		cancel.setOnClickListener(timelongitemsOnClick);
	
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
       return hourAdapter.getItemText(hours2.getCurrentItem())+"";
	}
}
