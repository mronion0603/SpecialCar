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

public class AddOfficePersonNet {
	private Handler handler;
	
	private boolean isRunning = false;
	String result = "网络连接错误！";
	String authn = "";
	String device = "";
	String serviceTypeId = "4";
	String cartype = "";
	String startTime = "";
	String startAddress = "";
	String endAddress = "";
	String riderName = "";
	String riderPhone = "";
	String comment = "";
	String sLongitude = "";
	String sLatitude = "";
	String eLatitude = "";
	String eLongitude = "";
	String mileage = "";
	String timeOut = "";
	String realMoney = "";
	String basicMoney = "";
	String timeOutMoney = "";
	String longFootMoney = "";
	String voucherNum = "";
	String voucherMoney = "";
	String driverNum = "";
	String economy = "0";
	String common = "0";
	String business = "0";
	String useCarTime = "0";
	public AddOfficePersonNet(){
		
	}
	public void setUseCarTime(String useCarTime){
		this.useCarTime = useCarTime;
	}
	public void setEconomy(String economy){
		this.economy = economy;
	}
	public void setCommon(String common){
		this.common = common;
	}
	public void setBusiness(String business){
		this.business = business;
	}
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	public void setDriverNum(String driverNum){
		this.driverNum = driverNum;
	}
	public void setServiceTypeId(String serviceTypeId){
		this.serviceTypeId = serviceTypeId;
	}
	public void setCartype(String cartype){
		this.cartype = cartype;
	}
	public void setStartTime(String startTime){
		this.startTime = startTime;
	}
	public void setRiderName(String riderName){
		this.riderName = riderName;
	}
	public void setRiderPhone(String riderPhone){
		this.riderPhone = riderPhone;
	}
	public void setRealMoney(String realMoney){
		this.realMoney = realMoney;
	}
	public void setDevice(String device){
		this.device = device;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public void setAuthn(String authn) {
		this.authn = authn;
	}
	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}
	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setsLongitude(String sLongitude) {
		this.sLongitude = sLongitude;
	}
	public void setsLatitude(String sLatitude) {
		this.sLatitude = sLatitude;
	}
	public void seteLatitude(String eLatitude) {
		this.eLatitude = eLatitude;
	}
	public void seteLongitude(String eLongitude) {
		this.eLongitude = eLongitude;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
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
	         		    String strurl =ConnectUrl.addinneturl;
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
	                				+"&serviceTypeId=" + URLEncoder.encode(serviceTypeId, "UTF-8")
	                			    +"&carTypeId=" + URLEncoder.encode(cartype, "UTF-8")
	                			    +"&startTime=" + URLEncoder.encode(startTime, "UTF-8")
	                			+"&startAddress=" + URLEncoder.encode(startAddress, "UTF-8")
	                			+"&riderName=" + URLEncoder.encode(riderName, "UTF-8")
	                			+"&riderPhone=" + URLEncoder.encode(riderPhone, "UTF-8")
	                			+"&comment=" + URLEncoder.encode(comment, "UTF-8")
	                			+"&sLongitude=" + URLEncoder.encode(sLongitude, "UTF-8")
	                			+"&sLatitude=" + URLEncoder.encode(sLatitude, "UTF-8")
	                				+"&realMoney=" + URLEncoder.encode(realMoney, "UTF-8")
	                				+"&useCarTime=" + URLEncoder.encode(useCarTime, "UTF-8")
	                				;
	                		System.out.println(content);
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
	                     msg.what = Global.ADDINNER;
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
