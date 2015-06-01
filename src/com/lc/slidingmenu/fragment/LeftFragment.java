package com.lc.slidingmenu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.lc.specialcar.MainActivity;
import com.lc.specialcar.R;
import com.lc.user.AddressManageActivity;
import com.lc.user.ItineraryActivity;
import com.lc.user.LoginActivity;
import com.lc.user.MessageActivity;
import com.lc.user.ReceiptActivity;

/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener{
	private View userinfo;
	private View todayView;
	private View lastListView;
	private View discussView;
	private View itineraryView;
	private View receiptView;
	private View addressView;
	private View messageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		
		return view;
	}
	
	
	public void findViews(View view) {
		userinfo = view.findViewById(R.id.personbg);
		todayView = view.findViewById(R.id.officeaccount);
		lastListView = view.findViewById(R.id.tvLastlist);
		discussView = view.findViewById(R.id.tvDiscussMeeting);
		itineraryView = view.findViewById(R.id.Itinerary);
		receiptView = view.findViewById(R.id.Receipt);
		addressView = view.findViewById(R.id.AddressManage);
		messageView = view.findViewById(R.id.Message);
		
		userinfo.setOnClickListener(this);
		todayView.setOnClickListener(this);
		lastListView.setOnClickListener(this);
		discussView.setOnClickListener(this);
		itineraryView.setOnClickListener(this);
		receiptView.setOnClickListener(this);
		addressView.setOnClickListener(this);
		messageView.setOnClickListener(this);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.personbg: // 今日
		{
			
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), LoginActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.officeaccount: // 今日
			newContent = new TodayFragment();
			title = getString(R.string.today);
			break;
		case R.id.tvLastlist:// 往期列表
			newContent = new LastListFragment();
			title = getString(R.string.lastList);
			break;
		case R.id.tvDiscussMeeting: // 讨论集会
			newContent = new DiscussFragment();
			title = getString(R.string.discussMeetting);
			break;
		case R.id.Itinerary: 
		{// 行程
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), ItineraryActivity.class);
			startActivity(intent);
			
		}
			break;
		case R.id.Receipt: // 发票
		{
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), ReceiptActivity.class);
			startActivity(intent);
		}	break;
		case R.id.Message: // 消息
		{
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), MessageActivity.class);
			startActivity(intent);
		}	break;
		case R.id.AddressManage: // 设置
		{	
			Intent intent = new Intent();
			intent.setClass(this.getActivity().getApplicationContext(), AddressManageActivity.class);
			startActivity(intent);
		}	break;
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent, title);
		}
	}
	
	/**
	 * 切换fragment
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchConent(fragment, title);
		}
	}
	
}
