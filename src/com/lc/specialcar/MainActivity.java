package com.lc.specialcar;



import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lc.innercity.InnerCityHomeActivity;
import com.lc.intercity.InterCityHomeActivity;
import com.lc.slidingmenu.fragment.LeftFragment;
import com.lc.slidingmenu.fragment.RightFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity implements OnClickListener, OnPageChangeListener {
	private RelativeLayout rlslidemenu;
	private Fragment mContent;
	private TextView topTextView;
	private ImageView ivOffice,ivPlane,ivInterCity,ivUrgency,ivCity, ivRight,ivleft;
	//ViewPager
	private ViewPager viewPager;
    // װ����ImageView����
	private ImageView[] tips;
	// װImageView����
	private ImageView[] mImageViews;
	//ͼƬ��Դid
	private int[] imgIdArray ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initSlidingMenu(savedInstanceState);
        init();
		initViewPager();
	}

	void init(){
		rlslidemenu = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rlslidemenu.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.topButton);
		ivleft.setVisibility(View.VISIBLE);
		topTextView = (TextView) findViewById(R.id.topTv);
		ivRight = (ImageView) findViewById(R.id.topButton2);
		ivRight.setVisibility(View.VISIBLE);
		ivRight.setOnClickListener(this);
		ivOffice = (ImageView) findViewById(R.id.office);
		ivOffice.setOnClickListener(this);
		ivPlane = (ImageView) findViewById(R.id.plane);
		ivPlane.setOnClickListener(this);
		ivInterCity = (ImageView) findViewById(R.id.intercity);
		ivInterCity.setOnClickListener(this);
		ivUrgency = (ImageView) findViewById(R.id.urgency);
		ivUrgency.setOnClickListener(this);
		ivCity = (ImageView) findViewById(R.id.city);
		ivCity.setOnClickListener(this);
		
		
	}
	void initViewPager(){
		ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.adv_pager);
		
		//����ͼƬ��ԴID
		imgIdArray = new int[]{R.drawable.home_ad, R.drawable.home_ad, R.drawable.home_ad, R.drawable.home_ad,
				R.drawable.home_ad,R.drawable.home_ad};
			
		//�������뵽ViewGroup��
		tips = new ImageView[imgIdArray.length];
		for(int i=0; i<tips.length; i++){
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10,10);
			lp.leftMargin=15;
	    	imageView.setLayoutParams(lp);
	    	tips[i] = imageView;
	    	if(i == 0){
	    		tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
	    	}else{
	    		tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
	    	}
	    	
	    	 group.addView(imageView);
		}
		
		//��ͼƬװ�ص�������
		mImageViews = new ImageView[imgIdArray.length];
		for(int i=0; i<mImageViews.length; i++){
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdArray[i]);
		}
		
		//����Adapter
		viewPager.setAdapter(new MyAdapter());
		//���ü�������Ҫ�����õ��ı���
		viewPager.setOnPageChangeListener(this);
		//����ViewPager��Ĭ����, ����Ϊ���ȵ�100���������ӿ�ʼ�������󻬶�
		viewPager.setCurrentItem((mImageViews.length) * 100);
	}
	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}

		if (mContent == null) {
			mContent = new RightFragment();
		}
		// 设置主界面视图
		// setContentView(R.layout.frag_right);
		// getSupportFragmentManager().beginTransaction()
		// .replace(R.id.right_frame, new RightFragment()).commit();

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// getSupportFragmentManager().putFragment(outState, "mContent",
		 //mContent);
		final FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();
		ft.add(mContent, "mContent");
	}

	/**
	 * 切换Fragment
	 * 
	 * @param fragment
	 */
	public void switchConent(Fragment fragment, String title) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
		topTextView.setText(title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlslidemenu:
			toggle();
			break;
		case R.id.intercity:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, InterCityHomeActivity.class);
			startActivity(intent);
			break;
		case R.id.city:
			Intent intent2 = new Intent();
			intent2.setClass(MainActivity.this, InnerCityHomeActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
	 @Override
	    protected void onResume() {
	        super.onResume();
	        //activity���������Ӻ󣬷���һ��message��������viewPager�е�ͼƬ�л�����һ��
	        mHandler.sendEmptyMessageDelayed(1, 5000);
	    }

	    @Override
	    protected void onStop() {
	        super.onStop();
	        //ֹͣviewPager��ͼƬ���Զ��л�
	        mHandler.removeMessages(1);
	    }
	    
	    @Override
	    protected void onPause() {
	        super.onPause();
	        mHandler.removeMessages(1);
	    }
		/**
		 * 
		 */
		public class MyAdapter extends PagerAdapter{

			@Override
			public int getCount() {
				return Integer.MAX_VALUE;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);
				
			}

			/**
			 * ����ͼƬ��ȥ���õ�ǰ��position ���� ͼƬ���鳤��ȡ�����ǹؼ�
			 */
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
				return mImageViews[position % mImageViews.length];
			}
			
			
			
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			setImageBackground(arg0 % mImageViews.length);
		}
		
		/**
		 * ����ѡ�е�tip�ı���
		 * @param selectItems
		 */
		private void setImageBackground(int selectItems){
			for(int i=0; i<tips.length; i++){
				if(i == selectItems){
					tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
				}else{
					tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
			}
		}
		
		private Handler mHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch(msg.what) {
	            case 1:
	                int totalcount = viewPager.getChildCount();//autoChangeViewPager.getChildCount();
	                int currentItem = viewPager.getCurrentItem();
	                int toItem = currentItem + 1 == totalcount ? 0 : currentItem + 1;
	                //Log.i(TAG, "totalcount: " + totalcount + "   currentItem: " + currentItem + "   toItem: " + toItem);
	                viewPager.setCurrentItem(toItem, true);
	                //ÿ�����ӷ���һ��message�������л�viewPager�е�ͼƬ
	                this.sendEmptyMessageDelayed(1, 5000);
	            }
	        }
	    };
}
