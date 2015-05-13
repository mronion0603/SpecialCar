package com.lc.slidingmenu.fragment;

import com.lc.specialcar.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author wuwenjie
 * @description 今日
 */
public class TodayFragment extends Fragment {
	 private LinearLayout listView;
	 private Button tv1;
	 private  Button tv2;
	private int[] idarr = new int[]{R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4,R.id.tv5,R.id.tv6,R.id.tv7,R.id.tv8};
	private int[] colorarr = new int[]{0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF};
	private int[] bgarr = new int[]{0xFFFF6666,0xFF1e67c0,0xFFd47756,0xFF5a626f,0xFFee7434,0xFF3eadeb,0xFF0385fd,0xFF00a179};
	private String[] textarr = new String[]{"通讯电脑","生活厨卫","家用电器","日用百货","母婴玩具","户外体育","汽车配件","其它商品"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_today, null);
		 listView   = (LinearLayout) view.findViewById(R.id.listView);
		 for(int i=0;i<idarr.length;i++){
				TextView tv = (TextView) view.findViewById(idarr[i]);
				tv.setText(textarr[i]);
				tv.setBackgroundColor(bgarr[i]);
				tv.setTextColor(colorarr[i]);
				view.findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
				            @Override
				            public void onClick(View view) {
				            	
				            	// startActivity(new Intent(newsinfo.this, dongtuActivity.class));
				            	 // newsinfo.this.finish();
				            	//showMessage("您点击的是 :电脑 ");
				            }
				        });
				}
		 view.findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
		            public void onClick(View view) {
		            	
		            	// startActivity(new Intent(newsinfo.this, dongtuActivity.class));
		            	 // newsinfo.this.finish();
		            	//showMessage("您点击的是 :生活 ");
		            }
		        });
	
		return view;
	}
	

	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
