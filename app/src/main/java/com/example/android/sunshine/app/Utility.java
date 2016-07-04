package com.example.android.sunshine.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by My on 7/3/2016.
 */
public class Utility {
   public static String getPreferenceValue(Context context, int keyResource, int defaultValueResource) {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
      String key = context.getString(keyResource);
      String defaultValue = context.getString(defaultValueResource);
      return preferences.getString(key, defaultValue);
   }
}
