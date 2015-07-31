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

public class AddCommentNet {
	private Handler handler;
	private boolean isRunning = false;
	String result = "网络连接错误！";
	String authn = "";
	String device = "";
	String score = "";
	String mcontent  = "";
	String orderNum  = "";
	public AddCommentNet(){
		
	}
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	public void setorderNum(String orderNum){
		this.orderNum = orderNum;
	}
	public void setScore(String score){
		this.score = score;
	}
	public void setcontent(String content){
		this.mcontent = content;
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
	         		    String strurl =ConnectUrl.addscore;
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
	                				+"&score=" + URLEncoder.encode(score, "UTF-8")
	                				+"&content=" + URLEncoder.encode(mcontent, "UTF-8")
	                				+"&orderNum=" + URLEncoder.encode(orderNum, "UTF-8")
	                				;
	                		outputStream.writeBytes(content);
	                		outputStream.flush();
	                		outputStream.close();
	                		
	                		int responseCode =urlConnection.getResponseCode();
	                		 System.out.println(responseCode);
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
	                     msg.what = Global.ADDSCORE;
	                     handler.sendMessage(msg); 	     
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
