package com.lc.utils;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;
public class DeleteDealApplication extends Application {
 private List<Activity> activityList = new LinkedList<Activity>();
 private static DeleteDealApplication instance;
 private DeleteDealApplication()
 {
 }

 public static DeleteDealApplication getInstance()
 {
 if(null == instance)
 {
 instance = new DeleteDealApplication();
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
 }
 //System.exit(0);
 }
 }
