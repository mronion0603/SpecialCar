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

public class FeedBackNet {
	private Handler handler;
	private boolean isRunning = false;
	String result = "N";
	String device = "";
	String mcontent = "";
	String authn = "";
	public FeedBackNet(){
		
	}
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	public void setDevice(String device){
		this.device = device;
	}
	public void setContent(String content){
		this.mcontent = content;
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
						//System.out.println("vvvvvvvvvvvv");
	         		        String strurl =ConnectUrl.addFeedback;
	                	    URL url = null;
	         
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
	                				 +"&content=" + URLEncoder.encode(mcontent, "UTF-8")
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
	                     msg.what = Global.ADDFEEDBACK;
	                     handler.sendMessage(msg); 	     
	            	     isRunning = false;
					 
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
