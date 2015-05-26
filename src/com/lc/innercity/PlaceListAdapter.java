package com.lc.innercity;

import java.util.List;  

import android.R.integer;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.ImageView;  
import android.widget.TextView;  
  

import com.baidu.mapapi.search.core.PoiInfo;  
import com.lc.specialcar.R;
 
  
public class PlaceListAdapter extends BaseAdapter {  
      
    List<PoiInfo> mList;  
    LayoutInflater mInflater;  
    int notifyTip ;  
      
    private class MyViewHolder {  
        TextView placeName;  
        TextView placeAddree;  
        ImageView placeSelected;  
    }  
  
    public PlaceListAdapter(LayoutInflater mInflater , List<PoiInfo> mList) {  
        super();  
        this.mList = mList;  
        this.mInflater = mInflater;  
        notifyTip = -1 ;  
    }  
      
      
    /** 
     * 设置第几个item被选择 
     * @param notifyTip 
     */  
    public void setNotifyTip(int notifyTip) {  
        this.notifyTip = notifyTip;  
    }  
      
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return mList.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return mList.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
        MyViewHolder holder;  
        if (convertView == null) {  
            System.out.println("----aa-");  
            convertView = mInflater.inflate(R.layout.listitem_place, parent, false);        
            holder = new MyViewHolder();  
            holder.placeName = (TextView) convertView  
                    .findViewById(R.id.place_name);  
            holder.placeAddree = (TextView) convertView  
                    .findViewById(R.id.place_adress);  
            holder.placeSelected = (ImageView) convertView  
                    .findViewById(R.id.place_select);  
            holder.placeName.setText(mList.get(position).name);  
            holder.placeAddree.setText(mList.get(position).address);  
            holder.placeSelected.setBackgroundResource(R.drawable.rectangle_bg);  
            convertView.setTag(holder);  
        } else {  
            holder = (MyViewHolder) convertView.getTag();             
        }  
        holder.placeName.setText(mList.get(position).name);  
        holder.placeAddree.setText(mList.get(position).address);  
        //根据重新加载的时候第position条item是否是当前所选择的，选择加载不同的图片  
        if(notifyTip == position ){           
            holder.placeSelected.setBackgroundResource(R.drawable.check);  
        }             
        else {  
            holder.placeSelected.setBackgroundResource(R.drawable.rectangle_bg);  
        }  
  
        return convertView;  
    }  
      
      
//  class MyItemClickListener implements OnClickListener {  
//  
//      ImageView mImg;  
//      public MyItemClickListener(ImageView mImg) {  
//          this.mImg = mImg;   
//      }  
//      @Override  
//      public void onClick(View v) {  
//          // TODO Auto-generated method stub  
//          mImg.setBackgroundResource(R.drawable.ic_select);  
//      }  
//        
//  }  
      
  
} 