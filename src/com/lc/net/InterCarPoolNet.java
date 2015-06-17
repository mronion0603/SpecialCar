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

public class InterCarPoolNet {
	private Handler handler;
	private boolean isRunning = false;
	String result = "网络连接错误！";

	String authn = "";
	String device = "";
	String startAddress = "";
	String endAddress = "";
	String date = "";
	public InterCarPoolNet(){
		
	}
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	
	public void setStartAddress(String startAddress){
		this.startAddress = startAddress;
	}
	public void setEndAddress(String endAddress){
		this.endAddress = endAddress;
	}
	public void setDate(String date){
		this.date = date;
	}
	public void setDevice(String device){
		this.device = device;
	}
	public void setAuth(String authn){
		this.authn = authn;
	}
	public void getDataFromServer(){
		isRunning = true;
		Thread connectThread; //声明一个子线程
		 connectThread = new Thread(new Runnable() {
	        	
	            @Override
	              public void run() {       
	                while (isRunning) {
					  try {
						//Thread.currentThread().sleep(10);
	         		    String strurl =ConnectUrl.getintercarpoolurl;
	                	URL url = null;
	                	try{
	                		url = new URL(strurl);
	                		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
	                		urlConnection.setDoOutput(true);
	                		urlConnection.setDoInput(true);
	                		urlConnection.setRequestMethod("POST");
	                		urlConnection.setUseCaches(false);
	                		
	                		urlConnection.setRequestProperty("Mime-Type", "application/x-www-form-urlencoded");
	                		urlConnection.connect();
	                		DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
	                		
	                		String content = "authn=" + URLEncoder.encode(authn, "UTF-8")
	                				+"&device=" + URLEncoder.encode(device, "UTF-8")
	                				+"&startAddress=" + URLEncoder.encode(startAddress, "UTF-8")
	                				+"&endAddress=" + URLEncoder.encode(endAddress, "UTF-8")
	                				+"&date=" + URLEncoder.encode(date, "UTF-8");
	                		
	                		outputStream.writeBytes(content);
	                		outputStream.flush();
	                		outputStream.close();
	                		
	                		int responseCode =urlConnection.getResponseCode();
	                		if ( responseCode== 200) { 
	                		  InputStream in = urlConnection.getInputStream();  
	                		  result = read(in);
	                		  //System.out.println(result);
	                		}
	                		urlConnection.disconnect();
	                
	                	}catch (Exception e) {
	        				// TODO: handle exception
	                		e.printStackTrace();
	        			}	
	                	 Message msg = new Message();
	                     msg.obj  = result;
	                     msg.what = Global.INTERCARPOOL;
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
