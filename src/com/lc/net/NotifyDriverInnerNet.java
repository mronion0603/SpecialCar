package com.lc.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.lc.utils.ConnectUrl;

import android.os.Handler;


public class NotifyDriverInnerNet {
	private Handler handler;
	private boolean isRunning = false;
	String result = "网络连接错误！";
	String orderNum = "";
	String driverNum = "";
	public NotifyDriverInnerNet(){
		
	}
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	public void setDriverNum(String driverNum){
		this.driverNum = driverNum;
	}
	public void setOrderNum(String orderNum){
		this.orderNum = orderNum;
	}
	
	public void getDataFromServer(){
		isRunning = true;
		Thread connectThread; //声明一个子线程
		 connectThread = new Thread(new Runnable() {
	        	
	            @Override
	              public void run() {       
	                while (isRunning) {
					  try {
						  String strurl =ConnectUrl.addnotifydriverinner;
		                	//URL url = null;
		                	try{
		                		String spec = strurl +"?orderNum=" + URLEncoder.encode(orderNum, "UTF-8")
		                                +"&driverNum=" + URLEncoder.encode(driverNum, "UTF-8")   
		                		        //+"&mcode=" + URLEncoder.encode(mcode, "UTF-8")
		                		        ;
		                        // 根据地址创建URL对象(网络访问的url)  
		                        URL url = new URL(spec);  
		                        // url.openConnection()打开网络链接  
		                        HttpURLConnection urlConnection = (HttpURLConnection) url  
		                                .openConnection();  
		                        urlConnection.setRequestMethod("GET");// 设置请求的方式  
		                        urlConnection.setReadTimeout(5000);// 设置超时的时间  
		                        urlConnection.setConnectTimeout(5000);// 设置链接超时的时间  
		                        // 设置请求的头  

		                		urlConnection.setRequestProperty("Mime-Type", "application/x-www-form-urlencoded");
		                		urlConnection.connect();
	                		
	                		int responseCode =urlConnection.getResponseCode();
	                		System.out.println("responseCode:"+responseCode);
	                		if ( responseCode== 200) { 
	                		  InputStream in = urlConnection.getInputStream();  
	                		  result = read(in);
	                		  System.out.println("result:"+result);
	                		}
	                		
	                		urlConnection.disconnect();
	                
	                	}catch (Exception e) {
	        				// TODO: handle exception
	                		e.printStackTrace();
	        			}	
	                	 //Message msg = new Message();
	                     //msg.obj  = result;
	                     //msg.what = Global.NOTIFYDRIVER;
	                     //handler.sendMessage(msg); 	     
	            	     isRunning = false;
					 } catch (Exception e1) {
						 e1.printStackTrace();
				     }
	               }
	            }
	        });
		connectThread.start();
	}

	 private String read(InputStream in) throws IOException {  
         byte[] data;  
         ByteArrayOutputStream bout = new ByteArrayOutputStream();  
         byte[]buf = new byte[1024];  
         int len = 0;  
         while((len = in.read(buf))!=-1){  
             bout.write(buf, 0, len);  
         }  
         data = bout.toByteArray();  
         return new String(data,"UTF-8");  
     }  
}
