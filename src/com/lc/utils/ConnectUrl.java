package com.lc.utils;

public class ConnectUrl {
	//public static String basic = "http://120.24.172.238:8080/";
	public static String basic = "http://112.74.82.102/";
 public static String commonurl0 = basic+"image/";
 public static String commonurl = basic+"specialCar/";	
 public static String commondriverurl = basic+"specialcardriver/";	
 
 public static String getcodeurl = commonurl+"sendAuthCode";				 //手机号注册验证码
 public static String getloginurl = commonurl+"login";				 //手机号注册验证码
 public static String getlogincivilurl = commonurl+"civilLogin";				 //手机号注册验证码
 public static String getcityurl = commonurl+"cities";				 //手机号注册验证码
 public static String getintercarpoolurl = commonurl+"getcjOrder";				 //手机号注册验证码
 public static String addintercarpoolurl = commonurl+"addOrder";	
 public static String updateuserurl = commonurl+"updateUser";	
 public static String getmessageurl = commonurl+"getMessage";	
 public static String addAddress = commonurl+"addAddress";	
 public static String getAddress = commonurl+"getAddress";	
 public static String getRouteMatrix ="http://api.map.baidu.com/direction/v1/routematrix";  //获取百度的距离时间评估	
 public static String getUserinfo = commonurl+"getUser";
 public static String addinneturl = commonurl+"addOrder";	
 public static String addnotifydriver = commondriverurl+"pushCJ.ct";	
 public static String getCarType = commonurl+"getCarType";	
 public static String getNearbyDriver = commonurl+"selSNCoord";
 public static String addnotifydriverinner = commondriverurl+"pushSN.ct";
 public static String getBlance = commonurl+"selBalance";
 public static String getDiscount = commonurl+"getVoucher";
 public static String getOrder = commonurl+"getOrder";
 public static String getAccount = commonurl+"getAccount";
 public static String getPayOrder = commonurl+"addAliPayMoney";
 public static String getWXPayOrder = commonurl+"addWeChatPayMoney";
 public static String cancelInnerOrder = commonurl+"cancelSNOrder";
 public static String getAirport= commonurl+"selAirportByCity";	
 public static String addReceipt= commonurl+"addBill";	
 public static String getMaxReceipt= commonurl+"selBillMoney";	
 public static String delAddress= commonurl+"delAddress";
 public static String addFeedback = commonurl+"opinion";
 public static String getupdate = commonurl+"checkVersion";
 public static String getcard = commonurl+"getCivilCardInfo";
 public static String addscore = commonurl+"assess";
 public static String cancelCity = commonurl+"cancelCJOrder";
 public static String bill = basic+"app/priceinfo.html";
 public static String getitinerartdetail = commonurl+"selUserOrderInfo";
 public static String addmoney = commonurl+"rechCard";
 public static String agreement = basic+"app/info.html";
 public static String exit = commonurl+"exit";
}
