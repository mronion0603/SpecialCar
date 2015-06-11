package com.lc.intercity;

import java.util.ArrayList;
import java.util.List;

import com.lc.innercity.AddressActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.ModifyNameActivity;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;




public class CarpoolActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
    TextView tvTitle,tvname,tvphone;
    Button ivSearch;
    ImageView ivleft,star;
    private RelativeLayout rls,modify;
    TextView chooseaddress;
	private PopupWindow contactWindow; 
	private View view; 
	private View originview; 
	GroupAdapter groupAdapter;
	private List<Integer> groups;
	private List<String> groups1;
	private ListView lv_group1; 
	EditText etnumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercity_carpool);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_intercity_carpool, null); 
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("拼车");
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		modify = (RelativeLayout) findViewById(R.id.modify);
		modify.setOnClickListener(this);
		chooseaddress = (TextView) findViewById(R.id.address);
		chooseaddress.setOnClickListener(this);
		star = (ImageView) findViewById(R.id.star);
		star.setOnClickListener(this);
		etnumber = (EditText) findViewById(R.id.number);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.star:
			selectAddressWindow(originview);
			break;
		case R.id.modify:
		{	Intent intent5 = new Intent();
			intent5.setClass(CarpoolActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
		}	break;
		case R.id.address:
		{Intent intent5 = new Intent();
		intent5.setClass(CarpoolActivity.this,AddressActivity.class);
		startActivityForResult(intent5, REQUSET_NAMEPHONE);
		}break;
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.Search:
			Intent intent = new Intent();
			intent.putExtra("title", "拼车");
			intent.setClass(getApplication(), SignUpActivity.class);
			startActivity(intent);
			break;
		default:
			break;
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
}
