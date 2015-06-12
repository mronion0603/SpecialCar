package com.lc.popupwindow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lc.specialcar.R;

/**
 * Day adapter
 *
 */
 @SuppressLint("SimpleDateFormat")
class DayArrayAdapter extends AbstractWheelTextAdapter {
	// Count of days to be shown
	private final int daysCount = 7;

	// Calendar
	Calendar calendar;
	TextView weekday,monthday;
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
		 weekday = (TextView) view.findViewById(R.id.time2_weekday);
		if (day == 0) {
			weekday.setText("");
		} else {
			DateFormat format = new SimpleDateFormat("EEE");
			weekday.setText(format.format(newCalendar.getTime()));
		}

		 monthday = (TextView) view.findViewById(R.id.time2_monthday);
		if (day == 0) {
			monthday.setText("今天");
			monthday.setTextColor(0xFF29b43d);
			//monthday.setTextColor(0xFF0000F0);
		} else {
			DateFormat format = new SimpleDateFormat("MMMd");
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
		 if (index == 0 ) {
			 return "今天";
	     }else{
	    	 Calendar newCalendar = (Calendar) calendar.clone();
	 		 newCalendar.roll(Calendar.DAY_OF_YEAR, index);
	 		 DateFormat format = new SimpleDateFormat("EEE");
			 DateFormat format2 = new SimpleDateFormat("MMMd");
			 return  format2.format(newCalendar.getTime())+"日  "+format.format(newCalendar.getTime());
	     }
	     
	}
}
