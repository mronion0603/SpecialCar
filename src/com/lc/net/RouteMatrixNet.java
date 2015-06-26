package com.lc.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.lc.utils.ConnectUrl;
import com.lc.utils.Global;

import android.os.Handler;
import android.os.Message;

public class RouteMatrixNet {
	private Handler handler;
	private boolean isRunning = false;
	String result = "网络连接错误！";
	String output = "json";
	String origins= "";
	String destinations= "";
	String ak= "KH7XhjdrRrF7XSWurvOUaGF5";
	String mode= "driving";
	String mcode = "68:22:67:A3:12:0E:51:27:79:D5:57:6A:83:09:76:F9:27:57:29:11;com.lc.specialcar";
	
	
	public RouteMatrixNet(){
		
	}
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	public void setOrigins(String origins){
		this.origins = origins;
	}
	public void setDestinations(String destinations){
		this.destinations = destinations;
	}
	public void getCodeFromServer(){
		isRunning = true;
		Thread connectThread; //声明一个子线程
		 connectThread = new Thread(new Runnable() {
	        	
	            @Override
	              public void run() {       
	                while (isRunning) {
					  try {
						//Thread.currentThread().sleep(10);
	         		    String strurl =ConnectUrl.getRouteMatrix;
	                	//URL url = null;
	                	try{
	                		String spec = "http://api.map.baidu.com/direction/v1/routematrix?origins="  
	                                + URLEncoder.encode(origins, "UTF-8")
	                                +"&destinations=" + URLEncoder.encode(destinations, "UTF-8")
	                                +"&ak=" + URLEncoder.encode(ak, "UTF-8")
	                                +"&output=" + URLEncoder.encode(output, "UTF-8")
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
	                		//DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
	                		
	                		String content ="origins=" + URLEncoder.encode(origins, "UTF-8")
	                				+"&destinations=" + URLEncoder.encode(destinations, "UTF-8")
	                				+"&ak=" + URLEncoder.encode(ak, "UTF-8")
	                				+"&output=" + URLEncoder.encode(output, "UTF-8")
	                				+"&mcode=" + URLEncoder.encode(mcode, "UTF-8")
	                				;
	                		//System.out.println(content);
	                		//outputStream.writeBytes(content);
	                		//outputStream.flush();
	                		//outputStream.close();
	                		
	                		int responseCode =urlConnection.getResponseCode();
	                		if ( responseCode== 200) { 
	                		InputStream in = urlConnection.getInputStream();  
	                		result = read(in);
	                		
	                		}
	                		urlConnection.disconnect();
	                
	                	}catch (Exception e) {
	        				// TODO: handle exception
	                		e.printStackTrace();
	        			}	
	                	 Message msg = new Message();
	                     msg.obj  = result;
	                     msg.what = Global.GETBAIDUROUTE;
	                     handler.sendMessage(msg); 	     
	            	     isRunning = false;
					 } catch (Exception e1) {
								// TODO Auto-generated catch block
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
