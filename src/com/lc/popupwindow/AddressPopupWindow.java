package com.lc.popupwindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lc.innercity.GroupAdapter;
import com.lc.specialcar.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class AddressPopupWindow extends PopupWindow {
	//private Button btn_take_photo, btn_pick_photo, btn_cancel;
	List<String> groups1;
	List<HashMap<String , Object>> groups3;
	private View mMenuView;
	public AddressPopupWindow(Activity context,OnItemClickListener itemClickListener,
			List<HashMap<String , Object>> groups2 ,String title) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.innercity_address, null);

        // 加载数据  
		 groups3 = new ArrayList<HashMap<String , Object>>(); 
		 groups3= groups2;

         GroupAdapter groupAdapter = new GroupAdapter(context, groups2);  
         ListView lv_group1 = (ListView) mMenuView.findViewById(R.id.lvGroup);  
         lv_group1.setAdapter(groupAdapter); 
         //setListViewHeightBasedOnChildren(lv_group1);
         lv_group1.setOnItemClickListener(itemClickListener);  
         TextView tv = (TextView) mMenuView.findViewById(R.id.tv);  
         tv.setText(title);
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
				int height = mMenuView.findViewById(R.id.lvGroup).getTop();
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
	public AddressPopupWindow(Activity context,OnItemClickListener itemClickListener,List<HashMap<String , Object>> groups2 ) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.innercity_address, null);
		
        // 加载数据  
		 groups3 = new ArrayList<HashMap<String , Object>>(); 
		 groups3= groups2;

         GroupAdapter groupAdapter = new GroupAdapter(context, groups2);  
         ListView lv_group1 = (ListView) mMenuView.findViewById(R.id.lvGroup);  
         lv_group1.setAdapter(groupAdapter); 
         setListViewHeightBasedOnChildren(lv_group1);
         /*
         ViewGroup.LayoutParams params = lv_group1.getLayoutParams();
         WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
         int height1 = wm.getDefaultDisplay().getHeight()/2 -50;
         int height2= 0;
         if(groupAdapter.getCount()>=1){
        	View listItem = groupAdapter.getView(0, null, lv_group1);
 	        listItem.measure(0, 0);
 	        height2 = listItem.getMeasuredHeight()*groupAdapter.getCount();
         }
         if(height1>height2){
        	 params.height = height2;
         }else{
        	 params.height = height1;
         }
         lv_group1.setLayoutParams(params);
         */
         lv_group1.setOnItemClickListener(itemClickListener);  
         
         
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
				int height = mMenuView.findViewById(R.id.lvGroup).getTop();
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

	public String getItemStr2(int index){
		return (String)groups3.get(index).get("address");
	}
	public HashMap<String , Object> getMap(int index){
		return groups3.get(index);
	}
	/**
	* 动态设置ListView的高度
	* @param listView
	*/
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    if(listView == null) return;

	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	        // pre-condition
	        return;
	    }

	    int totalHeight = 0;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        View listItem = listAdapter.getView(i, null, listView);
	        listItem.measure(0, 0);
	        totalHeight += listItem.getMeasuredHeight();
	    }

	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    int finalheight = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    if(listAdapter.getCount()<4){
	    	params.height = finalheight;
	    }else{
	    	double total = listAdapter.getCount();
	    	double cur = 4;
	    	double k = cur/total;
	    	double final2 =(double)(finalheight*k);
	    	params.height = (int)final2;
	    }
	    listView.setLayoutParams(params);
	}


}
