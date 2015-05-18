package com.lc.innercity;


import java.util.ArrayList;
import java.util.List;

import com.lc.setting.ButtonEffect;
import com.lc.specialcar.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;



public class CarInfoActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,righttext,feeRule;
    ImageView ivleft;
    Button ivSearch;
    private RelativeLayout rls;
    private ImageView imAddress;
	private PopupWindow contactWindow; 
	private View view; 
	private View originview; 
	GroupAdapter groupAdapter;
	private List<Integer> groups;
	private List<String> groups1;
	private ListView lv_group,lv_group1; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_innercity_carinfo);
		init();
		
	}

	public void init(){
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
		case R.id.Search:
				
             
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
}
