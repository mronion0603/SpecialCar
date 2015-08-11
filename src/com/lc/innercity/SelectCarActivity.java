package com.lc.innercity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lc.net.GetCarNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MyDialog;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


@SuppressWarnings("deprecation")
public class SelectCarActivity extends Activity implements OnClickListener {
	    TextView tvTitle,textright;
	    ImageView ivleft;
	    private RelativeLayout rls;
	    private Gallery mGallery;
	    TextView tv1,tv2,tv3,tvtype,tvtype2;
	    List<HashMap<String,String>> list =new ArrayList<HashMap<String,String>>();
	    GetCarNet getcarnet = new GetCarNet();
	    int currentposition = 0;
	    Dialog dia;
	    String type="经济";
	    int [] imgs = {R.drawable.fee_car1,R.drawable.fee_car2,R.drawable.fee_car3}; 
		//private ProgressBar pb; 
		@Override
		public void onCreate(Bundle savedInstanceState) {
			requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
			super.onCreate(savedInstanceState);
			setContentView(R.layout.innercity_selectcar);
			init();
		}
		public void init(){
			
			ExitApplication.getInstance().addActivity(this);
			//pb = (ProgressBar)findViewById(R.id.progress); 
			tvtype2 = (TextView) findViewById(R.id.type2);
			tvtype = (TextView) findViewById(R.id.type);
			tv1 = (TextView) findViewById(R.id.tv1);
			tv2 = (TextView) findViewById(R.id.tv2);
			tv3 = (TextView) findViewById(R.id.tv3);
			mGallery = (Gallery) findViewById(R.id.gallery);
			tvTitle = (TextView) findViewById(R.id.topTv);
			tvTitle.setText("选择车型");
			textright = (TextView) findViewById(R.id.righttext);
			textright.setVisibility(View.VISIBLE);
			textright.setText("确定");
			textright.setOnClickListener(this);
			rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
			rls.setOnClickListener(this);
			ivleft = (ImageView) findViewById(R.id.ArrowHead);
			ivleft.setVisibility(View.VISIBLE);
			//ivCarbg = (ImageView) findViewById(R.id.imageView);
					
			//initCarType();
			getcarnet.setHandler(mHandler);
			getcarnet.setDevice(Global.DEVICE);
			getcarnet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
			getcarnet.getDataFromServer();
			
			dia = MyDialog.createLoadingDialog(SelectCarActivity.this, "正在加载");
			dia.show();
		}
		
		void initCarType(){
			//mImageResourceIds = new int[]{R.drawable.temp1, R.drawable.temp2, R.drawable.temp5
			//};
		
			mGallery.setAdapter(new ImageAdapter(this));
			mGallery.setOnItemClickListener(new OnItemClickListener() {
				  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					    //ivCarbg.setBackgroundResource(mImageResourceIds[arg2]);
					  //  BitmapUtils bitmapUtils = new BitmapUtils(SelectCarActivity.this);
				    	// 加载网络图片
					  //  String str = list.get(arg2).get("carImg")+"";
				    //	bitmapUtils.display(ivCarbg, ConnectUrl.commonurl0+str);
				      }
				    });
			mGallery.setSelection(list.size()/2);
			mGallery.setOnItemSelectedListener(new OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					currentposition = position;
					// BitmapUtils bitmapUtils = new BitmapUtils(SelectCarActivity.this);
				    	// 加载网络图片
					    //String str = list.get(position).get("carImg")+"";
				    	//bitmapUtils.display(ivCarbg, ConnectUrl.commonurl0+str);
				    	
					//ivCarbg.setBackgroundResource(mImageResourceIds[position]);
					String type = list.get(position).get("carTypeId");
					String type2 = "";
					if(type.equals("1")){
						type="经济";
						type2="帕萨特 凯美瑞或类似5座车型";
					}else if(type.equals("2")){
						type="普通";
						type2="别克GL8或类似6座车型";
					}else{
						type="商务";
						type2="奥迪A6 宝马系列或类似5座车型";
					}
					tvtype.setText(type);
					tvtype2.setText(type2);
					String basicmoney = "<font color='black'><b>" + list.get(position).get("bascMoney")+ "</b></font>"
		                       +"<font color='#999999'><b>" + "元/起步价"+ "</b></font>";
					String mileageMoney = "<font color='black'><b>" + list.get(position).get("mileageMoney")+ "</b></font>"
		                       +"<font color='#999999'><b>" + "元/公里"+ "</b></font>";
					String timeMoney = "<font color='black'><b>" + list.get(position).get("timeMoney")+ "</b></font>"
		                       +"<font color='#999999'><b>" + "元/分钟"+ "</b></font>";
					tv1.setText(Html.fromHtml(basicmoney));
					tv2.setText(Html.fromHtml(mileageMoney));
					tv3.setText(Html.fromHtml(timeMoney));
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
			 Toast.makeText(SelectCarActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
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
				
				intent.putExtra("strtype",type);
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
		        //imageView.setLayoutParams(new Gallery.LayoutParams(300, 400));
		        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		        
		        //BitmapUtils bitmapUtils = new BitmapUtils(SelectCarActivity.this);
		    	// 加载网络图片
			    //String str = list.get(position).get("carImg")+"";
		    	//bitmapUtils.display(imageView, ConnectUrl.commonurl0+str);
		    	imageView.setBackgroundResource(imgs[position]);
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
