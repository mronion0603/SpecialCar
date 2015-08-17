package com.lc.intercity;
import java.util.HashMap;
import java.util.List;

import com.lc.innercity.SendDealActivity;
import com.lc.specialcar.R;
import com.lc.utils.ConnectUrl;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class CarpoolListViewAdapter extends BaseAdapter {
	private Context context;                        //运行上下文   
    private List<HashMap<String,Object>> listItems;    //商品信息集合   
    private LayoutInflater listContainer;           //视图容器   
    public final class ListItemView{                //自定义控件集合      
            public ImageView carIcon;
            public TextView time;
            public TextView pickUpArea; 
            public TextView getOffArea; 
   
            public TextView curNum; 
            public TextView totalNum; 
            
     }     
       
       
    public CarpoolListViewAdapter(Context context, List<HashMap<String,Object>> listItems) {   
        this.context = context;            
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
        this.listItems = listItems;   
       // hasChecked = new boolean[getCount()];   
    }   
  
    public int getCount() {   
        // TODO Auto-generated method stub   
        return listItems.size();   
    }   
  
    public Object getItem(int arg0) {   
        // TODO Auto-generated method stub   
        return null;   
    }   
  
    public long getItemId(int arg0) {   
        // TODO Auto-generated method stub   
        return 0;   
    }   

    /**  
     * ListView Item设置  
     */  
    public View getView(int position, View convertView, ViewGroup parent) {   
        // TODO Auto-generated method stub   
        Log.e("method", "getView");   
       // final int selectID = position;   
        //自定义视图   
        ListItemView  listItemView = null;   
        if (convertView == null) {   
            listItemView = new ListItemView();    
            //获取list_item布局文件的视图   
            convertView = listContainer.inflate(R.layout.activity_intercity_carpool_listitem, null);   
            //获取控件对象   
            listItemView.carIcon =  (ImageView)convertView.findViewById(R.id.CarIcon);    
            listItemView.time = (TextView)convertView.findViewById(R.id.Time);   
            listItemView.pickUpArea =  (TextView)convertView.findViewById(R.id.PickUpArea);   
            listItemView.getOffArea =  (TextView)convertView.findViewById(R.id.GetOffArea); 
            listItemView.curNum =  (TextView)convertView.findViewById(R.id.CurNum); 
            listItemView.totalNum =  (TextView)convertView.findViewById(R.id.TotalNum); 
            //设置控件集到convertView   
            convertView.setTag(listItemView);   
        }else {   
            listItemView = (ListItemView)convertView.getTag();   
        }   

        //设置文字 
        listItemView.time.setText( listItems.get(position).get("Time")+"");  
        listItemView.pickUpArea.setText( listItems.get(position).get("PickUpArea")+"");    
        listItemView.getOffArea.setText(  listItems.get(position).get("GetOffArea")+"");   
        listItemView.curNum.setText( listItems.get(position).get("CurNum")+"");  
        listItemView.totalNum.setText( listItems.get(position).get("TotalNum")+"");  
        String getUsericon =listItems.get(position).get("CarIcon")+"";
        if(getUsericon.length()>0){
        	BitmapUtils bitmapUtils = new BitmapUtils(context);
	        bitmapUtils.display(listItemView.carIcon, ConnectUrl.commonurl0+getUsericon);
	    }else{
	    	//listItemView.shopIcon.setImageResource(R.drawable.activities_shopicon);
	    }
     
        return convertView;   
    }   
}
