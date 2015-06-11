package com.lc.popupwindow;

import java.util.ArrayList;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ViewConstructor")
public class AddressPopupWindow extends PopupWindow {


	//private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;

	@SuppressWarnings("deprecation")
	public AddressPopupWindow(Activity context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.innercity_address, null);
		
		ListView lv_group1 = (ListView) mMenuView.findViewById(R.id.lvGroup);  
        // 加载数据  
		List<String> groups1 = new ArrayList<String>();  
         groups1.add("光谷广场");  
         groups1.add("同济大学");  
         groups1.add("盘龙城1234号");
         groups1.add("解放大道8000号");
         List<Integer> groups = new ArrayList<Integer>();  
         groups.add(R.drawable.select_site_home);  
         groups.add(R.drawable.select_site_work);  
         groups.add(R.drawable.select_site_collect);
         groups.add(R.drawable.select_site_collect);
         GroupAdapter groupAdapter = new GroupAdapter(context, groups1,groups);  
         lv_group1.setAdapter(groupAdapter);  
         lv_group1.setOnItemClickListener(new OnItemClickListener() {  	  
             @Override  
             public void onItemClick(AdapterView<?> adapterView, View view,  
                     int position, long id) {        
                	 dismiss();     
             }  
         });  
	
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
}
