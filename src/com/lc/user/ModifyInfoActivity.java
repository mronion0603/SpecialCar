package com.lc.user;

import org.json.JSONObject;

import com.lc.net.ModifyUserInfoNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyInfoActivity extends Activity implements OnClickListener {
	 TextView tvTitle,righttext;
	 ImageView ivleft;
	 EditText etName,etEmail;
	 private RelativeLayout rls;
	 RadioGroup group;
	 RadioButton bt1,bt2;
	 String name="",email="",gender="女";
	 ModifyUserInfoNet modifyUserInfoNet = new ModifyUserInfoNet();
	 private ProgressBar pro; 
	 
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.userinfo_modify);
        init();
	}
	void init(){
		ExitApplication.getInstance().addActivity(this);
		pro = (ProgressBar)findViewById(R.id.progress2); 
		pro.setProgress(0);  
		pro.setIndeterminate(true);
		righttext = (TextView) findViewById(R.id.righttext);
	    righttext.setVisibility(View.VISIBLE);
		righttext.setOnClickListener(this);
		righttext.setText("保存");
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("个人资料");
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		etName = (EditText) findViewById(R.id.Name);
		etEmail = (EditText) findViewById(R.id.Email);
		group = (RadioGroup)this.findViewById(R.id.radioGroup);
		bt1 = (RadioButton)this.findViewById(R.id.radioFemale);
		bt2 = (RadioButton)this.findViewById(R.id.radioMale);
		String username = MySharePreference.getStringValue(getApplication(), MySharePreference.USERNAME);
		if((username!=null)&&(username.length()>=1)){
			etName.setText( username);
		}
		String email = MySharePreference.getStringValue(getApplication(), MySharePreference.EMAIL);
		if(email!=null&&(email.length()>=1)){
			etEmail.setText(email);
		}
		String gender = MySharePreference.getStringValue(getApplication(), MySharePreference.GENDER);
		if(gender!=null){
			if(gender.equals("女")){
				bt1.setChecked(true);
			}else{
				bt2.setChecked(true);
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.righttext:
			name = etName.getText().toString();
			email = etEmail.getText().toString();
			if(name==null|name.length()<=0){
				 Toast.makeText(ModifyInfoActivity.this,"姓名不能为空", Toast.LENGTH_LONG).show();
			}else if(email==null|email.length()<=0){
				 Toast.makeText(ModifyInfoActivity.this,"邮箱不能为空", Toast.LENGTH_LONG).show();
			}else{
				righttext.setClickable(false);
				pro.setVisibility(View.VISIBLE); 
				int radioButtonId = group.getCheckedRadioButtonId();
		        //根据ID获取RadioButton的实例
		        RadioButton rb = (RadioButton)ModifyInfoActivity.this.findViewById(radioButtonId);
		        modifyUserInfoNet.setHandler(mHandler);
		        modifyUserInfoNet.setAuth(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		        modifyUserInfoNet.setEmail(email);
		        modifyUserInfoNet.setDevice(Global.DEVICE);
		        modifyUserInfoNet.setUsername(name);
		        //更新文本内容，以符合选中项
		        if(rb.getText().equals("男")){
		        	gender="男";
		        }else{
		        	gender="女";
		        }
		        modifyUserInfoNet.setGender(gender);
		        modifyUserInfoNet.getDataFromServer();
			}
			break;
		default:
			break;
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
	            case Global.UPDATEUSERINFO:{
	            	try {
						parseJSON((String)msg.obj);
					} catch (Exception e) {
						e.printStackTrace();
					}      	
	            break;
                }
            }
    }};
    private void parseJSON(String str)throws Exception{  
    	JSONObject jsonobj = new JSONObject(str); 
    	int result = jsonobj.getInt("ResultCode");
   	    if(result==Global.SUCCESS){
   	       MySharePreference.editStringValue(getApplication(), MySharePreference.USERNAME, name);
   	       MySharePreference.editStringValue(getApplication(), MySharePreference.EMAIL, email);
   	       MySharePreference.editStringValue(getApplication(), MySharePreference.GENDER, gender);
   	       Intent intent = new Intent();
           intent.putExtra("name", name);
           setResult(RESULT_OK, intent); 
   	       finish();
        }else{
           righttext.setClickable(true);
           Toast.makeText(ModifyInfoActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
        } 
   	    pro.setVisibility(View.GONE); 
    }
}
