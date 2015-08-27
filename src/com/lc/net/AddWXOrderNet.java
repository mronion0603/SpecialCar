package com.lc.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.lc.utils.ConnectUrl;

import android.os.Handler;
import android.os.Message;



public class AddWXOrderNet {
	private Handler handler;
	private boolean isRunning = false;
	String result = "N";
	String device = "";
	String addMoney = "";
	String authn = "";
	public AddWXOrderNet(){
		
	}
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	public void setDevice(String device){
		this.device = device;
	}
	public void setAddMoney(String addMoney){
		this.addMoney = addMoney;
	}
	public void setAuthn(String authn){
		this.authn = authn;
	}
	public void addOrderServer(){
		isRunning = true;
		Thread connectThread; //声明一个子线程
		 connectThread = new Thread(new Runnable() {
	        	
	            @Override
	              public void run() {       
	                while (isRunning) {
					  try {
						Thread.currentThread().sleep(100);
						//System.out.println("vvvvvvvvvvvv");
	         		    String strurl =ConnectUrl.getWXPayOrder;
	                	URL url = null;
	                	try{
	                		url = new URL(strurl);
	                		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
	                		urlConnection.setDoOutput(true);
	                		urlConnection.setDoInput(true);
	                		urlConnection.setRequestMethod("POST");
	                		urlConnection.setUseCaches(false);
	                		//urlConnection.setInstanceFollowRedirects(true);
	                		
	                		urlConnection.setRequestProperty("Mime-Type", "application/x-www-form-urlencoded");
	                		urlConnection.connect();
	                		DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
	                		
	                		String content = "device="+URLEncoder.encode(device, "UTF-8")
	                				 +"&addMoney=" + URLEncoder.encode(addMoney, "UTF-8")
	                				 +"&authn=" + URLEncoder.encode(authn, "UTF-8");
	                			   
	                		outputStream.writeBytes(content);
	                		outputStream.flush();
	                		outputStream.close();
	                		
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
	                     msg.obj = result;
	                     msg.what = 10;
	                     handler.sendMessage(msg); 	     
	            	     isRunning = false;
					 } catch (InterruptedException e1) {
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
