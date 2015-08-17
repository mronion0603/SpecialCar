package com.lc.utils;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;
public class DeleteWXPayApplication extends Application {
 private List<Activity> activityList = new LinkedList<Activity>();
 private static DeleteWXPayApplication instance;
 private DeleteWXPayApplication()
 {
 }

 public static DeleteWXPayApplication getInstance()
 {
 if(null == instance)
 {
 instance = new DeleteWXPayApplication();
 }
 return instance;
 }

 public void addActivity(Activity activity)
 {
 activityList.add(activity);
 }

 public void exit()
 {
 for(Activity activity:activityList)
 {
  
   activity.finish();
   //activityList.remove(activity);
 }
 //System.exit(0);
 }
 }
