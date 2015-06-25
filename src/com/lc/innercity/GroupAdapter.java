package com.lc.innercity;

import java.util.HashMap;
import java.util.List;






import com.lc.specialcar.R;

import android.content.Context;  
import android.graphics.Color;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;  

  
public class GroupAdapter extends BaseAdapter {  
  
    private Context context;  
    private List<HashMap<String , Object>> list;  
    private int selectedPosition = 0;
    public GroupAdapter(Context context, List<HashMap<String , Object>> list) {    
        this.context = context;  
        this.list = list;  
    }  
   
  
    @Override  
    public int getCount() {  
        return list.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
  
        return list.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
    public void setSelectedPosition(int position) {    
        selectedPosition = position;    
    	
    }    
    @Override  
    public View getView(int position, View convertView, ViewGroup viewGroup) {  
  
          
        ViewHolder holder;  
        if (convertView==null) {  
            convertView=LayoutInflater.from(context).inflate(R.layout.innercity_address_listitem, null);  
            holder=new ViewHolder();  
              
            convertView.setTag(holder);  
              
            holder.groupItem=(TextView) convertView.findViewById(R.id.groupItem);  
            holder.itembg=(RelativeLayout) convertView.findViewById(R.id.itembg);  
            holder.groupImage=(ImageView) convertView.findViewById(R.id.groupItem2);  
        }  
        else{  
            holder=(ViewHolder) convertView.getTag();  
        }  
        holder.groupItem.setTextColor(Color.BLACK);  
        holder.groupItem.setText((String)list.get(position).get("address"));  
        //holder.groupImage.setBackgroundResource(list2.get(position));
        
        if(selectedPosition == position){
        	
        	//holder.itembg.setBackgroundResource(R.drawable.filter_choosen);
        }else{
        	//holder.itembg.setBackgroundColor(Color.parseColor("#ffefebe7"));
        }
        return convertView;  
    }  
  
    static class ViewHolder {  
    	ImageView groupImage; 
        TextView groupItem;  
        RelativeLayout itembg;
    }  
  
}  