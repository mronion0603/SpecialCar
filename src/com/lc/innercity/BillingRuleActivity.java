package com.lc.innercity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.net.GetCarNet;
import com.lc.specialcar.R;
import com.lc.utils.ConnectUrl;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MyDialog;
import com.lc.utils.MySharePreference;
import com.lidroid.xutils.BitmapUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


@SuppressWarnings("deprecation")
public class BillingRuleActivity extends Activity implements OnClickListener {
	    TextView tvTitle,textright;
	    ImageView ivleft,ivCarbg;
	    private RelativeLayout rls;
	    private Gallery mGallery;
	    Button bttype;
	    TextView tv1,tv2,tv3;
	    List<HashMap<String,String>> list =new ArrayList<HashMap<String,String>>();
	    GetCarNet getcarnet = new GetCarNet();
	    int currentposition = 0;
	    Dialog dia;
	 
		//private ProgressBar pb; 
		@Override
		public void onCreate(Bundle savedInstanceState) {
			requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_innercity_bill);
			init();
		}
		public void init(){
			
			ExitApplication.getInstance().addActivity(this);
			//pb = (ProgressBar)findViewById(R.id.progress); 
			bttype= (Button) findViewById(R.id.Type);
			tv1 = (TextView) findViewById(R.id.tv1);
			tv2 = (TextView) findViewById(R.id.tv2);
			tv3 = (TextView) findViewById(R.id.tv3);
			mGallery = (Gallery) findViewById(R.id.gallery);
			tvTitle = (TextView) findViewById(R.id.topTv);
			tvTitle.setText("费用明细");
			textright = (TextView) findViewById(R.id.righttext);
			textright.setVisibility(View.VISIBLE);
			textright.setText("确定");
			textright.setOnClickListener(this);
			rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
			rls.setOnClickListener(this);
			ivleft = (ImageView) findViewById(R.id.ArrowHead);
			ivleft.setVisibility(View.VISIBLE);
			ivCarbg = (ImageView) findViewById(R.id.imageView);
					
			//initCarType();
			getcarnet.setHandler(mHandler);
			getcarnet.setDevice(Global.DEVICE);
			getcarnet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
			getcarnet.getDataFromServer();
			
			dia = MyDialog.createLoadingDialog(BillingRuleActivity.this, "正在加载");
			dia.show();
		}
		
		void initCarType(){
			//mImageResourceIds = new int[]{R.drawable.temp1, R.drawable.temp2, R.drawable.temp5
			//};
		
			mGallery.setAdapter(new ImageAdapter(this));
			mGallery.setOnItemClickListener(new OnItemClickListener() {
				  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					    //ivCarbg.setBackgroundResource(mImageResourceIds[arg2]);
					    BitmapUtils bitmapUtils = new BitmapUtils(BillingRuleActivity.this);
				    	// 加载网络图片
					    String str = list.get(arg2).get("carImg")+"";
				    	bitmapUtils.display(ivCarbg, ConnectUrl.commonurl0+str);
				      }
				    });
			mGallery.setSelection(list.size()/2);
			mGallery.setOnItemSelectedListener(new OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					currentposition = position;
					 BitmapUtils bitmapUtils = new BitmapUtils(BillingRuleActivity.this);
				    	// 加载网络图片
					    String str = list.get(position).get("carImg")+"";
				    	bitmapUtils.display(ivCarbg, ConnectUrl.commonurl0+str);
				    	
					//ivCarbg.setBackgroundResource(mImageResourceIds[position]);
					String type = list.get(position).get("carTypeId");
					if(type.equals("1"))
					bttype.setText("经济");
					else if(type.equals("2"))
					bttype.setText("普通");
					else
					bttype.setText("商务");
					//bttype.setText(list.get(position).get("carDesc"));
					tv1.setText(list.get(position).get("bascMoney")+"元/起步价");
					tv2.setText(list.get(position).get("mileageMoney")+"元/公里");
					tv3.setText(list.get(position).get("timeMoney")+"元/分钟");
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
				
			});
			
			dia.dismiss();
		}
		 @SuppressLint("HandlerLeak")
			private Handler mHandler = new Handler() {
		        public void handleMessage(android.os.Message msg) {
		            switch(msg.what) { 
			            case Global.GETCARTYPE:{
			            	try {
								parseJSON((String)msg.obj);
							} catch (Exception e) {	
								e.printStackTrace();
							}      	
			            break;
		                }
		            }
		        }};
		        
	private void parseJSON(String str) throws Exception {
		System.out.println("ss:"+str);
		JSONObject jsonobj = new JSONObject(str);
		if (jsonobj.getInt("ResultCode") == Global.SUCCESS) {
			JSONArray jsonarray = jsonobj.getJSONArray("Data");
			for (int x = 0; x < jsonarray.length(); x++) {
				JSONObject jsonobj2 = (JSONObject) jsonarray.get(x);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("carTypeId", jsonobj2.getString("carTypeId"));
				map.put("bascMoney", jsonobj2.getString("bascMoney"));
				map.put("mileageMoney", jsonobj2.getString("mileageMoney"));
				map.put("timeMoney", jsonobj2.getString("timeMoney"));
				map.put("carDesc", jsonobj2.getString("carDesc"));
				map.put("carImg", jsonobj2.getString("carImg"));
				map.put("inMileage", jsonobj2.getString("inMileage"));
			    list.add(map);
			}
		
			initCarType();
		}else{
			 Toast.makeText(BillingRuleActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
		}
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
				
				intent.putExtra("strtype",bttype.getText().toString());
			    intent.putExtra("type", (String)list.get(currentposition).get("carTypeId"));
			    intent.putExtra("bascMoney", (String)list.get(currentposition).get("bascMoney"));
			    intent.putExtra("mileageMoney", (String)list.get(currentposition).get("mileageMoney"));
			    intent.putExtra("timeMoney", (String)list.get(currentposition).get("timeMoney"));
			    setResult(RESULT_OK, intent); 
				finish();
				break;
			case R.id.Search:
					//Intent intent = new Intent();
					//intent.setClass(InnerCityHomeActivity.this, SearchCarpoolActivity.class);
					//startActivity(intent);
	             
				break;
				
			default:
				break;
			}
		}

		public class ImageAdapter extends BaseAdapter {

		    Context mContext;        //上下文对象
		    
		    //构造方法
		    public ImageAdapter(Context context) {
		        this.mContext = context;
		    }
		    
		    //获取图片的个数
		    public int getCount() {
		        return list.size();
		    }

		    //获取图片在库中的位置
		    public Object getItem(int position) {
		        return list.get(position);
		    }

		    //获取图片在库中的位置
		    public long getItemId(int position) {
		        return position;
		    }

		    //获取适配器中指定位置的视图对象
		    public View getView(int position, View convertView, ViewGroup parent) {
		        ImageView imageView = new ImageView(mContext);
		       // imageView.setImageResource(mImageResourceIds[position]);
		        imageView.setLayoutParams(new Gallery.LayoutParams(300, 400));
		        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		        
		        BitmapUtils bitmapUtils = new BitmapUtils(BillingRuleActivity.this);
		    	// 加载网络图片
			    String str = list.get(position).get("carImg")+"";
		    	bitmapUtils.display(imageView, ConnectUrl.commonurl0+str);
		    	
		        return imageView;
		    }
		}
		 @Override  
		 public void onBackPressed() {  
			 if(dia.isShowing())
		        dia.dismiss();
		        finish();
		 }  
}
