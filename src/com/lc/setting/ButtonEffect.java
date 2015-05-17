package com.lc.setting;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ButtonEffect {
    
		private static final OnTouchListener touchListener = new OnTouchListener() { 
		  @Override 
		  public boolean onTouch(View v, MotionEvent event) { 
		   if(event.getAction() == MotionEvent.ACTION_DOWN) { 
			   v.setAlpha(0.6f);
		   }else if(event.getAction() == MotionEvent.ACTION_UP) { 
			   v.setAlpha(1.0f);
		   } 
		   return false; 
		  } 
		}; 
		public static void setButtonStateChangeListener(View v) { 
		  v.setOnTouchListener(touchListener); 
		}
		
		private static final OnTouchListener touchListener2 = new OnTouchListener() { 
			  @Override 
			  public boolean onTouch(View v, MotionEvent event) { 
			   if(event.getAction() == MotionEvent.ACTION_DOWN) { 
				   v.setBackgroundColor(Color.parseColor("#EEEEEE"));
			   }else if(event.getAction() == MotionEvent.ACTION_UP) { 
				   v.setBackgroundColor(Color.parseColor("#FFFFFF"));
			   } 
			   return false; 
			  } 
			}; 
			public static void setButtonStateChangeListener2(View v) { 
				  v.setOnTouchListener(touchListener2); 
				}
}
