package com.lc.utils;

public class ConnectUrl {
 public static String commonurl = "http://120.24.172.238:8080/specialCar/";	//测试
 public static String commondriverurl = "http://120.24.172.238:8080/specialcardriver/";	//测试
 //public static String commonurl = "http://121.40.54.74:8080/";	//alpha地址
 
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
 
}
