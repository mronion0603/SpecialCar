package com.lc.innercity;

import com.lc.intercity.InterCityHomeActivity;
import com.lc.intercity.TypeCityActivity;
import com.lc.setting.ButtonEffect;
import com.lc.specialcar.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class ModifyNameActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,tvname,tvphone,tvright;

    ImageView ivleft;
    private RelativeLayout rls,modifyuser;
    String getname="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.innercity_modifyname);
		init();
		
	}

	public void init(){
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("更换乘车人");
		tvname = (TextView) findViewById(R.id.name);
		tvphone = (TextView) findViewById(R.id.phone);
		modifyuser = (RelativeLayout) findViewById(R.id.usecardate);
		modifyuser.setOnClickListener(this);
		tvright = (TextView) findViewById(R.id.righttext);
		tvright.setText("确认");
		tvright.setVisibility(View.VISIBLE);
		tvright.setOnClickListener(this);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.righttext:
			{
			 //Intent intent = new Intent();
			 //intent.setClass(ModifyNameActivity.this,BillingRuleActivity.class);
				
			 Intent intent = new Intent();
			 //intent.setClass(ModifyNameActivity.this,CarInfoActivity.class);
	         String name2 = tvname.getText().toString() ;
	         String phone2 = tvphone.getText().toString();
	         intent.putExtra("name", name2);
	         intent.putExtra("phone", phone2);
	         setResult(RESULT_OK, intent); 
	         finish();
			}break;
		case R.id.rlslidemenu:
			{finish();
			}break;
		case R.id.usecardate:
			{Intent intent = new Intent();  
		       intent.setAction(Intent.ACTION_PICK);  
		       intent.setData(ContactsContract.Contacts.CONTENT_URI);  
		       startActivityForResult(intent, 1);  
			}break;
		case R.id.Search:
			
			break;
		default:
			break;
		}
	}

	//重写的结果返回方法  
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        switch (requestCode) {  
        case 1:  
            if (data == null) {  
                return;  
            }  
            Uri contactData = data.getData();
            Cursor cursor = managedQuery(contactData, null, null, null,null);
            cursor.moveToFirst();
            String num = this.getContactPhone(cursor);
            tvphone.setText(num); 
            tvname.setText(getname);
            cursor.close();
            break;  
        }  
    }  
    private String getContactPhone(Cursor cursor) {
        // TODO Auto-generated method stub
        int phoneColumn = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String result = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    //int typeindex = phone
                    //       .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    String name = phone.getString(phone.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //int phone_type = phone.getInt(typeindex);
                    String phoneNumber = phone.getString(index);
                    result = phoneNumber;
                    getname=name;
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
        return result;
    }
 
}
