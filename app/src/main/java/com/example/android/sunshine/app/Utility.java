package com.example.android.sunshine.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Date;

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

   public static String getPreferredLocation(Context context) {
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
      return prefs.getString(context.getString(R.string.pref_location_key),
            context.getString(R.string.pref_location_default));
   }

   public static boolean isMetric(Context context) {
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
      return prefs.getString(context.getString(R.string.pref_units_key),
            context.getString(R.string.pref_units_value_metric))
            .equals(context.getString(R.string.pref_units_value_metric));
   }

   static String formatTemperature(double temperature, boolean isMetric) {
      double temp;
      if ( !isMetric ) {
         temp = 9*temperature/5+32;
      } else {
         temp = temperature;
      }
      return String.format("%.0f", temp);
   }

   static String formatDate(long dateInMillis) {
      Date date = new Date(dateInMillis);
      return DateFormat.getDateInstance().format(date);
   }
}
