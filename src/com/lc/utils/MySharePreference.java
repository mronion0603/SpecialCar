package com.lc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharePreference {
	 public static final String PREFERENCES_PUBLIC_NAME = "common";
	 public static final String PREFERENCES_PERSONAL_NAME = "userinfo";
	 
	 public static final String USER_TYPE = "usertype";
	 public static final String PHONE = "phone";
	 public static final String AUTHN = "authn";
	 public static final String USERNAME = "username";
	 public static final String EMAIL = "email";
	 public static final String GENDER = "gender";
	 public static final String UUID = "uuid";
	 public static final String CITY = "city";
	 public static final String LAT = "lat";
	 public static final String LONT = "lont";
	 public static final String BALANCE = "balance";
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
	 
	  public static void clearAll(Context context) {
	        clearPersonal(context);
	        SharedPreferences pref = getSharedPreferences(context, false);
	        Editor editor = pref.edit();
	        editor.clear();
	        editor.commit();
	        
	    }

	    public static void clearPersonal(Context context) {
	        SharedPreferences pref = getSharedPreferences(context, true);
	        Editor editor = pref.edit();
	        editor.clear();
	        editor.commit();
	       
	    }
}
