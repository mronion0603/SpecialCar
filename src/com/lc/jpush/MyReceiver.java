package com.lc.jpush;


import org.json.JSONException;
import org.json.JSONObject;

import com.lc.innercity.SendDealActivity;
import com.lc.specialcar.ChooseUserActivity;
import com.lc.user.ItineraryDetailActivity;
import com.lc.utils.Global;
import com.lc.utils.MyApplication;
import com.lc.utils.MySharePreference;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private  Context mcontext;
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            //bundle.getString(JPushInterface.EXTRA_MESSAGE);
    		//System.out.println( bundle.getString(JPushInterface.EXTRA_MESSAGE));
            try{
            StringBuilder sb = new StringBuilder();
    		for (String key : bundle.keySet()) {
    			
    			if(key.equals(JPushInterface.EXTRA_EXTRA)){
    				String gettype = "";
    				gettype= bundle.getString(key);
    				JSONObject jsonobj = new JSONObject(gettype);  
    		        String result = jsonobj.getString("type");  
    		        //JSONObject jsonobj2 = new JSONObject(result); 
    		        System.out.println(result);
    		        if(result.equals("1")){
    		        	//打开自定义的Activity
    		        	/*
    		        	Intent i = new Intent(context, ArriveAlertActivity.class);
    		        	i.putExtras(bundle);
    		        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
    		        	context.startActivity(i);
    		        	*/
    		        }else{
    		        	//打开自定义的Activity
    		        	/*
    		        	Intent i = new Intent(context, MainActivity.class);
    		        	i.putExtra("Activity", "");
    		        	i.putExtras(bundle);
    		        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
    		        	context.startActivity(i);
    		        	*/
    		        }
    			} 
    		}
            }catch(JSONException e){
            	//打开自定义的Activity
            	/*
	        	Intent i = new Intent(context, MainActivity.class);
	        	i.putExtra("Activity", "");
	        	i.putExtras(bundle);
	        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
	        	context.startActivity(i);
	        	*/
            }
        
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if(key.equals(JPushInterface.EXTRA_EXTRA)){
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			} 
			/*
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}*/
		}
		return sb.toString();
	}
	private static String checktypeBundle(Bundle bundle)throws Exception {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			
			if(key.equals(JPushInterface.EXTRA_EXTRA)){
				String gettype = "";
				gettype= bundle.getString(key);
				JSONObject jsonobj = new JSONObject(gettype);  
		        String result = jsonobj.getString("type");  
		        //JSONObject jsonobj2 = new JSONObject(result); 
		        System.out.println(result);
			} 
		}
		return sb.toString();
	}
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		     mcontext = context;
		    //String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		    //String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			JSONObject jsonobj;
			try {
				jsonobj = new JSONObject(message);
				int result = jsonobj.getInt("ResultCode");
				if (result == Global.SUCCESS) {
					JSONObject jsonobj2 = jsonobj.getJSONObject("Data");
					if(jsonobj2.getString("type").equals("0")){
					  Intent msgIntent = new Intent(SendDealActivity.MESSAGE_RECEIVED_ACTION);
					  msgIntent.putExtra(SendDealActivity.KEY_MESSAGE, message);
					  context.sendBroadcast(msgIntent);
				    }else if(jsonobj2.getString("type").equals("1")){
				    	Intent intent = new Intent(context, ItineraryDetailActivity.class);
				    	intent.putExtras(bundle);
				    	/*
    		        	intent.putExtra("OrderStatus", "已完成");
    					intent.putExtra("OrderNum", jsonobj2.getString("orderNum"));
    					intent.putExtra("StartTime", jsonobj2.getString("startTime"));
    					intent.putExtra("StartAddress", jsonobj2.getString("startAddress"));
    					String getSerTypeId = jsonobj2.getString("serTypeId");
    					if(getSerTypeId.equals("1")){
    						getSerTypeId= "接机";
    					}else if(getSerTypeId.equals("2")){
    						getSerTypeId= "送机";	
    					}else if(getSerTypeId.equals("3")){
    						getSerTypeId= "公务包车";
    					}else if(getSerTypeId.equals("4")){
    						getSerTypeId= "市内约租";
    					}else{
    						getSerTypeId= "城际约租";
    					}
    					intent.putExtra("SerTypeId", getSerTypeId);
    					intent.putExtra("CarTypeId", jsonobj2.getString("carTypeId"));
    					intent.putExtra("R_BMoney", jsonobj2.getString("R_BMoney"));
    					intent.putExtra("stopCarMoney", jsonobj2.getString("stopCarMoney"));
    					intent.putExtra("EndAddress", jsonobj2.getString("endAddress"));
    					intent.putExtra("UseCarTime", jsonobj2.getString("useCarTime"));
    					intent.putExtra("CarSum", jsonobj2.getString("carSum"));
    					intent.putExtra("RiderName", jsonobj2.getString("riderName"));
    					intent.putExtra("RiderPhone", jsonobj2.getString("riderPhone"));
    					intent.putExtra("Comment", jsonobj2.getString("comment"));
    					intent.putExtra("Mileage", jsonobj2.getString("mileage"));
    					intent.putExtra("Time",  jsonobj2.getString("time"));
    					intent.putExtra("RealMoney",  jsonobj2.getString("realMoney"));
    					
    					intent.putExtra("carNum", jsonobj2.getString("carNum"));
    					intent.putExtra("carType", jsonobj2.getString("carType"));
    					intent.putExtra("driverName", jsonobj2.getString("driverName"));
    					intent.putExtra("driverImg", jsonobj2.getString("driverImg"));
    					intent.putExtra("mileageMoney", jsonobj2.getString("mileageMoney"));
    					intent.putExtra("bascMoney", jsonobj2.getString("bascMoney"));
    					intent.putExtra("timeMoney",jsonobj2.getString("timeMoney"));
    					intent.putExtra("assessType",  jsonobj2.getString("assessType"));
    					intent.putExtra("asssScore", jsonobj2.getString("assessScore"));
    		        	*/
    		        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    	intent.putExtra("orderNum", jsonobj2.getString("orderNum"));
				    	intent.putExtra("new", "new");
    					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
    		        	context.startActivity(intent);
				    } else if(jsonobj2.getString("type").equals("3")){
				    	 Intent msgIntent = new Intent(SendDealActivity.MESSAGE_RECEIVED_ACTION);
						 msgIntent.putExtra(SendDealActivity.KEY_MESSAGE, message);
						 context.sendBroadcast(msgIntent);
				    }else if(jsonobj2.getString("type").equals("4")){
				    	/*
				    	 new AlertDialog.Builder(mcontext)
				    	 .setTitle("警告") 
				    	 .setMessage("该账号在别的设备登录，您已下线！")
				    	 .setPositiveButton("确定", new OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								MySharePreference.clearPersonal(mcontext);
								Intent intent = new Intent(mcontext, ChooseUserActivity.class);
		    					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
		    					mcontext.startActivity(intent);
							}
				    	 })
				    	 .show();
                         */
				    	MySharePreference.clearPersonal(mcontext);
						Intent intent = new Intent(mcontext, ChooseUserActivity.class);
    					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
    					intent.putExtra("getoff", "getoff");
    					mcontext.startActivity(intent);
				    }
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
}
