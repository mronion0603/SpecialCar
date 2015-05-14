package com.lc.specialcar;



import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lc.slidingmenu.fragment.LeftFragment;
import com.lc.slidingmenu.fragment.RightFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity implements OnClickListener {
	private RelativeLayout rlslidemenu;
	private Fragment mContent;
	private TextView topTextView;
	private ImageView ivOffice,ivPlane,ivInterCity,ivUrgency,ivCity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initSlidingMenu(savedInstanceState);

		rlslidemenu = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rlslidemenu.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
	
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

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

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
		// mContent);
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
		default:
			break;
		}
	}

}
