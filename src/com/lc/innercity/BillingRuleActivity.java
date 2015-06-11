package com.lc.innercity;

import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class BillingRuleActivity extends Activity implements OnClickListener {
	
    TextView tvTitle,textright;
    ImageView ivleft,ivCarbg;
    private RelativeLayout rls;
    private int[] mImageResourceIds;
    private Gallery mGallery;
    Button bttype;
    TextView tv1,tv2,tv3;
    String types[] = {"经济型","普通型","商务型"};
    String tvs1[] = {"9元/起步价","10元/起步价","11元/起步价"};
    String tvs2[] = {"1.6元/公里","1.7元/公里","1.8元/公里"};
    String tvs3[] = {"0.27元/分钟","0.28元/分钟","0.29元/分钟"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		 //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
		setContentView(R.layout.activity_innercity_bill);
		init();
	}
	public void init(){
		ExitApplication.getInstance().addActivity(this);
		bttype= (Button) findViewById(R.id.Type);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		mImageResourceIds = new int[]{R.drawable.temp1, R.drawable.temp2, R.drawable.temp5
				};
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("费用明细");
		textright = (TextView) findViewById(R.id.righttext2);
		textright.setVisibility(View.VISIBLE);
		textright.setText("武汉");
		
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		ivCarbg = (ImageView) findViewById(R.id.imageView);
		
		mGallery = (Gallery) findViewById(R.id.gallery);
		mGallery.setAdapter(new ImageAdapter(this));
		mGallery.setOnItemClickListener(new OnItemClickListener() {
			  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				  ivCarbg.setBackgroundResource(mImageResourceIds[arg2]);
			        }
			    });
		mGallery.setSelection(mImageResourceIds.length/2);
		mGallery.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ivCarbg.setBackgroundResource(mImageResourceIds[position]);
				bttype.setText(types[position]);
				tv1.setText(tvs1[position]);
				tv2.setText(tvs2[position]);
				tv3.setText(tvs3[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
       
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
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
	   
	/*
	 * Function   :    ImageAdapter类，继承自BaseAdapter
	 * Author     :    博客园-依旧淡然
	 */
	public class ImageAdapter extends BaseAdapter {

	    Context mContext;        //上下文对象
	    
	    //构造方法
	    public ImageAdapter(Context context) {
	        this.mContext = context;
	    }
	    
	    //获取图片的个数
	    public int getCount() {
	        return mImageResourceIds.length;
	    }

	    //获取图片在库中的位置
	    public Object getItem(int position) {
	        return mImageResourceIds[position];
	    }

	    //获取图片在库中的位置
	    public long getItemId(int position) {
	        return position;
	    }

	    //获取适配器中指定位置的视图对象
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView = new ImageView(mContext);
	        imageView.setImageResource(mImageResourceIds[position]);
	        imageView.setLayoutParams(new Gallery.LayoutParams(300, 400));
	        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	        return imageView;
	    }
	}
}
