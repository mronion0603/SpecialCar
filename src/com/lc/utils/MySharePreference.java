package com.lc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharePreference {
	 public static final String PREFERENCES_PUBLIC_NAME = "common";
	 private static final String PREFERENCES_PERSONAL_NAME = "userinfo";
	 
	 public static final String USER_TYPE = "usertype";
	 private static SharedPreferences getSharedPreferences(Context context,
	            boolean isPersonal) {
	        if (isPersonal) {
	            return context.getSharedPreferences(PREFERENCES_PERSONAL_NAME,
	                    Context.MODE_PRIVATE);
	        } else {
	            return context.getSharedPreferences(PREFERENCES_PUBLIC_NAME,
	                    Context.MODE_PRIVATE);
	        }
	    }
	 public static final void editStringValue(Context context, String key,
	            String value, boolean isPersonal) {
	        SharedPreferences pref = getSharedPreferences(context, isPersonal);
	        Editor editor = pref.edit();
	        editor.putString(key, value);
	        editor.commit();
	    }
	 public static final void editStringValue(Context context, String key,
	            String value) {
		    editStringValue(context,key,value,true);
	    }
	 public static final String getStringValue(Context context, String key,
	            String value, boolean isPersonal) {
		    SharedPreferences pref = getSharedPreferences(context, isPersonal);
	        return pref.getString(key, null);
	    }
	 public static final String getStringValue(Context context, String key) {
		    SharedPreferences pref = getSharedPreferences(context, true);
	        return pref.getString(key, null);
	    }
}